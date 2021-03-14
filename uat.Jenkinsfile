pipeline {
    agent any

    stages {
        stage('Build Image') {
            steps {
                sh "docker build . -f uat.Dockerfile -t restaurant-app"
            }
        }
        stage('Run Docker') {
            steps {
                sh "docker kill restaurant-app || true"
                sh "docker rm restaurant-app || true"
                sh "docker run -d --name restaurant-app -p 8978:80 restaurant-app"
            }
        }
    }
}
