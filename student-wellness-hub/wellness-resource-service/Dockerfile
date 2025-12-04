FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle* settings.gradle* ./
COPY src ./src
RUN chmod +x gradlew && ./gradlew clean build -x test


FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8091
ENV SPRING_PROFILES_ACTIVE=docker
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
CMD wget -qO- http://localhost:8091/actuator/health | grep UP || exit 1
ENTRYPOINT ["java","-jar","/app/app.jar"]