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
RUN mkdir -p /usr/local/tomcat/conf/certs
RUN mkdir -p /usr/local/ssl

COPY /cert/star.pub.network.key /usr/local/ssl
COPY /cert/star.pub.network.cert /usr/local/ssl
COPY cert/freestar.jks conf/certs/freestar.jks
COPY cert/pub.network.jks conf/certs/pub.network.jks

#RUN echo "changeit\nchangeit\nBrian Sorensen\nengineering\nfreestar.io\nphoenix\nAZ\nUS\ny\n\n" | /usr/bin/keytool -genkey -alias tomcat -keyalg RSA
#RUN /usr/bin/keytool -import -v -trustcacerts -alias keyAlias -file server.cer -keystore cacerts.jks -keypass changeit -storepass changeit
#RUN /usr/bin/keytool -import -v -trustcacerts -alias pub.network -keystore conf/certs/freestar.jks -keypass kDKmHY8ecT9nY3WAwWhqk5ZnF4eTYX2b -storepass kDKmHY8ecT9nY3WAwWhqk5ZnF4eTYX2b
RUN /usr/bin/keytool -import -v -trustcacerts -alias pub.network -keystore conf/certs/pub.network.jks -keypass kDKmHY8ecT9nY3WAwWhqk5ZnF4eTYX2b -storepass kDKmHY8ecT9nY3WAwWhqk5ZnF4eTYX2b

#RUN /usr/bin/keytool -genkey -alias tomcat -keyalg RSA
#RUN openssl genrsa -aes256 -out ca-key.pem 4096
#RUN openssl req -new -x509 -days 365 -key ca-key.pem -sha256 -out ca.pem
#RUN openssl req -new -x509 -text -key client.key -out client.cert

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

