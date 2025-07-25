#!/usr/bin/env bash
# Intelligent Docker Cleanup Script

# Colors
COLOR_CYAN='\033[1;36m'
COLOR_YELLOW='\033[1;33m'
COLOR_GREEN='\033[1;32m'
COLOR_RED='\033[1;31m'
COLOR_BLUE='\033[1;34m'
COLOR_RESET='\033[0m'

# Log function
log() {
    local color="$1"
    local message="$2"
    echo -e "${color}${message}${COLOR_RESET}"
}

# Header
echo -e "\n${COLOR_CYAN}=== STARTING DOCKER CLEANUP ===${COLOR_RESET}\n"

# 1. Global Docker environment check
if ! command -v docker &> /dev/null; then
    log "$COLOR_RED" "ERROR: Docker is not installed or not working"
    exit 1
fi

# 2. Container check
all_containers=($(docker ps -aq))
if [ ${#all_containers[@]} -eq 0 ]; then
    log "$COLOR_YELLOW" "No containers found. Proceeding to start services..."
    docker compose up -d
    exit 0
fi

# 3. Volume check and cleanup
volumes=($(docker volume ls -q))
if [ ${#volumes[@]} -gt 0 ]; then
    log "$COLOR_YELLOW" "Cleaning up volumes (${#volumes[@]} found)..."
    docker volume prune -f
else
    log "$COLOR_GREEN" "No volumes to clean"
fi

# 4. Running containers check and stop
running_containers=($(docker ps -q))
if [ ${#running_containers[@]} -gt 0 ]; then
    log "$COLOR_YELLOW" "Stopping running containers (${#running_containers[@]})..."
    docker stop "${running_containers[@]}"
else
    log "$COLOR_GREEN" "No running containers"
fi

# 5. Container removal check
if [ ${#all_containers[@]} -gt 0 ]; then
    log "$COLOR_YELLOW" "Removing containers (${#all_containers[@]})..."
    docker rm "${all_containers[@]}" 2>/dev/null
fi

# 6. Remove all images
log "$COLOR_YELLOW" "Removing images..."
docker rmi -f $(docker images -aq) 2>/dev/null || true

# 7. Service restart
log "$COLOR_CYAN" "\nStarting services..."
docker compose up -d

# 8. Final report
echo -e "\n${COLOR_CYAN}=== FINAL REPORT ==="
log "$COLOR_GREEN" "Active containers:"
docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Status}}"

log "$COLOR_BLUE" "\nRemaining images:"
docker images --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}"

log "$COLOR_GREEN" "\nCleanup completed successfully!"