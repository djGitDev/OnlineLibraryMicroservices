# # Script de nettoyage Docker simplifié
# Write-Host "Nettoyage Docker en cours..." -ForegroundColor Yellow

# # 1. Nettoyer les volumes
# docker volume prune -f

# # 2. Arrêter tous les conteneurs
# docker stop $(docker ps -aq)

# # 3. Supprimer tous les conteneurs
# docker rm $(docker ps -aq)

# # 4. Supprimer toutes les images
# Write-Host "3. Suppression des images..." -ForegroundColor Yellow
# docker rmi -f $(docker images -aq) 2>$null

# Write-Host "Nettoyage des images et conteuneurs en execution terminé!" -ForegroundColor Green

# #5. lancer docker compose
# docker compose up -d

# #6. afficher lles conteneurs en mode running
# Write-Host "Running containers" -ForegroundColor Green

# docker ps

# #7. afficher les conteuneurs arretes
# Write-Host "Stopped containers" -ForegroundColor Red

# docker ps -a --filter "status=exited"

#!/usr/bin/env pwsh

<#
.SYNOPSIS
Script intelligent de nettoyage Docker avec vérifications complètes
#>

Write-Host "`n=== DEBUT DU NETTOYAGE DOCKER ===`n" -ForegroundColor Cyan

# 1. Vérification globale de l'environnement Docker
try {
    docker version | Out-Null
}
catch {
    Write-Host "ERREUR: Docker n'est pas installé ou ne fonctionne pas" -ForegroundColor Red
    exit 1
}

# 2. Vérification des conteneurs
$allContainers = @(docker ps -aq)
if ($allContainers.Count -eq 0) {
    Write-Host "Aucun conteneur trouvé. Passage au demarrage des services..." -ForegroundColor Yellow
    docker compose up -d
    exit 0
}

# 3. Vérification et nettoyage des volumes
$volumes = @(docker volume ls -q)
if ($volumes.Count -gt 0) {
    Write-Host "Nettoyage des volumes ($($volumes.Count) trouves..." -ForegroundColor Yellow
    docker volume prune -f
}
else {
    Write-Host "Aucun volume a nettoyer" -ForegroundColor Green
}

# 4. Vérification et arrêt des conteneurs actifs
$runningContainers = @(docker ps -q)
if ($runningContainers.Count -gt 0) {
    Write-Host "Arret des conteneurs actifs ($($runningContainers.Count))..." -ForegroundColor Yellow
    docker stop $runningContainers
}
else {
    Write-Host "Aucun conteneur en cours d'execution" -ForegroundColor Green
}

# 5. Vérification et suppression des conteneurs
if ($allContainers.Count -gt 0) {
    Write-Host "Suppression des conteneurs ($($allContainers.Count))..." -ForegroundColor Yellow
    docker rm $allContainers 2>$null | Out-Null
}

# 6. Supprimer toutes les images
Write-Host "3. Suppression des images..." -ForegroundColor Yellow
docker rmi -f $(docker images -aq) 


# 7. Redémarrage des services
Write-Host "`nDemarrage des services..." -ForegroundColor Cyan
docker compose up -d

# 8. Rapport final
Write-Host "`n=== RAPPORT FINAL ===" -ForegroundColor Cyan
Write-Host "Conteneurs actifs:" -ForegroundColor Green
docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Status}}"

Write-Host "`nImages restantes:" -ForegroundColor Blue
docker images --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}"

Write-Host "`nNettoyage termine avec succees!" -ForegroundColor Green