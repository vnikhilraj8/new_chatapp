pipeline {
    agent any
    stages {
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('sonar-scanner') { // If you have configured more than one global server connection, you can specify its name
                    sh "/opt/sonar-scanner-4.4.0.2170-linux/bin/sonar-scanner"
                }
            }
        }
        stage("Quality Gate") {
            steps {
                timeout(time: 3, unit: 'MINUTES') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don'
                    step {
                        def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                    //waitForQualityGate abortPipeline: true
                }
            }
        }
        stage("deploy") {
            steps{
               step([$class: 'AWSCodeDeployPublisher',
                     applicationName: 'chat-Application', awsAccessKey: '',
                     awsSecretKey: '',
                     credentials: 'awsAccessKey', deploymentConfig: 'CodeDeployDefault.OneAtATime',
                     deploymentGroupAppspec: false, deploymentGroupName: 'demo', deploymentMethod: 'deploy',
                     excludes: '', iamRoleArn: '', includes: '**', proxyHost: '', proxyPort: 0, region: 'us-east-2',
                     s3bucket: 'demo24123', s3prefix: '', subdirectory: '', versionFileName: '', waitForCompletion: false])
        }}

    }
}
