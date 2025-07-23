#!/usr/bin/env bash
# Targeted Docker Cleanup Script

# Colors
COLOR_GREEN='\033[1;32m'
COLOR_RESET='\033[0m'

# Display running containers with their images
echo "Current running containers:"
docker ps --format "table {{.ID}}\t{{.Image}}\t{{.Names}}"

# Prompt for required information
echo -e "\nEnter the container ID to remove:"
read -r containerId

echo "Enter the image name to remove (leave empty to skip):"
read -r imageName

# Stop and remove the container
echo "Stopping and removing container $containerId..."
docker stop "$containerId" && docker rm "$containerId"

# Remove the image if specified
if [ -n "$imageName" ]; then
    echo "Removing image $imageName..."
    docker rmi "$imageName"
fi

# Operation summary
echo -e "\n${COLOR_GREEN}Operation completed:${COLOR_RESET}"
echo "- Container $containerId removed"
if [ -n "$imageName" ]; then
    echo "- Image $imageName removed"
fi