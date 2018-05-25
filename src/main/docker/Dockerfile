FROM openjdk:8-jdk-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/simplecrudapp
ADD @project.build.finalName@.jar /usr/local/simplecrudapp/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
