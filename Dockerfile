FROM amazoncorretto:17
LABEL authors="prink"

WORKDIR /app
COPY build/libs/WebRiseTest.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]