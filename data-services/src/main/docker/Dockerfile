
#clean up extra apps
RUN rm -rf /usr/local/tomcat/webapps/examples
RUN rm -rf /usr/local/tomcat/webapps/docs
RUN rm -rf /usr/local/tomcat/webapps/host-manager
RUN rm -rf /usr/local/tomcat/webapps/manager

#copy our war into place
COPY /open-ssp-services.war /usr/local/tomcat/webapps

#set-up config
RUN mkdir -p /usr/local/tomcat/logfiles
RUN mkdir -p /usr/local/tomcat/properties

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
