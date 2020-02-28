package com.cegeka.cnads.cdk.application;

import com.cegeka.cnads.cdk.ClusterStack;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.ecr.IRepository;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;

import java.util.HashMap;

public class ApplicationStack extends Stack {

    private final ApplicationLoadBalancedFargateService fargateService;
    private final IRepository imageRepository;

    public ApplicationStack(Construct scope, String id, ClusterStack clusterStack) {
        super(scope, id);

        this.addDependency(clusterStack);

        imageRepository = Repository.fromRepositoryName(
                this,
                "cnadsDockerRepo",
                "cnads"
        );

        fargateService = ApplicationLoadBalancedFargateService.Builder.create(this, "SpringBootPipelineApp")
                .cluster(clusterStack.getEcsCluster())
                .desiredCount(2)
                .cpu(256)
                .memoryLimitMiB(1024)
                .healthCheckGracePeriod(Duration.minutes(5))
                .serviceName("simple-project-service")
                .taskImageOptions(
                        ApplicationLoadBalancedTaskImageOptions.builder()
                                .containerName("simple-project")
                                .image(ContainerImage.fromEcrRepository(imageRepository, "f631bb43"))
                                .environment(getSpringBootDockerEnvironment(clusterStack.getDatabaseInstance().getInstanceEndpoint().getSocketAddress()))
                                .containerPort(8080)
                                .build())
                .build();

        fargateService.getTargetGroup().configureHealthCheck(HealthCheck.builder()
                .path("/actuator/health")
                .build());
    }

    private HashMap<String, String> getSpringBootDockerEnvironment(String dbUrl) {
        HashMap<String, String> environment = new HashMap<>();
        environment.put("SPRING_PROFILES_ACTIVE", "postgres");
        // dbUrl = e.g. cssl4vrkypq2oq.cv763nfkiqx6.eu-west-1.rds.amazonaws.com:5432
        environment.put("SPRING_DATASOURCE_URL", "jdbc:postgresql://" + dbUrl + "/hello");
        environment.put("SPRING_DATASOURCE_PASSWORD", "testtest");
        environment.put("SPRING_JPA_HIBERNATE_DDL_AUTO", "update");
        return environment;
    }

    public ApplicationLoadBalancedFargateService getFargateService() {
        return fargateService;
    }

    public IRepository getImageRepository() {
        return imageRepository;
    }
}
