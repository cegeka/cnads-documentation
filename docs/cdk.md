# CDK
Our repository: https://eu-west-1.console.aws.amazon.com/codesuite/codecommit/repositories/cnads-cdk-workshop/browse?region=eu-west-1

## Learning resources
- https://cdkworkshop.com/
    - create a queue - AWS SQS (https://eu-west-1.console.aws.amazon.com/sqs/home?region=eu-west-1)
    - use AWS Lambdas (https://eu-west-1.console.aws.amazon.com/lambda/home?region=eu-west-1)
    - use a database - DynamoDB (https://eu-west-1.console.aws.amazon.com/dynamodb/home?region=eu-west-1)
    - expose resources - API Gateway
    - use CloudFormation console to view stacks (https://console.aws.amazon.com/cloudformation/home)
        - see also the the `Designer` on the `Template` tab, visualises your stack
    - use CloudWatch to view logs (https://eu-west-1.console.aws.amazon.com/cloudwatch/home?region=eu-west-1)

## Commands
- `aws configure`   configure aws credentials on your local environment
- `cdk init sample-app --language java` initialize the sample app in the current directory
- `mvn package`     compile and run tests
- `cdk ls`          list all stacks in the app
- `cdk synth`       emits the synthesized CloudFormation template (not necessary, for info)
- `cdk bootstrap`   execute before you can deploy an AWS CDK app
- `cdk deploy`      deploy this stack to your default AWS account/region
- `cdk diff`        compare deployed stack with current state
- `cdk destroy`     destroy currently configured stacks

## Concepts
- Stacks - _A root construct which represents a single CloudFormation stack_
- Constructs - _Represents the building block of the construct graph_

## Findings
- when destroying a stack, cdk will destroy everything linked to your current stack
    - meaning, if you deploy stack `A`, rename your stack to `B` and then execute `cdk destroy`, nothing will be removed as he cannot match `B` to an existing stack
- also realise that `cdk destroy` does not delete everything, your CDKToolkit (created by `cdk bootstrap`) remains, your database remains, the default for stateful stuff seems to be 'retain'
- watch out when using other stack libraries, they can set up anything they want without you realizing if you are not careful
- status/popularity/community:
    - _The AWS CDK is a work in progress. It is currently in Developer Preview._
- CloudWatch is slow, meaning: you send a call and only +_8 seconds later you see a log entry appear. When you have a lot of logging this will be annoying.
- CDK supports several programming languages, even Java. As a Java developer this makes it possible to write tests for your code the way you are used to.
- CDK isn't very 'opinionated', meaning: it does not hold your hand and tell you how to bring structure to everything, which best-practices there are etc. There is a user-made readme that tries to help in this area but it's quite minimalistic: https://github.com/kevinslin/open-cdk#license
- When deploying a stack where we use a docker image and the docker image is not found it seems to try and retry for a long time without any feedback. We ended up killing the creation, which meant we had to destroy the stack to create it again from scratch. We even had to guess the problem because we didn't have any logs?
- Cost estimates.. very difficult (when uploading a CloudFormation file we have an 'estimated cost' link but it doesn't work)
- Eventual non consistent UI weirdness
- Not everything is configurable as you would expect with the builders. Sometimes you have to create an object with a builder and then afterwards call setters on the resulting object. (eg applicationLoadBalancedFargateService.getTargetGroup().setHealthCheck(...) #fml)
- Googling for solutions to your problems / for examples is a pain. It feels like we are the first people to try some specific things, this means we sometimes have to do a lot of trial and error to get something to work. (feels like playing Jenga in the dark)
- In general: The current 'CDK community' is very small, measured by the amount of results you get when googling about it.
- In general: it feels like the CDK API could be a lot better, seems like the creators haven't used it much themselves or haven't recieved much feedback on the usage. It's not like a framework that has been out for at least 3 years with a large community using it. 

## Further playing
https://github.com/enghwa/springboot-fargate-cdk
https://github.com/aws-samples/aws-cdk-examples#Java
