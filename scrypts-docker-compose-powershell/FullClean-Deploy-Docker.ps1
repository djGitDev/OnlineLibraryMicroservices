#!/usr/bin/env pwsh

<#
.SYNOPSIS
Intelligent Docker cleanup script with comprehensive checks
#>

Write-Host "`n=== STARTING DOCKER CLEANUP ===`n" -ForegroundColor Cyan

# 1. Global Docker environment check
try {
    docker version | Out-Null
}
catch {
    Write-Host "ERROR: Docker is not installed or not working" -ForegroundColor Red
    exit 1
}

# 2. Container check
$allContainers = @(docker ps -aq)
if ($allContainers.Count -eq 0) {
    Write-Host "No containers found. Proceeding to start services..." -ForegroundColor Yellow
    docker compose up -d
    exit 0
}

# 3. Volume check and cleanup
$volumes = @(docker volume ls -q)
if ($volumes.Count -gt 0) {
    Write-Host "Cleaning up volumes ($($volumes.Count) found)..." -ForegroundColor Yellow
    docker volume prune -f
}
else {
    Write-Host "No volumes to clean" -ForegroundColor Green
}

# 4. Running containers check and stop
$runningContainers = @(docker ps -q)
if ($runningContainers.Count -gt 0) {
    Write-Host "Stopping running containers ($($runningContainers.Count))..." -ForegroundColor Yellow
    docker stop $runningContainers
}
else {
    Write-Host "No running containers" -ForegroundColor Green
}

# 5. Container removal check
if ($allContainers.Count -gt 0) {
    Write-Host "Removing containers ($($allContainers.Count))..." -ForegroundColor Yellow
    docker rm $allContainers 2>$null | Out-Null
}

# 6. Remove all images
Write-Host "Removing images..." -ForegroundColor Yellow
docker rmi -f $(docker images -aq) 

# 7. Service restart
Write-Host "`nStarting services..." -ForegroundColor Cyan
docker compose -f ../docker-compose.yaml up -d

# 8. Final report
Write-Host "`n=== FINAL REPORT ===" -ForegroundColor Cyan
Write-Host "Active containers:" -ForegroundColor Green
docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Status}}"

Write-Host "`nRemaining images:" -ForegroundColor Blue
docker images --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}"

Write-Host "`nCleanup completed successfully!" -ForegroundColor Green