#!/bin/sh

while true; do
java $JAVA_OPTS -server -XX:+AlwaysPreTouch -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -Djava.security.egd=file:/dev/./urandom -jar /app.jar

if [ $? -eq 5 ]; then
  echo " ............. Restarting ............. "
else
  exit 1;
fi

done

