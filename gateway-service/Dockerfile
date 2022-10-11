FROM  openjdk:17.0.2-slim
VOLUME /tmp
COPY build/libs/gateway-service-1.0.jar ApigatewayService.jar
ENTRYPOINT ["java", "-jar", "ApigatewayService.jar"]dock