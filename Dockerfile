FROM maven:3.9.9-eclipse-temurin-21 as builder

COPY ./ /app
WORKDIR /app
RUN keytool -import -alias zscaler -cacerts -storepass changeit -file zscaler.pem -noprompt
RUN mvn package

FROM openjdk:21-jdk-bullseye

COPY --from=builder /app/target/*.jar app.jar

CMD java $JAVA_OPTS $KS_OPTS $AGENT_OPTS $MEM_OPTS -jar app.jar $SPRING_OPTS
EXPOSE 9098
