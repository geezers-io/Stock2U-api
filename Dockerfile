FROM openjdk:17
WORKDIR /app
RUN ["echo", "$JAR_NAME"]
COPY ./build/libs/${JAR_NAME} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
