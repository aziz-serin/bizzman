FROM openjdk:11
EXPOSE 8080
ARG WAR_FILE=target/bizzman.war
ADD ${WAR_FILE} bizzman.war
ENTRYPOINT ["java","-jar","/bizzman.war"]
