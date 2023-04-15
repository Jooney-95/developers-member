FROM openjdk:17-jdk
ADD /build/libs/developers-member-0.0.1-SNAPSHOT.jar springbootApp.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "springbootApp.jar"]
