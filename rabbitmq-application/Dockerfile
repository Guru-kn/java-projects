FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/rabbitmqapp-1.0.jar rabbitmqapp-1.0.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /rabbitmqapp-1.0.jar" ]