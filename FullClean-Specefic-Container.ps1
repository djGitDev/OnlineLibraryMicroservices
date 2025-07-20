<#
.SYNOPSIS
    Nettoie un conteneur Docker et son image en spécifiant directement l'ID et le nom de l'image
#>

# Affiche les conteneurs actifs avec leurs images
docker ps --format "table {{.ID}}\t{{.Image}}\t{{.Names}}"

# Demande les infos nécessaires
$containerId = Read-Host "`nEntrez l'ID du conteneur a supprimer"
$imageName = Read-Host "Entrez le nom de l'image à supprimer (laisser vide pour ignorer)"

# Arrête et supprime le conteneur
docker stop $containerId
docker rm $containerId

# Supprime l'image si spécifiée
if ($imageName) {
    docker rmi $imageName
}

Write-Host "`nOperation terminee :" -ForegroundColor Green
Write-Host "- Conteneur $containerId supprime"
if ($imageName) {
    Write-Host "- Image $imageName supprimee"
}