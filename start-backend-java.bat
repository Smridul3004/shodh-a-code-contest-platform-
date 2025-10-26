@echo off
echo Starting Shodh-a-Code Backend...
cd backend

echo Checking for Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17+ from https://adoptium.net/
    echo Or set JAVA_HOME environment variable
    pause
    exit /b 1
)

echo Java found! Starting Spring Boot application...
mvnw.cmd spring-boot:run
pause
