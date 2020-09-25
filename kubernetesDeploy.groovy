node{
    stage('SCM Checkout'){
        git url: 'https://github.com/Sanjana-cell/Django-Chat-Application.git'
    }
    stage('Build Docker Image'){
        sh "docker build -t chat-app:${BUILD_ID}.0.0 ."
    }

    stage('Push Docker Image'){
        sh "aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 513124100898.dkr.ecr.us-east-2.amazonaws.com"
        sh "docker tag chat-app:${BUILD_ID}.0.0  513124100898.dkr.ecr.us-east-2.amazonaws.com/chat-app:latest"
        sh "docker push 513124100898.dkr.ecr.us-east-2.amazonaws.com/chat-app:latest"
    }

    stage('Deploy the pod'){
        sh "sudo ssh -i /sanjana.pem -o StrictHostKeyChecking=no ubuntu@18.191.122.152 kubectl delete -f /kubernetes/deploy.yml"
        sh "sudo ssh -i /sanjana.pem -o StrictHostKeyChecking=no ubuntu@18.191.122.152 kubectl apply -f /kubernetes/deploy.yml"
    }
}
