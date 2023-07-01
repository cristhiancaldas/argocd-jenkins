pipeline {

  agent any

  tools{
        maven 'maven3'
    }

  environment {
     docker_repo = "crist"
     ImageName = 'jenkins-docker-hub'
     APP_NAME = "argocd-jenkins"
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
                       sh "docker image build -t ${IMAGE_NAME} ."
                       sh "docker tag ${IMAGE_NAME} ${IMAGE_NAME}:${IMAGE_TAG}"
                       sh "docker tag ${IMAGE_NAME} ${IMAGE_NAME}:latest"
                    }
                 }
            }
    }

    stage("Docker Push") {
             steps {
                 dir("${WORKSPACE}") {
                   script {
                    withCredentials([usernamePassword(
                                credentialsId: "docker-hub",
                                usernameVariable: "USER",
                                passwordVariable: "PASS"
                        )]) {
                            sh "docker login -u '$USER' -p '$PASS'"
                        }
                        sh "docker image push ${IMAGE_NAME}:${IMAGE_TAG}"
                        sh "docker image push ${IMAGE_NAME}:latest"
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

def dockerBuildCall(String project, String hubUser) {
    sh "docker image build -t ${hubUser}/${project} ."
    sh "docker tag ${hubUser}/${project} ${hubUser}/${project}:${ImageTag}"
    sh "docker tag ${hubUser}/${project} ${hubUser}/${project}:latest"
    withCredentials([usernamePassword(
            credentialsId: "docker-hub",
            usernameVariable: "USER",
            passwordVariable: "PASS"
    )]) {
        sh "docker login -u '$USER' -p '$PASS'"
    }
    sh "docker image push ${hubUser}/${project}:${ImageTag}"
    sh "docker image push ${hubUser}/${project}:latest"
}