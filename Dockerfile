FROM openjdk:17
COPY target/notepad-0.0.1-SNAPSHOT.jar notepad-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","notepad-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080