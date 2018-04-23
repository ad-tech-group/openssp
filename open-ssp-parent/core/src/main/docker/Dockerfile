FROM tomcat:8.0.20-jre8
#clean up extra apps
RUN rm -rf /usr/local/tomcat/webapps/examples
RUN rm -rf /usr/local/tomcat/webapps/docs
RUN rm -rf /usr/local/tomcat/webapps/host-manager
RUN rm -rf /usr/local/tomcat/webapps/manager

#copy our war into place
COPY /open-ssp.war /usr/local/tomcat/webapps

#set-up config
RUN mkdir -p /usr/local/tomcat/logfiles
RUN mkdir -p /usr/local/tomcat/properties
RUN mkdir -p /usr/local/tomcat/conf/certs
RUN mkdir -p /usr/local/ssl

COPY /cert/star.pub.network.key /usr/local/ssl
COPY /cert/star.pub.network.cert /usr/local/ssl

RUN echo "changeit\nchangeit\nBrian Sorensen\nengineering\nfreestar.io\nphoenix\nAZ\nUS\ny\n\n" | /usr/bin/keytool -genkey -alias tomcat -keyalg RSA

#RUN /usr/bin/keytool -genkey -alias tomcat -keyalg RSA
#RUN openssl genrsa -aes256 -out ca-key.pem 4096
#RUN openssl req -new -x509 -days 365 -key ca-key.pem -sha256 -out ca.pem
#RUN openssl req -new -x509 -text -key client.key -out client.cert

COPY /properties/config.xml /usr/local/tomcat/properties
COPY /properties/global.runtime.xml /usr/local/tomcat/properties
COPY /properties/local.runtime.xml /usr/local/tomcat/properties
COPY /properties/log4j2.xml /usr/local/tomcat/properties

#COPY /properties/server.xml /usr/local/tomcat/conf/server.xml

COPY /properties/server.xml /usr/local/tomcat/conf/server.xml
#RUN #sed -i -e 's/8080/8886/g' /usr/local/tomcat/conf/server.xml

EXPOSE 3025
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]

