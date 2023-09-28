FROM amazoncorretto:17
ENV JAR_NAME="mapstock-api-0.0.1-SNAPSHOT.jar"
COPY build/libs/${JAR_NAME} app.jar
EXPOSE 8080:8080
ENTRYPOINT ["java", "-jar", "app.jar"]
