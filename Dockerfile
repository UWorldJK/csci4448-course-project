# ---- Build stage ----
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

# Copy Gradle wrapper and build files first so dependencies are cached
COPY gradlew settings.gradle* build.gradle* gradle.properties* ./
COPY gradle ./gradle
COPY app/build.gradle* ./app/
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null 2>&1 || true

# Copy the rest of the source and build the Spring Boot jar
COPY . .
RUN ./gradlew :app:bootJar --no-daemon -x test

# ---- Runtime stage ----
FROM eclipse-temurin:25-jre
WORKDIR /app

# curl for the healthcheck
RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/app/build/libs/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]