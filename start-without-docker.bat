@echo off
echo ========================================
echo   Shodh-a-Code Contest Platform
echo   (Running without Docker - Mock Mode)
echo ========================================
echo.

echo Step 1: Starting Spring Boot backend...
cd backend
start "Backend" cmd /k "mvnw.cmd spring-boot:run"

echo.
echo Step 2: Waiting for backend to start (30 seconds)...
timeout /t 30 /nobreak >nul

echo.
echo Step 3: Starting React frontend...
cd ..\frontend
start "Frontend" cmd /k "npm install && npm run dev"

echo.
echo ========================================
echo   Application is starting up!
echo ========================================
echo.
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Sample Contest ID: CONTEST001
echo.
echo NOTE: Running in mock mode - all submissions will be accepted
echo       Install Docker for full code execution functionality
echo.
echo Press any key to exit this window...
pause >nul
