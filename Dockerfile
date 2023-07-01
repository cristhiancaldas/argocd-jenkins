FROM openjdk:11-jdk-slim

LABEL AUHTOR="Cristhian Caldas Mendoza"

ADD target/*.jar argocd.jar

ENTRYPOINT ["java","-jar","/argocd.jar"]