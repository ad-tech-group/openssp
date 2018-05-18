FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp

#set-up config
#RUN mkdir -p /usr/local/open-ssp/sim/logfiles
#RUN mkdir -p /usr/local/open-ssp/sim/properties
COPY /properties/dsp-sim.properties .

ADD open-ssp-dsp-sim.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 8081
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -server -XX:+AlwaysPreTouch -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -Djava.security.egd=file:/dev/./urandom -jar /app.jar

