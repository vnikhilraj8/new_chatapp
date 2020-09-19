node{
    stage('SCM Checkout'){
        git url: 'https://github.com/Sanjana-cell/Django-Chat-Application.git'
    }
    stage('Build Docker Image'){
        sh "docker build -t sanjanakr/my-app:${BUILD_ID}.0.0 ."
    }

    stage('Push Docker Image'){
        withCredentials([string(credentialsId: 'docker-pwd', variable: 'dockerHubPwd')]) {
            sh "docker login -u sanjanakr -p ${dockerHubPwd}"
        }
        sh "docker push sanjanakr/my-app:${BUILD_ID}.0.0"
        sh "docker tag sanjanakr/my-app:${BUILD_ID}.0.0  sanjanakr/my-app:latest"
        sh "docker push sanjanakr/my-app:latest"
    }

    stage('Run Container on Dev Server'){
        withCredentials([string(credentialsId: 'docker-pwd', variable: 'dockerHubPwd')]) {
            sh "docker login -u sanjanakr -p ${dockerHubPwd}"
        }
        sh "sudo ssh -i /sanjana.pem -o StrictHostKeyChecking=no ubuntu@190.178.0.11 /./docker.sh"
    }
}
