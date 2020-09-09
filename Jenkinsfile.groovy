pipeline {
    agent any
    stages {
        stage("deploy") {
            step{
                sh 'rsync -r -e "sudo ssh -i /home/ubuntu/.ssh/sanjana.pem" /var/lib/jenkins/workspace/chat-app ubuntu@190.178.0.6:/home/ubuntu/demo'
                sh 'sudo ssh -i /home/ubuntu/.ssh/sanjana.pem ubuntu@190.178.0.6 sudo cp -r /home/ubuntu/demo/chat-app/* /new_chatapp\n'
                sh 'sudo ssh -i /home/ubuntu/.ssh/sanjana.pem ubuntu@190.178.0.6 sudo systemctl reload nginx'
        }
        }
    }
}
