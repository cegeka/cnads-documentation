# Cloud architecture

## Learning resources
- https://github.com/aws-samples/aws-modern-application-workshop/tree/java

## Findings
Lot's of magic _works out of the box_ examples without much explanation e.g. use `AmazonDynamoDBClientBuilder.defaultClient()` and you can automagically use a database without even configuring how to connect to it. But what about users? roles? connection details? What if you want to use something different?
