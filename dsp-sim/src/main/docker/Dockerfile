FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp

#set-up config
#RUN mkdir -p /usr/local/open-ssp/sim/logfiles
#RUN mkdir -p /usr/local/open-ssp/sim/properties
COPY /properties/dsp-sim.properties .
COPY  run_sim.sh run.sh

ADD open-ssp-dsp-sim.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT exec run.sh
