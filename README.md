# hmpps-electronic-monitoring-datastore-api

[![repo standards badge](https://img.shields.io/badge/endpoint.svg?&style=flat&logo=github&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-electronic-monitoring-datastore-api)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-report/hmpps-electronic-monitoring-datastore-api "Link to report")
[![CircleCI](https://circleci.com/gh/ministryofjustice/hmpps-electronic-monitoring-datastore-api/tree/main.svg?style=svg)](https://circleci.com/gh/ministryofjustice/hmpps-electronic-monitoring-datastore-api)
[![Docker Repository on Quay](https://img.shields.io/badge/quay.io-repository-2496ED.svg?logo=docker)](https://quay.io/repository/hmpps/hmpps-electronic-monitoring-datastore-api)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-electronic-monitoring-datastore-api-dev.hmpps.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

## Contents
- [About this project](#about-this-project)
- [Get started](#get-started)
    - [Using IntelliJ IDEA](#using-intellij-idea)
- [Usage](#usage)
    - [Running the application locally from the CLI](#running-the-application-locally-from-the-cli)
    - [Running with Docker](#running-with-docker)
    - [Running the application locally using Intellij](#running-the-application-locally-using-intellij)
- [Querying Athena](#querying-athena)
  - [Overview](#overview)
  - [Acquiring local credentials](#acquiring-local-credentials)
  - [Querying with the AWS CLI](#querying-athena-via-the-cli)
  - [Querying Athena with the local EM API](#querying-athena-with-the-local-em-api)

## About this project

An API used by the [Electronic Monitoring datastore UI](https://github.com/ministryofjustice/hmpps-electronic-monitoring-datastore-ui),
a service that allows users to interrogate the electronic monitoring datastore hosted in the HMPPS Modernisation Platform.

It is built using [Spring Boot](https://spring.io/projects/spring-boot/) and [Kotlin](https://kotlinlang.org/)
as well as the following technologies for its infrastructure:
- [AWS](https://aws.amazon.com/) - Services utilise AWS features through Cloud Platform.
- [CircleCI](https://circleci.com/developer) - Used for our build platform, responsible for executing workflows to
  build, validate, test and deploy our project.
- [Cloud Platform](https://user-guide.cloud-platform.service.justice.gov.uk/#cloud-platform-user-guide) - Ministry of
  Justice's (MOJ) cloud hosting platform built on top of AWS which offers numerous tools such as logging, monitoring and
  alerting for our services.
- [Docker](https://www.docker.com/) - The API is built into docker images which are deployed to our containers.
- [Kubernetes](https://kubernetes.io/docs/home/) - Creates 'pods' to host our environment. Manages auto-scaling, load
  balancing and networking to our application.

## Get started

### Using IntelliJ IDEA

When using an IDE like [IntelliJ IDEA](https://www.jetbrains.com/idea/), getting started is very simple as it will
handle installing the required Java SDK and [Gradle](https://gradle.org/) versions. The following are the steps for
using IntelliJ but other IDEs will prove similar.

1. Clone the repo with the following command;

```bash
git clone git@github.com:ministryofjustice/hmpps-electronic-monitoring-datastore-api.git
```

2. Launch IntelliJ and open the `hmpps-electronic-monitoring-datastore-api` project by navigating to the location 
of the repository.

Upon opening the project, IntelliJ will begin downloading and installing necessary dependencies which may take a few
minutes.

3. Enable pre-commit hooks for formatting and linting code with the following command;

```bash
./gradlew addKtlintFormatGitPreCommitHook addKtlintCheckGitPreCommitHook
```

## Usage

### Connecting the local API to athena
- Acquire short-term credentials as per [Acquiring local credentials](#acquiring-local-credentials)
- Set the API to use these credentials as per [Querying Athena with the local EM API](#querying-athena-with-the-local-em-api)  

**Without these steps the local API will not talk to Athena**

### Running the application locally from the CLI

The application comes with a `dev` spring profile that includes default settings for running locally. This is not
necessary when deploying to kubernetes as these values are included in the helm configuration templates -
e.g. `values-dev.yaml`.


### Running with Docker
There is also a `docker-compose.yml` that can be used to run a local instance of the template in docker and also an
instance of HMPPS Auth (required if your service calls out to other services using a token).

```bash
docker compose pull && docker compose up
```

will build the application and run it and HMPPS Auth within a local docker instance.

### Running the application locally using Intellij

1. Run `docker compose pull && docker compose up --scale hmpps-electronic-monitoring-datastore-api=0`
, which will just start a docker instance of the database and HMPPS Auth.

3. Click the drop-down button for the `HmppsElectronicMonitoringDatastoreApi` run configuration file in the top 
right corner, and select Edit Configurations. 
    - For the 'Active Profiles' field, put 'local'
    - You may also need to set the JDK to openjdk-23 or openjdk-21
    - Apply these changes

4. Click the run button.

Or, to run the application using the command line:

```bash
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

Then visit [http://localhost:8080/health](http://localhost:8081/health).

### Checking the app has started successfully:
If using docker, your app is probably exposed at `localhost:8080`.  
Call http://localhost:8080/health with a browser to get app health info.


## Querying Athena
### Overview
The process by which the API queries Athena is:
1. API acquires a role from the Cloud Platform cluster it is deployed in (e.g. dev is deployed in [hmpps-electronic-monitoring-datastore-dev](https://github.com/ministryofjustice/cloud-platform-environments/tree/main/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-dev))
2. The [AthenaClient](src/main/kotlin/uk/gov/justice/digital/hmpps/electronicmonitoringdatastoreapi/client/AthenaClient.kt) requests a `CredentialsProvider` from the [AthenaAssumeRoleService](src/main/kotlin/uk/gov/justice/digital/hmpps/electronicmonitoringdatastoreapi/client/AthenaAssumeRoleService.kt)
3. The AthenaAssumeRoleService makes an `StsClient.assumeRole()` call using the local Cloud Platform role, and gives the `CredentialsProvider` this returns to the `AthenaClient`
4. The AthenaClient builds an AWS.AthenaClient with these credentials that has access to the appropriate data in the Athena datastore.

For debugging it's useful to be able to run these queries ourselves. To do this you will need:
- To be a member of [our GitHub team](https://github.com/orgs/ministryofjustice/teams/hmpps-electronic-monitoring)
- To configure KubeCtl to let you connect to the CLoud Platform Kubernetes cluster - [follow this guide](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/getting-started/kubectl-config.html)
- The [cloud platform CLI](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/getting-started/cloud-platform-cli.html)

### Acquiring local credentials
1. Set the kubectl context to the dev environment: `kubectl config set-context --current --namespace=hmpps-electronic-monitoring-datastore-dev`
2. Get details for the service pod that you can use to query AWS: `kubectl get pods`. One should have a name indicating it's a service account similar to `hmpps-em-datastore-dev-athena-service-pod-#Z###ZZZ##-Z####`.
3. Ssh into this service pod: `kubectl exec --stdin --tty YOUR_SERVICE_POD_NAME_FROM_THE_PREV_STEP -- /bin/bash`
   > Confirm you've signed in correctly by running `aws sts get-caller-identity` - this should return a response with an ARN matching the pattern `arn:aws:sts::############:assumed-role/cloud-platform-irsa-abc123xyz-live/botocore-session-##########` 
4. Confirm the Role ARN you require from [here](src/main/kotlin/uk/gov/justice/digital/hmpps/electronicmonitoringdatastoreapi/client/AthenaRole.kt)
5. Assume this role ([AWS docs](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/sts/assume-role.html)): `aws sts assume-role --role-arn YOUR_ATHENA_ROLE_ARN --role-session-name cli-session`
   > This will return AWS credentials including  a SessionToken, which will last around an hour
6. Open your local AWS credentials file: `nano ~/.aws/credentials`Add a section to your local .aws credentials file:
   > It should look like the following:  
   > ```[default]
   > region=eu-west-2
   > aws_access_key_id=SECRET_CHARACTER_STRING
   > aws_secret_access_key=LONGER_SECRET_CHARACTER_STRING
   > ```
7. Add a new section to this file - inputting the values without quotes:
   ```
   [athena-dev]
   aws_access_key_id=DATA_FROM_ASSUME_ROLE
   aws_secret_access_key=DATA_FROM_ASSUME_ROLE
   aws_session_token=DATA_FROM_ASSUME_ROLE
   region=eu-west-2
   ```
   
_You now have Athena credentials_ - they will last for 1 hour.

### Querying Athena via the CLI
- Once you have set up your role, use this profile in your aws requests by passing the flag `--profile athena-dev` with each request  
- Confirm you have access by running `aws athena list-work-groups --profile athena-dev` . This will open VIM with the results - typing `:q` will exit.
- The CLI docs are [here](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/athena/index.html)
- You can now run SQL queries that match what the API would run.

### Querying Athena with the local EM API
1. Edit your Spring Boot configuration file to include the following environment variables you retrieved in [acquiring local credentials](#acquiring-local-credentials):
   - `AWS_ACCESS_KEY_ID` = value you retrieved (no quotes)
   - `AWS_SECRET_ACCESS_KEY` = value you retrieved (no quotes)
   - `AWS_SESSION_TOKEN` = value you retrieved (no quotes)
   - `FLAG_USE_LOCAL_CREDS` = `true`
2. The [AthenaAssumeRoleService](src/main/kotlin/uk/gov/justice/digital/hmpps/electronicmonitoringdatastoreapi/client/AthenaAssumeRoleService.kt)`.getRole()` method will now use these values to create the athena connection at runtime.
3. To disable this, just set `FLAG_USE_LOCAL_CREDS` to `false`

This should pick up the values you set in your environment variables as per the [AWS Java SDK docs](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials-chain.html).

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

## Note on remaining TODOs and Examples from template app

We have tried to provide some examples of best practice in the application - so there are lots of TODOs in the code
where changes are required to meet your requirements. There is an `ExampleResource` that includes best practice and also
serve as spring security examples. The template typescript project has a demonstration that calls this endpoint as well.

For the demonstration, rather than introducing a dependency on a different service, this application calls out to
itself. This is only to show a service calling out to another service and is certainly not recommended!
