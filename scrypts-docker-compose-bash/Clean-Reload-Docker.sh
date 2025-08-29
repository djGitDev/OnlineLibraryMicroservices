#!/bin/bash

# Docker Cleanup Script

echo -e "\033[33mDocker cleanup in progress...\033[0m"  # Yellow text

# 1. Clean up volumes
echo "Cleaning up volumes..."
docker volume prune -f

# 2. Stop all containers
echo "Stopping all containers..."
docker stop $(docker ps -aq) 2>/dev/null

# 3. Remove all containers
echo "Removing all containers..."
docker rm $(docker ps -aq) 2>/dev/null

echo -e "\033[32mContainer cleanup completed!\033[0m"  # Green text

# 4. Start docker compose
echo "Starting services with docker compose..."
docker compose up -d

# 5. Show running containers
echo -e "\n\033[32mRunning containers:\033[0m"  # Green text
docker ps

# 6. Show stopped containers
echo -e "\n\033[31mStopped containers:\033[0m"  # Red text
docker ps -a --filter "status=exited"