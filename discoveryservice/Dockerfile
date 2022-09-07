FROM openjdk:17.0.2-slim
VOLUME /tmp
COPY build/libs/discoveryservice-1.0.jar DiscoveryService.jar
ENTRYPOINT ["java", "-jar", "DiscoveryService.jar"]