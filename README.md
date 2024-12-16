# hmpps-electronic-monitoring-datastore-api

[![repo standards badge](https://img.shields.io/badge/endpoint.svg?&style=flat&logo=github&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-electronic-monitoring-datastore-api)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-report/hmpps-electronic-monitoring-datastore-api "Link to report")
[![CircleCI](https://circleci.com/gh/ministryofjustice/hmpps-electronic-monitoring-datastore-api/tree/main.svg?style=svg)](https://circleci.com/gh/ministryofjustice/hmpps-electronic-monitoring-datastore-api)
[![Docker Repository on Quay](https://img.shields.io/badge/quay.io-repository-2496ED.svg?logo=docker)](https://quay.io/repository/hmpps/hmpps-electronic-monitoring-datastore-api)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-electronic-monitoring-datastore-api-dev.hmpps.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

API to access the Electronic Monitoring datastore in the Modernisation Platform.

## Running the application locally

The application comes with a `dev` spring profile that includes default settings for running locally. This is not
necessary when deploying to kubernetes as these values are included in the helm configuration templates -
e.g. `values-dev.yaml`.

### Checking the app has started successfully:
If using docker, your app is probably exposed at `localhost:8080`.  
Call http://localhost:8080/health with a browser to get app health info.

### Running with Docker
There is also a `docker-compose.yml` that can be used to run a local instance of the template in docker and also an
instance of HMPPS Auth (required if your service calls out to other services using a token).

```bash
docker compose pull && docker compose up
```

will build the application and run it and HMPPS Auth within a local docker instance.

### Running the application in Intellij

```bash
docker compose pull && docker compose up --scale hmpps-electronic-monitoring-datastore-api=0
```

will just start a docker instance of HMPPS Auth. The application should then be started with a `dev` active profile
in Intellij.

## Vulnerability analysis
Gradle includes OWASP dependency checking. Run this locally using:
1. `./gradlew dependencyCheckUpdate --info` to update the definitions file
2. `./gradlew dependencyCheckAnalyze --info` to run the check.
The results will be found in [./build/reports/dependency-check-report.html](./build/reports/dependency-check-report.html)

To run trivy analysis on the built image locally, run:
1. `docker compose build` to build the image
2. `brew install aquasecurity/trivy/trivy` to install trivy
3. `trivy image <your image uid>` to scan.

### Code coverage
This project has Jacoco integrated, and this will run after each test run. The generated report can be found [here](build/reports/jacoco/test/html/index.html) and can be opened in your browser.

## Deployment
> Force-push your code to branch `deploy-dev` to deploy it to dev.  
> This cannot deploy past dev, but is otherwise the same as the main deployment pipeline.
>
> This is configured in [.circleci/config.yml](/.circleci/config.yml) in the `hmpps/deploy_env:` action for dev.

- `main` is deployed via [CircleCI](https://app.circleci.com/pipelines/github/ministryofjustice/hmpps-electronic-monitoring-datastore-api)
- Kubernetes logs can be found on [Kibana](https://kibana.cloud-platform.service.justice.gov.uk/_plugin/kibana/app/discover#/?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(columns:!(log,kubernetes.pod_id),filters:!(('state':(store:appState),meta:(alias:!n,disabled:!f,index:'167701b0-f8c0-11ec-b95c-1d65c3682287',key:kubernetes.namespace_name.keyword,negate:!f,params:(query:tst-em-example-app-jkn-dev),type:phrase),query:(match_phrase:(kubernetes.namespace_name.keyword:tst-em-example-app-jkn-dev))),('state':(store:appState),meta:(alias:!n,disabled:!f,index:'167701b0-f8c0-11ec-b95c-1d65c3682287',key:kubernetes.pod_name,negate:!f,params:(query:hmpps-electronic-monitoring-datastore-api),type:phrase),query:(match_phrase:(kubernetes.pod_name:hmpps-electronic-monitoring-datastore-api)))),index:'167701b0-f8c0-11ec-b95c-1d65c3682287',interval:auto,query:(language:kuery,query:''),sort:!()))

## Accessing Athena
This application gets its data from Athena, in the Modernisation Platform. The overall flow for this is as follows:
- The app namespace in Cloud Platform Environments has an IRSA account defined.
  - This IRSA account has permissions to assume the roles of accounts in the Modernisation Platform
  - On the mod plat side, these accounts are configured to accept assume-role requests from this IRSA account
- This service account is injected into the API app in the helm config
- When the API needs to connect to Athena, it authenticates with AWS using the AWS Java SDK's `DefaultCredentialsProvider()` method, which lets the API use this service account.
- The [AssumeRoleService](src/main/kotlin/uk/gov/justice/digital/hmpps/electronicmonitoringdatastoreapi/service/AssumeRoleService.kt) class uses these creds and from them generate an `StsAssumeRoleCredentialsProvider` for this role
- This is passed to the [AthenaService](src/main/kotlin/uk/gov/justice/digital/hmpps/electronicmonitoringdatastoreapi/service/AthenaService.kt) which creates the Athena connection

### Testing Athena Access  
A Service Pod is provisioned in the cloud platform with the above IRSA role, which can be accessed via the kubernetes CLI.  
This pod is periodically recreated so you must re-check the pod name before use.  
To use this pod (while in the correct Kubernetes namespace)
1. Get the name of the service pod with `kubectl get pods` - e.g. hmpps-em-datastore-dev-athena-service-pod-6d694cfd48-zqhtr
2. Connect to the service pod with `kubectl exec --stdin --tty POD_NAME_HERE -- /bin/bash`
3. Confirm you have the expected IAM role with `aws sts get-caller-identity`
   - This should be of the form `arn:aws:sts::CLOUD-PLATFORM-ACCOUNT-NUMBER:assumed-role/cloud-platform-irsa-FAKESTRING-live/botocore-session-FAKEID`
   - It should match the content of the Kubernetes secret for your environment: Obtain this with:  
   `cloud-platform decode-secret -n hmpps-electronic-monitoring-datastore-dev -s hmpps-electronic-monitoring-datastore-dev-irsa-output`
4. Get temporary credentials to assume the correct Mod Plat athena role by running:  
   `aws sts assume-role --role-arn arn:aws:sts::MOD-PLATFORM-ACCOUNT-NUMBER:role/ROLENAME --role-session-name terminal-session`  
   This should return an AccessKeyId, SecretAccessKey, and SessionToken you can use to run athena queries as per [Using temporary security credentials with the aws cli](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_use-resources.html#using-temp-creds-sdk-cli)
5. These can be used in the environment variables to run the API within IntelliJ using local.yml

## Note on remaining TODOs and Examples from template app

We have tried to provide some examples of best practice in the application - so there are lots of TODOs in the code
where changes are required to meet your requirements. There is an `ExampleResource` that includes best practice and also
serve as spring security examples. The template typescript project has a demonstration that calls this endpoint as well.

For the demonstration, rather than introducing a dependency on a different service, this application calls out to
itself. This is only to show a service calling out to another service and is certainly not recommended!

