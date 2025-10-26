@echo off
echo ========================================
echo   Shodh-a-Code Contest Platform
echo ========================================
echo.

echo Step 1: Building Docker image for code execution...
cd backend
docker build -t shodh-a-code-judge:latest .
if %errorlevel% neq 0 (
    echo Error building Docker image. Please ensure Docker is running.
    pause
    exit /b 1
)

echo.
echo Step 2: Starting Spring Boot backend...
start "Backend" cmd /k "mvnw.cmd spring-boot:run"

echo.
echo Step 3: Waiting for backend to start (30 seconds)...
timeout /t 30 /nobreak >nul

echo.
echo Step 4: Starting React frontend...
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
echo Press any key to exit this window...
pause >nul
