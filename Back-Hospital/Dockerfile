FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

# Copiar solo lo necesario
COPY pom.xml .
COPY src ./src

# Compilar el JAR
RUN mvn clean package -DskipTests && cp target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
