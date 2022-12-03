FROM eclipse-temurin:17
RUN apt-get update && apt-get -y upgrade
RUN apt-get install -y inotify-tools dos2unix
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . .
CMD ["./mvnw", "install -DskipTests"]
ENTRYPOINT ["java","-jar","target/squads-0.0.1-SNAPSHOT.jar"]