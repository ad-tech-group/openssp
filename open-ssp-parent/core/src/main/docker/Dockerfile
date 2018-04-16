FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD open-ssp.jar open-ssp.war
RUN sh -c 'touch /open-ssp.war'
ENV JAVA_OPTS=""
#ENTRYPOINT exec java $JAVA_OPTS -server -XX:+AlwaysPreTouch -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly  -Djava.security.egd=file:/dev/./urandom -jar /app.jar
ENTRYPOINT exec java  -Dmaven.multiModuleProjectDirectory=/Users/bsorensen/master_project/workspace/openssp/open-ssp-parent/core "-Dmaven.home=/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3" "-Dclassworlds.conf=/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/m2.conf" "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=61883:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/boot/plexus-classworlds-2.5.2.jar" org.codehaus.classworlds.Launcher -Didea.version=2017.3.5 -DskipTests=true org.apache.tomcat.maven:tomcat7-maven-plugin:2.2:run-war
