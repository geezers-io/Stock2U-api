FROM amazoncorretto:17
WORKDIR /
RUN ls -al
COPY build/libs/${JAR_NAME} /app.jar
ENTRYPOINT "java -jar ${JAVA_OPTIONS} app.jar"
