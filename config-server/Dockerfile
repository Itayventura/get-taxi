FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=config-server-0.0.1-SNAPSHOT.jar
COPY target/${JAR_FILE} config-server.jar
ENTRYPOINT ["java","-jar","/config-server.jar"]
