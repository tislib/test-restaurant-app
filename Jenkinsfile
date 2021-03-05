node {
    deleteDir()
    APP_NAME="toptal-screening-restaurant-app"
    NAMESPACE="default"

    try {
         stage ('Checkout') {
            checkout scm
         }

         stage ('Build Project') {
            sh './gradlew clean build -x test --no-daemon --info'
         }

         stage ('Unit & Integration Tests') {
            try {
                sh './gradlew check --no-daemon --info'
            } finally {
                junit '**/build/test-results/test/*.xml'
            }
         }

        if (env.BRANCH_NAME == 'master'){
            stage ('Build Image') {
				sh "docker build -t hub.tisserv.net/$APP_NAME:v${env.BUILD_NUMBER} ."
            }

      	    stage ('Push&Clean Image') {
				sh "docker push hub.tisserv.net/$APP_NAME:v${env.BUILD_NUMBER}"
				sh "docker rmi -f hub.tisserv.net/$APP_NAME:v${env.BUILD_NUMBER}"
		}

           stage ('deploy') {
               sh '''
                   cd infra

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
