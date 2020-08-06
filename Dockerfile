FROM openjdk:11.0.8-jre
COPY ./dist /opt/data-validation
RUN chmod a+x /opt/data-validation/bin/data-validator-cli
ENTRYPOINT  ["/opt/data-validation/bin/data-validator-cli"]
