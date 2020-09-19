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
        def dockerPull = 'docker pull sanjanakr/my-app:latest'
        def dockerImageid = 'docker image ls | awk \'$2=="<none>"{print $3}\''
        def deleteImage = "docker rmi ${dockerImageid}"
        def containerid = "docker container ls | grep -v -i names | awk '\$13==\"chat-app\"{print \$1}'"
        def restartContainer = "docker restart ${containerid}"
        sh "sudo ssh -i /sanjana.pem -o StrictHostKeyChecking=no ubuntu@190.178.0.11 ${dockerPull}"
        sh "sudo ssh -i /sanjana.pem -o StrictHostKeyChecking=no ubuntu@190.178.0.11 ${deleteImage}"
        sh "sudo ssh -i /sanjana.pem -o StrictHostKeyChecking=no ubuntu@190.178.0.11 ${restartContainer}"
    }
}
