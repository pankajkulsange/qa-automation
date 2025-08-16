@echo off
echo Starting Jenkins on port 8080...
echo Jenkins will be available at: http://localhost:8080
echo.
echo To stop Jenkins, press Ctrl+C
echo.
java -jar jenkins.war --httpPort=8080 --prefix=/jenkins
