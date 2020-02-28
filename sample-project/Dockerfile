FROM openjdk:11 AS base
LABEL image=base

COPY *.gradle gradlew /project/
COPY gradle /project/gradle
WORKDIR /project

FROM base AS dependencies
LABEL image=dependencies
RUN ./gradlew --version --no-daemon

FROM dependencies AS builder
LABEL image=build
ARG build
LABEL build=${build}
ADD . /project
RUN ./gradlew build --full-stacktrace --no-daemon

FROM openjdk:11
LABEL image=release
COPY --from=builder /project/build/libs/ /app/
WORKDIR /app
EXPOSE 8080
ENTRYPOINT exec java ${JAVA_OPTS:-} -Djava.security.egd=file:/dev/./urandom -jar simple-project-0.0.1.jar