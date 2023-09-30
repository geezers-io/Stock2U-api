FROM amazoncorretto:17
COPY build/libs/${JAR_NAME} /app.jar
ENTRYPOINT "java -jar ${JAVA_OPTIONS} app.jar"
