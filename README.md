# FaPra 2021 / 2022 - Kooperative algorithmische Kunst
The application works with linux as well as with windows. We used Windows for the developement and only tested linux once. So there might be unknown issues with linux.
There
## Start from sourcecode in an IDE:
Tested with IntelliJ and eclipse (we used eclipse).
Download and install https://projectlombok.org/download to your IDE.
Make sure that your IDE is running the projects with java 8 otherwise javafx is not found.
To start the application start the Starter class in the coop-algo-art.distribution project.

## Deployment by maven:
Download jdk 8 for your os 
Download and install Maven>3.3.9 https://maven.apache.org/download.cgi
Configure Maven to run with java 8
Call "mvn clean install" on cmd or shell in the reactor project( <gitrepo_gruppe2>/dev).
The examples use relative paths to a 'pics' directory which has to be located in the start directory of the application.
Run following commands in the cmd.
cd <gitrepo_gruppe2>/dev/distribution
<java8_installationpath>/bin/java -jar ./target/coop-algo-art.distribution-0.0.1-jar-with-dependencies.jar>mciGruppe2.log
 

## Start application by starting the fat-jar contained in the git repo:
To make it easier we copied the fat jar into the git repo.
Download java 8 for your os.
open cmd or shell and navigate to <gitrepo_gruppe2>/dev/distribution (only here are the pictures placed which are called by relativ paths in the examples)
run following command in the console
<java8_installationpath>/bin/java -jar coop-algo-art.distribution-0.0.1-jar-with-dependencies.jar>mciGruppe2.log

## Easiest way for windows
Download preconfigured distribution from https://www.dropbox.com/s/rvpyy1oabaabkdy/MCI_Fachpraktikum21_22_Gruppe2_Windows.zip?dl=0 . Unzip the distribution and start the start.bat.


