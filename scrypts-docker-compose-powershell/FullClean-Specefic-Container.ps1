<#
.SYNOPSIS
    Cleans up a Docker container and its image by directly specifying the ID and image name
#>

# Display running containers with their images
docker ps --format "table {{.ID}}\t{{.Image}}\t{{.Names}}"

# Prompt for required information
$containerId = Read-Host "`nEnter the container ID to remove"
$imageName = Read-Host "Enter the image name to remove (leave empty to skip)"

# Stop and remove the container
docker stop $containerId
docker rm $containerId

# Remove the image if specified
if ($imageName) {
    docker rmi $imageName
}

Write-Host "`nOperation completed:" -ForegroundColor Green
Write-Host "- Container $containerId removed"
if ($imageName) {
    Write-Host "- Image $imageName removed"
}