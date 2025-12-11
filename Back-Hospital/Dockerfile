#SERVIR LA IMAGEN DEL BACK CON MAVEN
FROM maven:3.9.9-eclipse-temurin-21

#ESTABLECER UN DIRECTORIO DE TRABAJO
WORKDIR /app

#copia tood los archivos del proyecto en el directorio de trabajo
COPY . .

#ejecutar 2 comandos
#1.- limpiar y compilar el .JAR del back
#2.- copiar el .JAR resultante al directorio de trabajo
RUN mvn clean package -DskipTests && cp ./target/*.jar app.jar

# Exponsr la imagen en puerto
EXPOSE 8080
#ejecutar el comando para servir el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
