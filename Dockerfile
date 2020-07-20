FROM openjdk:11.0.8-jre

COPY ./dist /opt/data-validation

WORKDIR /opt/data-validation/bin

ENTRYPOINT  ["./data-validator-cli"]
