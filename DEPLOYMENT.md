# Deployment Guide

## Prerequisites

### System Requirements
- Docker 20.10+
- Docker Compose 2.0+
- 4GB RAM minimum
- 2GB free disk space

### For Local Development
- Java 17+
- Node.js 18+
- Maven 3.6+

## Quick Deployment

### 1. Clone Repository
```bash
git clone <your-repo-url>
cd shodh-a-code-contest-platform
```

### 2. Start with Docker Compose
```bash
# Build and start all services
docker-compose up --build

# Or run in background
docker-compose up -d --build
```

### 3. Access Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **H2 Database Console**: http://localhost:8080/h2-console

## Manual Deployment

### Backend Deployment

#### Option 1: Docker
```bash
cd backend
docker build -t shodh-a-code-backend .
docker run -p 8080:8080 shodh-a-code-backend
```

#### Option 2: Local Java
```bash
cd backend
./mvnw clean package
java -jar target/shodh-a-code-0.0.1-SNAPSHOT.jar
```

### Frontend Deployment

#### Option 1: Docker
```bash
cd frontend
docker build -t shodh-a-code-frontend .
docker run -p 3000:3000 shodh-a-code-frontend
```

#### Option 2: Local Node.js
```bash
cd frontend
npm install
npm run build
npm start
```

## Production Deployment

### Environment Variables

Create `.env` file:
```env
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/shodh_contest
SPRING_DATASOURCE_USERNAME=contest_user
SPRING_DATASOURCE_PASSWORD=secure_password

# Docker
DOCKER_HOST=unix:///var/run/docker.sock

# Frontend
NEXT_PUBLIC_API_URL=https://api.yourdomain.com
```

### Database Setup

#### PostgreSQL (Recommended for Production)
```bash
# Create database
createdb shodh_contest

# Update application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shodh_contest
    username: contest_user
    password: secure_password
  jpa:
    hibernate:
      ddl-auto: update
```

### Docker Compose for Production

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: shodh_contest
      POSTGRES_USER: contest_user
      POSTGRES_PASSWORD: secure_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/shodh_contest
      - SPRING_DATASOURCE_USERNAME=contest_user
      - SPRING_DATASOURCE_PASSWORD=secure_password
    depends_on:
      - postgres
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock

  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
    environment:
      - NEXT_PUBLIC_API_URL=http://localhost:8080

volumes:
  postgres_data:
```

### SSL/HTTPS Setup

#### Using Nginx Reverse Proxy
```nginx
server {
    listen 443 ssl;
    server_name yourdomain.com;

    ssl_certificate /path/to/certificate.crt;
    ssl_certificate_key /path/to/private.key;

    location / {
        proxy_pass http://localhost:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## Monitoring and Logging

### Health Checks
```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend health
curl http://localhost:3000/api/health
```

### Log Management
```bash
# View logs
docker-compose logs -f backend
docker-compose logs -f frontend

# Log rotation
docker-compose logs --tail=100 backend
```

## Scaling

### Horizontal Scaling
```yaml
services:
  backend:
    deploy:
      replicas: 3
    ports:
      - "8080-8082:8080"
```

### Load Balancer
```nginx
upstream backend {
    server localhost:8080;
    server localhost:8081;
    server localhost:8082;
}

server {
    location /api {
        proxy_pass http://backend;
    }
}
```

## Troubleshooting

### Common Issues

#### Port Already in Use
```bash
# Find process using port
lsof -i :8080
lsof -i :3000

# Kill process
kill -9 <PID>
```

#### Docker Permission Issues
```bash
# Add user to docker group
sudo usermod -aG docker $USER
# Logout and login again
```

#### Memory Issues
```bash
# Increase Docker memory limit
docker-compose up --scale backend=1 --scale frontend=1
```

### Debug Mode
```bash
# Backend debug
docker-compose up backend --build --no-cache

# Frontend debug
cd frontend
npm run dev
```

## Backup and Recovery

### Database Backup
```bash
# PostgreSQL backup
pg_dump shodh_contest > backup.sql

# Restore
psql shodh_contest < backup.sql
```

### Application Backup
```bash
# Backup entire application
tar -czf shodh-contest-backup.tar.gz .
```

## Security Considerations

### Production Security Checklist
- [ ] Change default database passwords
- [ ] Enable HTTPS/SSL
- [ ] Implement authentication
- [ ] Set up firewall rules
- [ ] Regular security updates
- [ ] Monitor logs for suspicious activity
- [ ] Implement rate limiting
- [ ] Use secrets management

### Docker Security
```bash
# Run containers as non-root user
docker run --user 1000:1000 shodh-a-code-backend

# Use read-only filesystem
docker run --read-only shodh-a-code-backend
```
