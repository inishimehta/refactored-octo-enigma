FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY build/libs/event-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]

#endpoints to test
#POST   http://localhost:8082/api/events
#GET    http://localhost:8082/api/events
#GET    http://localhost:8082/api/events/1
#PUT    http://localhost:8082/api/events/1
#DELETE http://localhost:8082/api/events/1
#POST   http://localhost:8082/api/events/1/register?studentId=S123
