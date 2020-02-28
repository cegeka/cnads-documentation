package com.cegeka.cnads.cdk;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.SecretValue;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.eks.FargateCluster;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;

public class ClusterStack extends Stack {

    private final Vpc vpc;
    private final Cluster ecsCluster;
    private final DatabaseInstance databaseInstance;

    public ClusterStack(Construct scope, String id) {
        super(scope, id);

        vpc = Vpc.Builder.create(this, "clusterVPC")
                .maxAzs(2)
                .natGateways(1)
                .build();

        ecsCluster = Cluster.Builder.create(this, "ecsCluster")
                .vpc(vpc)
                .build();

        databaseInstance = DatabaseInstance.Builder.create(this, "databaseInstance")
                .vpc(vpc)
                .engine(DatabaseInstanceEngine.POSTGRES)
                .instanceClass(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .masterUsername("test")
                .masterUserPassword(SecretValue.plainText("testtest"))
                .databaseName("hello")

                //do not use in production
                .deletionProtection(false)
                .deleteAutomatedBackups(true)
                .backupRetention(Duration.seconds(0)) //disables backups

                .build();

        databaseInstance.getConnections().allowDefaultPortFromAnyIpv4();
    }

    public Vpc getVPC() {
        return vpc;
    }

    public Cluster getEcsCluster() {
        return ecsCluster;
    }

    public DatabaseInstance getDatabaseInstance() {
        return databaseInstance;
    }
}
