# EKS-Fargate
## [Install and configure eksctl](https://docs.aws.amazon.com/eks/latest/userguide/getting-started-eksctl.html)
## [Install and configure fargate](https://docs.aws.amazon.com/eks/latest/userguide/fargate-getting-started.html)
## [Install and configure ALB ingress](https://docs.aws.amazon.com/eks/latest/userguide/alb-ingress.html)

## Create fargate profile
```bash
eksctl create fargateprofile --cluster cnads-fargate --region eu-west-1 --name cnads-fargate-profile --namespace cnads-petclinic
```
## Manual deploy K8S manifests
```bash
kubectl -n cnads-petclinic apply -f ../cnads-petclinic/1_ECS_Java_Spring_PetClinic/src/main/kubernetes
```
