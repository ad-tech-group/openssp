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
RUN echo "<html><body><div>openssp data services - healthy</div></body></html>" > /usr/local/tomcat/webapps/ROOT/index.jsp
RUN cat /usr/local/tomcat/webapps/ROOT/WEB-INF/web.xml

#copy our war into place
COPY /open-ssp-services.war /usr/local/tomcat/webapps

#set-up config
RUN mkdir -p /usr/local/tomcat/logfiles
RUN mkdir -p /usr/local/tomcat/properties
RUN mkdir -p /usr/local/tomcat/conf/certs
RUN mkdir -p /root

COPY cert/pub.network.jks conf/certs/star.pub.network.jks
COPY cert/pub.network.jks /root/.keystore

RUN keytool -storepasswd -new changeit -storepass kDKmHY8ecT9nY3WAwWhqk5ZnF4eTYX2b -keystore /root/.keystore
RUN keytool -keypasswd -alias pub.network -new changeit -storepass changeit -keypass kDKmHY8ecT9nY3WAwWhqk5ZnF4eTYX2b -keystore /root/.keystore

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
#RUN #sed -i -e 's/8080/8886/g' /usr/local/tomcat/conf/server.xml

EXPOSE 3024
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]

