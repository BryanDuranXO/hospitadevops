# SERVIR LA IMAGEN DEL BACK CON MAVEN
FROM maven:3.9.9-eclipse-temurin-21

# ESTABLECER UN DIRECTORIO DE TRABAJO
WORKDIR /app

# Copiar todos los archivos del proyecto al contenedor
COPY . .

# Copiar las credenciales de Firebase A UNA RUTA FIJA DENTRO DEL CONTENEDOR
# (IMPORTANTE: tu carpeta existe aquí: src/main/resources/firebase)
COPY src/main/resources/firebase /app/firebase

# Ejecutar build y copiar jar
RUN mvn clean package -DskipTests && cp ./target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
