# Install Kubernetes with fargate on AWS
A EKS + Fargate enable terraform file is available in this folder.
Get the terraform modules
```console
terraform init
```
Apply the terraform code
```console
terraform apply -auto-approve  15.76s user 8.82s system 2% cpu 18:09.62 total
```
The terraform eks fargate profile is based on: https://github.com/cloudposse/terraform-aws-eks-fargate-profile

# Install kubernetes console
```console
kubectl --kubeconfig=.kube/config apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta8/aio/deploy/recommended.yaml
```

## Map IAM users to provide access to kubernetes cluster
https://docs.aws.amazon.com/eks/latest/userguide/add-user-role.html
```console
kubectl --kubeconfig=.kube/config edit -n kube-system configmap/aws-auth
```

## Get named user token for kubernetes dashboard
```console
aws eks get-token --cluster-name cnads-test-eks-fargate-cluster
```
## kubernetes dashboard token ttl
```console
kubectl --kubeconfig=.kube/config -n kubernetes-dashboard edit deployment kubernetes-dashboard
```
URL: https://blinkeye.github.io/post/public/2019-05-30-kubernetes-dashboard/

## Access the kubernetes dashboard
```console
kubectl --kubeconfig=.kube/config proxy
```
URL: http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

## Get IAM user kubeconfig
```console
aws eks update-kubeconfig --name cnads-test-eks-fargate-cluster
```

# Documentation
- EKS and authentication
  - https://www.terraform.io/docs/providers/aws/index.html#shared-credentials-file
  - https://www.terraform.io/docs/providers/aws/d/eks_cluster.html
- kubernetes dashboard
  - https://docs.aws.amazon.com/eks/latest/userguide/dashboard-tutorial.html
- fargate / eks availability
  - https://aws.amazon.com/about-aws/global-infrastructure/regional-product-services/
  - https://aws.amazon.com/blogs/aws/amazon-eks-on-aws-fargate-now-generally-available/
- https://www.nclouds.com/blog/kubernetes-aws-terraform-kops/
  - s3 persistency
- https://github.com/cloudposse/terraform-aws-eks-fargate-profile
- https://docs.fluxcd.io/en/latest/tutorials/get-started.html
