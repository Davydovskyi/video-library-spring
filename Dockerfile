FROM alpine:3.20 AS base

RUN apk add git openjdk21 && apk cache clean

RUN git clone https://github.com/Davydovskyi/video-library-spring.git

WORKDIR /video-library-spring

RUN chmod +x gradlew && ./gradlew bootJar

FROM alpine:3.20 AS result

RUN apk add openjdk21 && apk cache clean

WORKDIR /app
COPY --from=base /video-library-spring/build/libs/video-library-spring-*.jar ./video-library-spring.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "video-library-spring.jar"]
CMD ["--DATABASE_URL=jdbc:postgresql://some-postgres:5432/video-library-spring"]