@echo off
echo Starting Shodh-a-Code Backend...
cd backend

echo Setting JAVA_HOME to your JDK installation...
set JAVA_HOME=C:\Program Files\Java\jdk-20

echo JAVA_HOME is set to: %JAVA_HOME%
echo Starting Spring Boot application...
mvnw.cmd spring-boot:run
pause
