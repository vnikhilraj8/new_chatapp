pipeline {
    agent any
    stages {
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('chat-app-sonar') { // If you have configured more than one global server connection, you can specify its name
                    sh "/sonar/sonar-scanner-4.4.0.2170-linux/bin/sonar-scanner"
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
