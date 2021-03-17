#stage 1
FROM gradle:jdk11 AS appbuild
WORKDIR "/home/gradle/"

VOLUME /root/.gradle
VOLUME /home/gradle/.gradle

COPY --chown=gradle:gradle "./build.gradle" "/home/gradle/"
COPY --chown=gradle:gradle "./gradle.properties" "/home/gradle/"
COPY --chown=gradle:gradle "./gradlew" "/home/gradle/"
COPY --chown=gradle:gradle "./gradle" "/home/gradle/gradle"
RUN ["./gradlew", "--no-daemon", "--info"]
COPY --chown=gradle:gradle "./src" "/home/gradle/src"

RUN ["./gradlew", "bootJar", "-x", "test", "--no-daemon", "--info"]

RUN ["ls", "/home/gradle/build/libs/"]

#stage 2
FROM node as uiBuild

WORKDIR /app
COPY ui/package*.json /app/
RUN npm install
COPY ui /app
ARG configuration=uat
RUN npm run build:prod -- --outputPath=./dist/out --configuration $configuration


# Stage 3, use the compiled app, ready for production with Nginx
FROM nginx:alpine
RUN apk update
RUN apk add openjdk11

COPY --from=uiBuild /app/dist/out/ /usr/share/nginx/html
COPY --from=uiBuild /app/dist/out/ /usr/share/nginx/html
COPY --from=appBuild /home/gradle/build/libs/*.jar /app.jar

COPY --from=uiBuild /app/dist /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY ui/uat.nginx.conf /etc/nginx/conf.d

COPY deploy/uat/run.sh /

ENTRYPOINT ["sh", "/run.sh"]
