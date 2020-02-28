# cnads-documentation
Container native application developments documentation

This repository tries to document a few of the approaches we took during the Cloud Native Application Development Sandbox.
During the sandbox we focused heavily on the AWS offering (which might not be the definition of cloud native the CNCF has).

## cdk

A repository that uses the AWS CDK to set up an application pipeline. It combines the following elements:

* An ECS/Fargate cluster
* An RDS postgres DB instance
* An ECR image repository
* A CodePipeline that uses
    * A CodeCommit repo
    * A CodeBuild step
    * An ECS Code Deploy action
* to deploy an ECS Service that is
    * LoadBalanced
    * Health monitored

## eks