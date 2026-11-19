# syntax=docker/dockerfile:1
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app
COPY --from=builder /app/target/*.jar app.jar
RUN chown -R app:app /app

USER app
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
  CMD curl -fsS http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
