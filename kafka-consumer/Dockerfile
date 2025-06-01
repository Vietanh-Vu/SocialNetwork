FROM jelastic/maven:3.9.8-zulujdk-21.0.4-almalinux-9 as build

WORKDIR /build

COPY .mvn/ ./.mvn
COPY mvnw pom.xml  ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY . .
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

FROM alpine/java:21-jdk
WORKDIR /app
COPY --from=build /build/target/*.jar run.jar
ENTRYPOINT ["java", "-jar", "/app/run.jar"]