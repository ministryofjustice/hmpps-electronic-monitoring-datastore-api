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
    - [Running the application locally](#running-the-application-locally)
        - [Calling endpoints](#calling-endpoints)
            - [Generate a token for a HMPPS Auth client](#generate-a-token-for-a-hmpps-auth-client)
        - [Running the application locally with the UI](#running-both-the-ui-and-the-api-locally)

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

### Running the application locally

To run the application using IntelliJ:

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

## Note on remaining TODOs and Examples from template app

We have tried to provide some examples of best practice in the application - so there are lots of TODOs in the code
where changes are required to meet your requirements. There is an `ExampleResource` that includes best practice and also
serve as spring security examples. The template typescript project has a demonstration that calls this endpoint as well.

For the demonstration, rather than introducing a dependency on a different service, this application calls out to
itself. This is only to show a service calling out to another service and is certainly not recommended!

