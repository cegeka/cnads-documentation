package com.cegeka.cnads.cdk;

import com.cegeka.cnads.cdk.application.ApplicationStack;
import com.cegeka.cnads.cdk.pipeline.PipelineStack;
import software.amazon.awscdk.core.App;

public class CnadsPipelineApp {

    public static void main(String[] args) {
        App app = new App();

        ClusterStack clusterStack = new ClusterStack(app, "CnadsClusterStack");
        ApplicationStack appStack = new ApplicationStack(app, "CnadsApplicationStack", clusterStack);
        PipelineStack cnadsPipelineStack = new PipelineStack(app, "CnadsPipelineStack", appStack);

        app.synth();
    }
}