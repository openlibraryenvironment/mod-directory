FROM folioci/alpine-jre-openjdk17:latest
MAINTAINER Ian.Ibbotson@k-int.com
VOLUME /tmp
ENV VERTICLE_FILE mod-directory.war
ENV VERTICLE_HOME /
COPY service/build/libs/mod-directory-*.*.*.jar mod-directory.war
EXPOSE 8080/tcp
