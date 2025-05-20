# Storing secrets as SSM parameters


## Contents
- [Introduction](#introduction)
- [Background knowledge](#background-knowledge)
- [Creating an SSM parameter](#creating-an-ssm-parameter)
- [Checking the value of an SSM parameter](#checking-the-value-of-an-ssm-parameter)
- [Setting the value of an SSM parameter](#setting-the-value-of-an-ssm-parameter)
- [Providing an SSM parameter to a service as a kubernetes secret](#providing-an-ssm-parameter-to-a-service-as-a-kubernetes-secret)

## Introduction

Some secrets used by this application, such as ARNs for AWS Athena resources, are stored using AWS SSM Parameter Store.  
We use SSM Parameter Store to avoid publicly exposing sensitive data and certain implementation details.

This document explains:
- [How to create new SSM parameters](#creating-an-ssm-parameter)
- [How to assign / update the value of an existing SSM parameter](#setting-the-value-of-an-ssm-parameter)
- [How to provide an SSM parameter to a service hosted in a kubernetes namespace using kubernetes secrets](#providing-an-ssm-parameter-to-a-service-as-a-kubernetes-secret)

## Background knowledge

- AWS SSM (Systems Manager) is a service used to manage and automate operational tasks on AWS resources.
- AWS SSM Parameter Store is a built-in feature of SSM, used to securely store parameters such as configuration data and secrets.
- We use AWS SSM Parameter Store to store secrets used by the EMDS API, such as ARNs for AWS Athena resources. Additional secrets can be added.
- We then provide these secrets to services running in the kubernetes namespace as kubernetes secrets.
- AWS resources and kubernetes secrets are provisioned / configured in [cloud-platform-environments](https://github.com/ministryofjustice/cloud-platform-environments).

## Creating an SSM parameter

To create an SSM parameter, configure the relevant [cloud platform environment](https://github.com/ministryofjustice/cloud-platform-environments) to provision that resource.  
The guide below uses examples from [the EMDS preprod cloud platform environment](https://github.com/ministryofjustice/cloud-platform-environments/tree/main/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod).
1. In the [SSM.tf config file](https://github.com/ministryofjustice/cloud-platform-environments/blob/main/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod/resources/ssm.tf), provision the requred SSM parameter resources as in [this example](https://github.com/ministryofjustice/cloud-platform-environments/blob/3d8e65733b17e7733eba59a986b4e37a82e8bc83/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod/resources/ssm.tf#L1), using placeholder values for the secret values. We'll replace these with real values using the AWS CLI in a later step. Don't create [data sources](https://github.com/ministryofjustice/cloud-platform-environments/blob/3d8e65733b17e7733eba59a986b4e37a82e8bc83/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod/resources/ssm.tf#L16) yet.
1. Create an [IAM policy](https://github.com/ministryofjustice/cloud-platform-environments/blob/3d8e65733b17e7733eba59a986b4e37a82e8bc83/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod/resources/iam-policies.tf#L34) & [IAM policy document](https://github.com/ministryofjustice/cloud-platform-environments/blob/3d8e65733b17e7733eba59a986b4e37a82e8bc83/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod/resources/iam-policies.tf#L20) granting permissions to get and put the SSM parameters.
3. [Assign the IAM policy to the IRSA](https://github.com/ministryofjustice/cloud-platform-environments/blob/3d8e65733b17e7733eba59a986b4e37a82e8bc83/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod/resources/cross-iam-role-sa.tf#L10). This will allow us to view and update the SSM parameter using the [service pod](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/other-topics/cloud-platform-service-pod.html#cloud-platform-service-pod-for-aws-cli-access).
4. Create a PR for these changes. Submit it to the Cloud Platform team in Slack for review & approval.  
Once the changes are merged the new AWS SSM property resources will be created.

## Checking the value of an SSM parameter

You can view the value of an SSM parameter using the AWS CLI via the [service pod](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/other-topics/cloud-platform-service-pod.html#cloud-platform-service-pod-for-aws-cli-access).

**NB:** When the SSM parameter is first created its value will be the placeholder value defined in cloud-platform-environments.  
Once its value has been [updated using the AWS CLI](#setting-the-value-of-an-ssm-parameter) its new value will persist even if the pods are redeployed.  
Unless something catastrophic happens it should only need to be set once.

To check the value of an SSM parameter:
1. View the pods in the relevant namespace:  
`kubectl get pods -n [namespace]`    
 eg. `kubectl get pods -n hmpps-electronic-monitoring-datastore-dev`  
One of the pods will include the term `service-pod`.

2. Use kubectl to access the service pod:  
`kubectl exec --stdin --tty [namespace] [service pod name] -- /bin/bash`  
eg. `kubectl exec --stdin --tty hmpps-electronic-monitoring-datastore-dev hmpps-em-datastore-dev-service-pod-abc123  -- /bin/bash` 

3. You can now enter AWS CLI commands to interact with AWS resources.  
To view an existing SSM parameter:  
`aws ssm get-parameter --name [SSM parameter name]`  
eg. `aws ssm get-parameter --name /hmpps-electronic-monitoring-datastore-dev/my-super-secret-value`  
**NB**: The parameters are base64 encoded. To view the decoded value:  
`aws ssm get-parameter --name [SSM parameter name] --with-decryption --query "Parameter.Value" --output text`

## Setting the value of an SSM parameter

When a SSM parameter is first created its value will be the placeholder value defined in cloud-platform-environments.  
Once its value has been updated using the AWS CLI its new value will persist even if the pods are redeployed.  
Unless something catastrophic happens it should only need to be set once.

1. Follow the steps in [Checking the value of an SSM parameter](#checking-the-value-of-an-ssm-parameter) to access the service pod.

2. To set the value of an SSM parameter:  
`aws ssm put-parameter --name "[SSM parameter name]" --value "[value to store]" --type SecureString --overwrite`  
eg. `aws ssm put-parameter --name "/hmpps-electronic-monitoring-datastore-dev/my-super-secret-value" --value "arn:aws:iam::123:role/my-role" --type SecureString --overwrite`

3. Follow the steps in [Checking the value of an SSM parameter](#checking-the-value-of-an-ssm-parameter) to check that the SSM parameter has the expected new value.

## Providing an SSM parameter to a service as a kubernetes secret

Once a SSM parameter has been provisioned and its value has been set,  
it can be provided to services running in the namespace as a kubernetes secret.

To provide a service with a value stored in SSM we need to make changes in [cloud-platform-environments](https://github.com/ministryofjustice/cloud-platform-environments/tree/main) and the repo for the service accessing the value.

### 1. In the relevant namespace in [cloud-platform-environments](https://github.com/ministryofjustice/cloud-platform-environments/tree/main):

1. Create a new data source as in [this example](https://github.com/ministryofjustice/cloud-platform-environments/blob/b62ab2b0c750af416963e029b87a330cb842e83f/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod/resources/ssm.tf#L16). This data source retrieves the SSM property from AWS.

2. [In kubernetes-secrets.tf](https://github.com/ministryofjustice/cloud-platform-environments/blob/b62ab2b0c750af416963e029b87a330cb842e83f/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-electronic-monitoring-datastore-preprod/resources/kubernetes-secrets.tf#L1) either create a new secret containing the data source, or add the data source to an existing secret.

3. Create a PR. Share it with the Cloud Platform team in Slackfor review.  
Once the changes have been deployed the kubernetes secret will exist in the namespace.

### 2. In the service repo, access the kubernetes secret

In the helm values of your service repo, [add namespace secret as in this example](https://github.com/ministryofjustice/hmpps-electronic-monitoring-datastore-api/blob/c8e54c9a01402a937169c8404de8b98fe0ac4e03/helm_deploy/hmpps-electronic-monitoring-datastore-api/values.yaml#L29). The value of the secret will be assigned to the specified variable. The variable can then be used in code as required.

