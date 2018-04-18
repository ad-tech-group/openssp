FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD open-ssp.war app.war
RUN sh -c 'touch /app.war'
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -server -XX:+AlwaysPreTouch -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly  -Djava.security.egd=file:/dev/./urandom -jar /app.war