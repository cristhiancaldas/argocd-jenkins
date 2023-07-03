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
     IMAGE_TAG = "${BUILD_NUMBER}"
     REPOSITORY_GITHUB = "https://github.com/cristhiancaldas/argocd-jenkins.git"
      GIT_CREDS = credentials('github-token')
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
                        sh "mvn sonar:sonar\
                        -Dsonar.projectName=argocdProject"
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

    stage('Update Kubernetes deployment file'){
        steps{
          script {
             sh """
             cat deployment.yml
             sed -i 's/${APP_NAME}.*/${APP_NAME}:${IMAGE_TAG}/g' deployment.yml
             cat deployment.yml
             """
          }
        }
    }

    stage('Push changed deployment file to Git'){
        steps{
           script {
              sh """
                 git config --global user.name "ccargocd"
                 git config --global user.email "c.caldas.m@gmail.com"
                 git add deployment.yml
                 git commit -m "update deployment file"
              """
              sh "git push https://$GIT_CREDS_USR:$GIT_CREDS_PSW@github.com/cristhiancaldas/argocd-jenkins.git"
             // withCredentials([gitUsernamePassword(credentialsId: 'github-token', gitToolName: 'Default')]) {
              //       sh "git push https://$GIT_CREDS_USR:$GIT_CREDS_PSW@github.com/$GIT_CREDS_USR/argocd-jenkins.git"
                    //sh "git push  https://github.com/cristhiancaldas/argocd-jenkins.git  main"
              //}
           }
        }
    }

    }
}