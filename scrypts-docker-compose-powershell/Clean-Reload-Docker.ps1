# Simplified Docker cleanup script
Write-Host "Docker cleanup in progress..." -ForegroundColor Yellow

# 1. Clean up volumes
docker volume prune -f

# 2. Stop all containers
docker stop $(docker ps -aq)

# 3. Remove all containers
docker rm $(docker ps -aq)

Write-Host "Container cleanup completed!" -ForegroundColor Green

# 4. Start docker compose
docker compose -f ../docker-compose.yaml up -d

# 5. Show running containers
Write-Host "Running containers" -ForegroundColor Green
docker ps

# 6. Show stopped containers
Write-Host "Stopped containers" -ForegroundColor Red
docker ps -a --filter "status=exited"