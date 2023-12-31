FROM amazoncorretto:17
VOLUME /back-end
EXPOSE 8080
ADD ./build/libs/datacenter-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]