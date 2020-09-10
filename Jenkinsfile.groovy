pipeline {
    agent any
    stages {
        stage('SCM') {
            git 'https://github.com/foo/bar.git'
        }
        stage('SonarQube analysis') {
            def scannerHome = tool 'SonarScanner 4.0';
            withSonarQubeEnv('chat-app-sonar') { // If you have configured more than one global server connection, you can specify its name
                bat "C:\\Users\\Administrator\\Downloads\\sonar-scanner-cli-4.4.0.2170-windows\\sonar-scanner-4.4.0.2170-windows\\bin"
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
