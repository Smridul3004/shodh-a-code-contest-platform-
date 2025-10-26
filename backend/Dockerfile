FROM openjdk:17-jdk-slim

# Install necessary tools
RUN apt-get update && apt-get install -y \
    gcc \
    g++ \
    python3 \
    python3-pip \
    nodejs \
    npm \
    && rm -rf /var/lib/apt/lists/*

# Create working directory
WORKDIR /app

# Copy the execution script
COPY execute.sh /app/execute.sh
RUN chmod +x /app/execute.sh

# Set the entrypoint
ENTRYPOINT ["/app/execute.sh"]
