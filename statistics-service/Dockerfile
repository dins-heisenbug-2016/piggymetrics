FROM java:8-jre
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./build/libs/*.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/statistics-service*.jar"]

EXPOSE 7000