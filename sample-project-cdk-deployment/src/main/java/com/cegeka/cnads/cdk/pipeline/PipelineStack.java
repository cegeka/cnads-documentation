package com.cegeka.cnads.cdk.pipeline;

import com.cegeka.cnads.cdk.application.ApplicationStack;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.codebuild.*;
import software.amazon.awscdk.services.codecommit.IRepository;
import software.amazon.awscdk.services.codecommit.Repository;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.Pipeline;
import software.amazon.awscdk.services.codepipeline.StageProps;
import software.amazon.awscdk.services.codepipeline.actions.CodeBuildAction;
import software.amazon.awscdk.services.codepipeline.actions.CodeCommitSourceAction;
import software.amazon.awscdk.services.codepipeline.actions.EcsDeployAction;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;

import java.util.HashMap;

import static java.util.Arrays.asList;

public class PipelineStack extends Stack {

    public PipelineStack(Construct scope, String id, ApplicationStack applicationStack) {
        super(scope, id);

        this.addDependency(applicationStack);

        IRepository codeCommitRepo = Repository.fromRepositoryName(this, "mainrepo", "cnads-simple-project");

        IBucket bucket = Bucket.fromBucketName(this, "artifactBucket", "cnadspipelinestack-pipelineartifactsbucket7d04d0d-ari55r8do7sa");
        Artifact sourceArtifact = new Artifact();
        Artifact buildOutput = new Artifact("buildOutput");

        String projectName = "cdk-simple-project";
        IManagedPolicy codeBuildPolicy = ManagedPolicy.Builder.create(this, "buildPolicy")
                .managedPolicyName("cdk-simple-project-build-policy")
                .statements(asList(
                        PolicyStatement.Builder.create()
                                .effect(Effect.ALLOW)
                                .resources(asList(
                                        "arn:aws:logs:eu-west-1:349294767929:log-group:/aws/codebuild/" + projectName,
                                        "arn:aws:logs:eu-west-1:349294767929:log-group:/aws/codebuild/" + projectName + ":*"
                                )).actions(asList(
                                "logs:CreateLogGroup",
                                "logs:CreateLogStream",
                                "logs:PutLogEvents"
                        )).build(),
                        PolicyStatement.Builder.create()
                                .effect(Effect.ALLOW)
                                .resources(asList(
                                        "arn:aws:s3:::codepipeline-eu-west-1-*"
                                )).actions(asList(
                                "s3:PutObject",
                                "s3:GetObject",
                                "s3:GetObjectVersion",
                                "s3:GetBucketAcl",
                                "s3:GetBucketLocation"
                        )).build(),
                        PolicyStatement.Builder.create()
                                .effect(Effect.ALLOW)
                                .resources(asList(
                                        codeCommitRepo.getRepositoryArn()
                                )).actions(asList(
                                "codecommit:GitPull"
                        )).build(),
                        PolicyStatement.Builder.create()
                                .effect(Effect.ALLOW)
                                .resources(asList(
                                        "arn:aws:codebuild:eu-west-1:349294767929:report-group/" + projectName + "-*"
                                )).actions(asList(
                                "codebuild:CreateReportGroup",
                                "codebuild:CreateReport",
                                "codebuild:UpdateReport",
                                "codebuild:BatchPutTestCases"
                        )).build()
                ))
                .build();
        IPrincipal assumedBy = new CompositePrincipal(new ServicePrincipal("codebuild.amazonaws.com"), new AccountPrincipal("349294767929"));
        Role role = Role.Builder.create(this, "buildRole")
                .roleName("cdk-simple-project-role")
                .managedPolicies(asList(
                        ManagedPolicy.fromManagedPolicyArn(this, "ecrPolicy", "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryPowerUser"),
                        codeBuildPolicy
                ))
                .assumedBy(assumedBy)
                .build();
        IProject buildProject = PipelineProject.Builder.create(this, "build")
                .projectName(projectName)
                .role(role)
                .environment(
                        BuildEnvironment.builder()
                                .computeType(ComputeType.SMALL)
                                .buildImage(LinuxBuildImage.STANDARD_3_0)
                                .privileged(true)
                                .environmentVariables(new HashMap<String, BuildEnvironmentVariable>() {{
                                    put("REPOSITORY_URI", BuildEnvironmentVariable.builder()
                                            .type(BuildEnvironmentVariableType.PLAINTEXT)
                                            .value(applicationStack.getImageRepository().getRepositoryUri())
                                            .build());
                                }})
                                .build()
                )
                .build();


        Pipeline pipeline = Pipeline.Builder.create(this, "pipeline")
                .pipelineName("simple-project-pipeline")
                .artifactBucket(bucket)
                .stages(asList(
                        StageProps.builder()
                                .stageName("Source")
                                .actions(asList(
                                        CodeCommitSourceAction.Builder.create()
                                                .actionName("Source")
                                                .repository(codeCommitRepo)
                                                .output(sourceArtifact)
                                                .build()
                                ))
                                .build(),
                        StageProps.builder()
                                .stageName("Build")
                                .actions(asList(
                                        CodeBuildAction.Builder.create()
                                                .actionName("Build")
                                                .input(sourceArtifact)
                                                .project(buildProject)
                                                .outputs(asList(buildOutput))
                                                .build()
                                ))
                                .build(),
                        StageProps.builder()
                                .stageName("Deploy")
                                .actions(asList(
                                        EcsDeployAction.Builder.create()
                                                .actionName("Deploy")
                                                .input(buildOutput)
                                                .service(applicationStack.getFargateService().getService())
                                                .build()
                                ))
                                .build()
                        ))
                .build();
    }
}
