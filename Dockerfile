FROM adoptopenjdk:11-jdk-openj9-bionic
MAINTAINER Ian.Ibbotson@k-int.com
VOLUME /tmp
# ADD service-0.1.war mockncipserver.war
COPY service/build/libs/mod-directory-*.*.*-SNAPSHOT.jar mod-directory.war

# Handy info here: https://dev.to/hugodias/wait-for-mongodb-to-start-on-docker-3h8b
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.6.0/wait /wait
RUN chmod +x /wait

EXPOSE 8080/tcp

# See https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config
#     https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-relaxed-binding-from-environment-variables
# THis image understands the following environment variables
# DATASOURCES.MTCP.URL=jdbc://host:5432/dbname
CMD /wait && java -Djava.security.egd=file:/dev/./urandom -Xshareclasses -Xscmx50M -Xtune:virtualized -jar /mod-directory.war

