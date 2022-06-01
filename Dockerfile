FROM maven:3.6.3-openjdk-14-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM adoptopenjdk/openjdk14
COPY --from=build /home/app/target/iemdb-0.0.1-SNAPSHOT.jar /usr/local/lib/iemdb.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/iemdb.jar"]