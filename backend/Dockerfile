# Build context = repository root
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /build

COPY backend/id-generator/pom.xml .

RUN mvn -q -B dependency:go-offline

COPY backend/id-generator/src ./src

# Package system configuration into the application classpath.
COPY data ./src/main/resources/data

RUN mvn -q -B -DskipTests package


# Runtime image
FROM eclipse-temurin:25-jre

WORKDIR /app

RUN useradd --system --no-create-home appuser

COPY --from=build \
    /build/target/id-generator-0.0.1-SNAPSHOT.jar \
    app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]