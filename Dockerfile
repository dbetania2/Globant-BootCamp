# Dockerfile
FROM openjdk:17-jdk-slim

# Copiar el archivo JAR del proyecto al contenedor
ARG JAR_FILE=target/ShopiShopping-0.0.1.jar
COPY ${JAR_FILE} ShopiShopping.jar
# Exponer el puerto en el que se ejecuta la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "ShopiShopping.jar"]

