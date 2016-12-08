FROM java:8-jre
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

COPY ./build/libs/*.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/account-service-1.0-SNAPSHOT.jar"]

EXPOSE 6000