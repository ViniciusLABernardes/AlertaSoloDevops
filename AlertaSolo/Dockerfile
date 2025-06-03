
FROM eclipse-temurin:21-jdk-alpine

RUN adduser -D -h /home/alertasolo alertauser

WORKDIR /app


ENV APP_NAME="AlertaSolo"
ENV AUTORES="Vinícius, Edvan e Rafael"

COPY target/*.jar app.jar

EXPOSE 8080

USER alertauser

ENTRYPOINT ["sh", "-c", "echo Aplicação: $APP_NAME - Autor: $AUTORES && exec java -jar app.jar"]