#!/bin/bash

echo `svn update /home/warfile/source/trip-master/ --username terrytang --password 09050831 --no-auth-cache 2>&1`
echo "svn update done."
#sudu su

sudo ps -ef | grep "tomcate" | grep -v "grep\|$$" | awk '{print $2}'| xargs sudo kill -9
echo "<br>killed tomcat process "
cd /home/warfile/source/trip-master

echo "<br>maven build:"
echo `mvn clean install`

#echo `sudo -u phpuser -E /usr/local/go/bin/go build`
echo  "<br>tomcat redeploy"
echo `mvn tomcat:redeploy`

echo "<br> trip-master tomcat started!"
