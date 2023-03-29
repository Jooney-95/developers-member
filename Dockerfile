FROM openjdk:17-jdk
ADD /build/libs/developers-member-0.0.1-SNAPSHOT.jar springbootApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "springbootApp.jar"]
