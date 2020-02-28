# cnads-documentation
Container native application developments documentation

This repository tries to document a few of the approaches we took during the Cloud Native Application Development Sandbox.
During the sandbox we focused heavily on the AWS offering (which might not be the definition of cloud native the CNCF has).

## docs

Documentation as recorded during the sandbox

## eks

Terraform manifests for deploying an EKS cluster with ec2 and fargate

## qk-greeter

A simple project written in Quarkus to test native compilation and how it works with AWS lambda / AWS API gateway.

## sample-project

A repository that contains a very simple spring boot application that offers two endpoints to save and retrieve information from a database.

It contains a `buildspec.yml` file describing how to build a container from it and emits an `imagedefinitions.yml` file describing how it should be deployed

## sample-project-cd-deployment

For more information about using cdk, see also docs/cdk.md

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