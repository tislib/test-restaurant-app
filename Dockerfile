FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENV PROFILE="prod"

RUN mkdir -p /home/resource/config/
RUN ln -s /tmp/plugins/config /home/resource/config/merchants

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "/app.jar"]
