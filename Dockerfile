FROM amazoncorretto:21-alpine-jdk AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .

RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:21

WORKDIR /app

COPY --from=build /app/target/aprenda-tech-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "aprenda-tech-0.0.1-SNAPSHOT.jar"]