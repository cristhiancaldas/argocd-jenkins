pipeline {

  agent any

  tools{
        maven 'maven3'
    }

  environment {
     docker_repo = "crist"
     ImageName = 'jenkins-docker-hub'
     AppName = "argocd-jenkins"
     DOCKER_USER = "crist"
     IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
     IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
  }

    stages {

     stage("Cleanup Workspace"){
           steps {
                cleanWs()
           }
     }

     stage("Checkout from SCM"){
           steps {
               git branch: 'main', credentialsId: 'github-token', url: 'https://github.com/cristhiancaldas/argocd-jenkins.git'
           }
     }

     stage('Build-Maven'){
           steps {
             dir("${WORKSPACE}") {
               sh "mvn clean package"
          }
        }
     }

     stage("Test Application"){
           steps {
               sh "mvn test"
           }
     }

    stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv(credentialsId: 'sonar-token') {
                        sh "mvn sonar:sonar"
                    }
                }
            }
    }

    stage("Docker Build") {
             steps {
                 dir("${WORKSPACE}") {
                 script {
                  docker.withRegistry('','docker-hub') {
                     docker_image = docker.build "${IMAGE_NAME}"
                  }
                  }
                 }
             }
    }

    stage("Docker Push") {
             steps {
                 dir("${WORKSPACE}") {
                   script {
                     docker.withRegistry('','docker-hub') {
                             docker_image.push("${IMAGE_TAG}")
                             docker_image.push('latest')
                     }
                     }
                 }
             }
    }

    stage ('Cleanup Artifacts') {
            steps {
                script {
                    sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker rmi ${IMAGE_NAME}:latest"
                }
            }
        }
    }
}

/*
def dockerCleanupCall(String project, String hubUser) {
    sh "docker rmi ${hubUser}/${project}:${ImageTag}"
    sh "docker rmi ${hubUser}/${project}:latest"
}*/