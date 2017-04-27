########################################################################
########################################################################
#                        JAVA Web Server                               #
#                        Version: 0.0.1-SNAPSHOT                       #
#                        Date: 27-Apr-2017                             #
########################################################################
########################################################################

How to build?
Source code contains pom.xml file with maven compile plugin which creates
executable jar file.
Open command prompt where source code along with pom.xml is placed and run
below command
mvn clean install

This build assumes that Maven is already installed on this PC.


How to Start?
Once build is successful, it generates "JavaWebServer.jar" file.
Execute this jar file using below command,

java -jar JavaWebServer.jar

Omce done, open any browser and hit the url - http://localhost:8080

########################################################################
########################################################################

