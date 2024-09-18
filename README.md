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


## Note on remaining TODOs and Examples from template app

We have tried to provide some examples of best practice in the application - so there are lots of TODOs in the code
where changes are required to meet your requirements. There is an `ExampleResource` that includes best practice and also
serve as spring security examples. The template typescript project has a demonstration that calls this endpoint as well.

For the demonstration, rather than introducing a dependency on a different service, this application calls out to
itself. This is only to show a service calling out to another service and is certainly not recommended!


