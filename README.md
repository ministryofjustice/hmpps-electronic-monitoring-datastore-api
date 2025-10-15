# hmpps-electronic-monitoring-datastore-api

[![repo standards badge](https://img.shields.io/badge/endpoint.svg?&style=flat&logo=github&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-electronic-monitoring-datastore-api)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-report/hmpps-electronic-monitoring-datastore-api "Link to report")
[![CircleCI](https://circleci.com/gh/ministryofjustice/hmpps-electronic-monitoring-datastore-api/tree/main.svg?style=svg)](https://circleci.com/gh/ministryofjustice/hmpps-electronic-monitoring-datastore-api)
[![Docker Repository on Quay](https://img.shields.io/badge/quay.io-repository-2496ED.svg?logo=docker)](https://quay.io/repository/hmpps/hmpps-electronic-monitoring-datastore-api)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-electronic-monitoring-datastore-api-dev.hmpps.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

# Contents
- [About this project](#about-this-project)
- [Project set up](#project-set-up)
- [Running the application locally](#running-the-application-locally)
  - [Running the application in intellij](#running-the-application-in-intellij)
  - [Running the application with connection to Athena](#running-the-application-with-connection-to-dev-athena)
  - [Running the application with mocked Athena](#running-the-application-with-mocked-athena)

## About this project

An API used by the [Electronic Monitoring datastore UI](https://github.com/ministryofjustice/hmpps-electronic-monitoring-datastore-ui),
a service that allows users to interrogate the electronic monitoring datastore hosted in the HMPPS Modernisation Platform.

It is built using [Spring Boot](https://spring.io/projects/spring-boot/) and [Kotlin](https://kotlinlang.org/) as well as the following technologies for its infrastructure:

  - [AWS](https://aws.amazon.com/) - Services utilise AWS features through Cloud Platform.
  - [Cloud Platform](https://user-guide.cloud-platform.service.justice.gov.uk/#cloud-platform-user-guide) - Ministry of Justice's (MOJ) cloud hosting platform built on top of AWS which offers numerous tools such as logging, monitoring and alerting for our services.
  - [Docker](https://www.docker.com/) - The API is built into docker images which are deployed to our containers.
  - [Kubernetes](https://kubernetes.io/docs/home/) - Creates 'pods' to host our environment. Manages auto-scaling, load balancing and networking to our application.

## Project set up

Enable pre-commit hooks for formatting and linting code with the following command;

```bash
./gradlew addKtlintFormatGitPreCommitHook addKtlintCheckGitPreCommitHook
```

## Running the application locally

The application comes with a `local` spring profile that includes default settings for running locally.

There is also a `docker-compose.yml` that can be used to run a local instance in docker and also an
instance of HMPPS Auth.

```bash
docker compose pull && docker compose up
```

will build the application and run it and HMPPS Auth within a local docker instance.

### Running the application in Intellij

```bash
docker compose pull && docker compose up --scale hmpps-electronic-monitoring-datastore-api=0
```
will just start a docker instance of HMPPS Auth. The application should then be started with a `local` active profile
in Intellij.

### Connecting to the cloud platform kubernetes cluster

- Configure KubeCtl to let you connect to the Cloud Platform Kubernetes cluster - [follow this guide](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/getting-started/kubectl-config.html)

### Running the application with connection to dev Athena

When deployed to cloud-platform, the pod running the service is configured to use a service account which gives
it the permissions of the linked IAM role. The linked IAM role is allowed to assume a role that gives it access to the
Electronic Monitoring Datastore (Athena). When we're running the application locally, we want to try to connect to
AWS/Athena in a similar way to the deployed service. To achieve this, we can get a programmatic access key which will
give our workstation access to AWS.

Navigate to https://moj.awsapps.com/start/#/?tab=accounts and retrieve access keys for
`electronic-monitoring-data-test`. Add these to the `.env` file in root of this project. N.B. These will expire so you
will need to refresh them periodically.

e.g.
```env
AWS_ACCESS_KEY_ID="ASIAIOSFODNN7EXAMPLE"
AWS_SECRET_ACCESS_KEY="wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
AWS_SESSION_TOKEN="IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZVERYLONGSTRINGEXAMPLE"
```

These credentials will allow access to Athena without any additional configuration, however, the goal is to connect
to Athena in a similar way the deployed service will, so we will also configure a role to assume. The
`./scripts/get-athena-credentials.sh` script will fetch the ARN of the role the dev environment will use to connect to
Athena. Add this to your `.env` file.

e.g.
```env
ATHENA_GENERAL_IAM_ROLE=""
```

### Running tests

When we run tests either via the command line or IntelliJ, the AWS SDK still expects to find credentials from any of the
default credential providers.

The easiest way to get around this is to add some dummy variables to the AWS credentials file `~/.aws/credentials`. This should allow you to
run any test in IntelliJ or from the cli e.g. `./gradlew check`.

```text
AWS_ACCESS_KEY_ID="ASIAIOSFODNN7EXAMPLE"
AWS_SECRET_ACCESS_KEY="wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
AWS_SESSION_TOKEN="IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZVERYLONGSTRINGEXAMPLE"
```

### Code coverage
This project has Jacoco integrated and this will run after each test run. The generated report can be found [here](build/reports/jacoco/test/html/index.html) and can be opened in your browser.

## Vulnerability analysis

Gradle includes OWASP dependency checking. Run this locally using:
1. `./gradlew dependencyCheckUpdate --info` to update the definitions file
2. `./gradlew dependencyCheckAnalyze --info` to run the check.
   The results will be found in [./build/reports/dependency-check-report.html](./build/reports/dependency-check-report.html)

To run trivy analysis on the built image locally, run:
1. `docker compose build` to build the image
2. `brew install aquasecurity/trivy/trivy` to install trivy
3. `trivy image <your image uid>` to scan.
