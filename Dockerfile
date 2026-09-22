# Stage 1: Build da aplicação usando Maven instalado no container
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Instala o Maven via apt
RUN apt-get update && apt-get install -y maven

# Copia os arquivos do projeto
COPY . .

# Compila o projeto gerando o .jar
RUN mvn clean package -DskipTests

# Stage 2: Runtime leve para execução
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
