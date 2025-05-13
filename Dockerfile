FROM amazoncorretto:17
LABEL authors="prink"

WORKDIR /app
COPY target/webrisetest.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]