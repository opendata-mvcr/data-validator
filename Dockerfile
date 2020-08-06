FROM openjdk:11.0.8-jre

COPY ./dist /opt/data-validation

WORKDIR /opt/data-validation/bin

RUN chmod a+x ./data-validator-cli

RUN ls -l

ENTRYPOINT  ["./data-validator-cli"]
