FROM amazoncorretto:17
WORKDIR /
COPY build/libs/${JAR_NAME} /app.jar
RUN ls -al
#ENTRYPOINT "java -jar ${JAVA_OPTIONS} app.jar"
