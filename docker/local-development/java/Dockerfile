FROM eclipse-temurin:19-jdk-alpine AS builder

WORKDIR /app

USER root

RUN apk update

COPY . .

RUN ./mvnw clean install -DskipTests

RUN cp /root/.m2/repository/com/example/api-admin/0.0.1-SNAPSHOT/api-admin-0.0.1-SNAPSHOT.jar admin.jar
RUN cp /root/.m2/repository/com/example/api-main/0.0.1-SNAPSHOT/api-main-0.0.1-SNAPSHOT.jar main.jar

FROM eclipse-temurin:19-jre-alpine as MAIN-API

WORKDIR /app

COPY --from=builder /app/main.jar main.jar

LABEL 'env'='main-api'

ENTRYPOINT ["java", "-jar", "main.jar"]

FROM eclipse-temurin:19-jre-alpine as ADMIN-API

WORKDIR /app

COPY --from=builder /app/admin.jar admin.jar

LABEL 'env'='admin-api'

ENTRYPOINT ["java", "-jar", "admin.jar"]

