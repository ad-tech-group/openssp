FROM tomcat:8.0.20-jre8
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
COPY /properties/config.xml /usr/local/tomcat/properties
COPY /properties/global.runtime.xml /usr/local/tomcat/properties
COPY /properties/local.runtime.xml /usr/local/tomcat/properties
COPY /properties/log4j2.xml /usr/local/tomcat/properties

COPY /properties/app_db.json /usr/local/tomcat/properties
COPY /properties/currency_db.json /usr/local/tomcat/properties
COPY /properties/price_layer.json /usr/local/tomcat/properties
COPY /properties/site_db.json /usr/local/tomcat/properties
COPY /properties/supplier_db.json /usr/local/tomcat/properties

#RUN #sed -i -e 's/8080/8886/g' /usr/local/tomcat/conf/server.xml
COPY /properties/server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 8886

CMD ["/usr/local/tomcat/bin/startup.sh", "run"]

