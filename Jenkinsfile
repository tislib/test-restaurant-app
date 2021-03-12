node {
    deleteDir()
    APP_NAME="toptal-screening-restaurant-app"
    NAMESPACE="default"

    try {
         stage ('Checkout') {
            checkout scm
         }

         stage ('API / Build Project') {
            sh './gradlew clean build -x test --no-daemon --info'
         }

         stage ('API / Unit & Integration Tests') {
            try {
                sh './gradlew check --no-daemon --info'
            } finally {
                junit '**/build/test-results/test/*.xml'
            }
         }

         stage ('UI / npm install') {
            sh 'npm install'
         }

         stage ('UI / build') {
            sh 'npm run build:prod'
         }

        if (env.BRANCH_NAME == 'master'){
            stage ('API / Build Image') {
				sh "docker build -t hub.tisserv.net/$APP_NAME-api:v${env.BUILD_NUMBER} ."
            }

      	    stage ('API / Push&Clean Image') {
				sh "docker push hub.tisserv.net/$APP_NAME-api:v${env.BUILD_NUMBER}"
				sh "docker rmi -f hub.tisserv.net/$APP_NAME-api:v${env.BUILD_NUMBER}"
		    }
            stage ('UI / Build Image') {
				sh "docker build -t hub.tisserv.net/$APP_NAME-ui:v${env.BUILD_NUMBER} ui"
            }

      	    stage ('UI / Push&Clean Image') {
				sh "docker push hub.tisserv.net/$APP_NAME-ui:v${env.BUILD_NUMBER}"
				sh "docker rmi -f hub.tisserv.net/$APP_NAME-ui:v${env.BUILD_NUMBER}"
		    }

           stage ('deploy') {
               sh '''
                   cd deploy/kubernetes/terraform

                   terraform init
                   terraform validate .
                   terraform plan -var DOCKER_IMG_TAG=v${BUILD_NUMBER}
                   terraform apply -var DOCKER_IMG_TAG=v${BUILD_NUMBER} -auto-approve
               '''
           }
        }
    } catch (err) {
        throw err
    }
}
