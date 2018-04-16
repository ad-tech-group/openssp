FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD open-ssp-services.war open-ssp-services.war
RUN sh -c 'touch /open-ssp-services.war'
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -server -XX:+AlwaysPreTouch -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly  -Djava.security.egd=file:/dev/./urandom -jar /open-ssp-services.war
