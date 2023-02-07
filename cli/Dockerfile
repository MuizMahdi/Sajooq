FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-22.3.0-b2 as graalvm
RUN gu install native-image
COPY . /home/app/sajooq-cli
WORKDIR /home/app/sajooq-cli
RUN native-image -jar target/sajooq-cli-*-jar-with-dependencies.jar

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/sajooq-cli /app/sajooq-cli
ENTRYPOINT ["/app/sajooq-cli"]