FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . /app
# Собираем приложение
RUN ./gradlew build -x test
# Copy the first .jar file from build/libs to app.jar
RUN cp build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]