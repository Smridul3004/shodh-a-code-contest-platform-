#!/bin/bash

# Start script for Railway deployment
echo "Starting Shodh-a-Code Contest Platform..."

# Start backend
echo "Starting Spring Boot backend..."
cd backend
java -jar target/shodh-a-code-0.0.1-SNAPSHOT.jar &
BACKEND_PID=$!

# Wait for backend to start
sleep 10

# Start frontend
echo "Starting Next.js frontend..."
cd ../frontend
npm start &
FRONTEND_PID=$!

# Wait for both processes
wait $BACKEND_PID $FRONTEND_PID
