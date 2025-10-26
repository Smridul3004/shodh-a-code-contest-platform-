@echo off
echo Starting Shodh-a-Code Backend...
cd backend

echo Finding Java installation...
for /f "tokens=*" %%i in ('where java 2^>nul') do (
    echo Found Java at: %%i
    if "%%i"=="C:\Program Files\Java\jdk-20\bin\java.exe" (
        set JAVA_PATH=%%i
        goto :found_java
    )
)

echo ERROR: Java not found in PATH
pause
exit /b 1

:found_java
echo Found Java at: %JAVA_PATH%

REM Extract JAVA_HOME from the java.exe path
for %%i in ("%JAVA_PATH%") do set JAVA_HOME=%%~dpi
set JAVA_HOME=%JAVA_HOME:~0,-1%
for %%i in ("%JAVA_HOME%") do set JAVA_HOME=%%~dpi
set JAVA_HOME=%JAVA_HOME:~0,-1%

echo Setting JAVA_HOME to: %JAVA_HOME%
set JAVA_HOME=%JAVA_HOME%

echo Starting Spring Boot application...
mvnw.cmd spring-boot:run
pause
