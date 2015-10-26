FROM maven:3.3
MAINTAINER Jacob Tomlinson <jacob.tomlinson@informaticslab.co.uk>
MAINTAINER Tom Powell <thomas.powell@informaticslab.co.uk>

RUN mkdir /usr/src/molab-3dwx-ds
ADD src /usr/src/molab-3dwx-ds/src
ADD pom.xml /usr/src/molab-3dwx-ds/pom.xml

RUN cd /usr/src/molab-3dwx-ds && mvn clean install

RUN mkdir /opt/molab-3dwx-ds
WORKDIR /opt/molab-3dwx-ds

ENV APP_NAME "molab-3dwx-ds"
ENV HEADLESS_SETTING "-Djava.awt.headless=true"
ENV MEMORY_SETTINGS "-Xmx2048m -XX:PermSize=64m -XX:MaxPermSize=128m"
ENV JMX_SETTINGS "-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1072 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

RUN unzip /usr/src/molab-3dwx-ds/target/molab-3dwx-ds-1.0-SNAPSHOT-distribution.zip

EXPOSE 9000

CMD java ${MEMORY_SETTINGS} ${HEADLESS_SETTING} ${JMX_SETTINGS} -jar ${APP_NAME}.jar
