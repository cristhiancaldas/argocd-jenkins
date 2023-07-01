pipeline {

  agent any

  tools{
        maven 'maven3'
    }

  environment {
     docker_repo = "crist"
     ImageName = 'jenkins-docker-hub'
     AppName = "message"
     ImageTag="v1"
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

    }
}

/*
def dockerCleanupCall(String project, String hubUser) {
    sh "docker rmi ${hubUser}/${project}:${ImageTag}"
    sh "docker rmi ${hubUser}/${project}:latest"
}*/