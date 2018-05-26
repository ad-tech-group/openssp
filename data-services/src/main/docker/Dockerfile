FROM tomcat:8.0.20-jre8
#clean up extra apps
RUN rm -rf /usr/local/tomcat/webapps/examples
RUN rm -rf /usr/local/tomcat/webapps/docs
RUN rm -rf /usr/local/tomcat/webapps/host-manager
RUN rm -rf /usr/local/tomcat/webapps/manager
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*.xml
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*.png
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*.gif
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*.svg
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*.css
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*.ico
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*.txt
RUN echo "<html><body><div>ssp data services - healthy</div></body></html>" > /usr/local/tomcat/webapps/ROOT/index.jsp
RUN cat /usr/local/tomcat/webapps/ROOT/WEB-INF/web.xml

#copy our war into place
COPY /ssp-services.war /usr/local/tomcat/webapps

#set-up config
RUN mkdir -p /usr/local/tomcat/properties
RUN mkdir -p /usr/local/tomcat/logfiles
RUN mkdir -p /usr/local/tomcat/conf/certs
RUN mkdir -p /root

#COPY cert/openssp.jks conf/certs/openssp.jks
#COPY cert/openssp.jks /root/.keystore

COPY /properties/config.xml /usr/local/tomcat/properties
COPY /properties/global.runtime.xml /usr/local/tomcat/properties
COPY /properties/local.runtime.xml /usr/local/tomcat/properties
COPY /properties/log4j2.xml /usr/local/tomcat/properties

COPY /properties/app_db.json /usr/local/tomcat/properties
COPY /properties/currency_db.json /usr/local/tomcat/properties
COPY /properties/price_layer.json /usr/local/tomcat/properties
COPY /properties/site_db.json /usr/local/tomcat/properties
COPY /properties/supplier_db.json /usr/local/tomcat/properties

COPY /properties/server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 9090
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]

