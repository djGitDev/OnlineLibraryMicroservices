# Script de nettoyage Docker simplifié
Write-Host "Nettoyage Docker en cours..." -ForegroundColor Yellow

# 1. Nettoyer les volumes
docker volume prune -f

# 2. Arrêter tous les conteneurs
docker stop $(docker ps -aq)

# 3. Supprimer tous les conteneurs
docker rm $(docker ps -aq)

# 4. Supprimer toutes les images
Write-Host "3. Suppression des images..." -ForegroundColor Yellow
docker rmi -f $(docker images -aq) 2>$null

Write-Host "Nettoyage des images et conteuneurs en execution terminé!" -ForegroundColor Green

#5. lancer docker compose
docker compose up -d

#6. afficher lles conteneurs en mode running
Write-Host "Running containers" -ForegroundColor Green

docker ps

#7. afficher les conteuneurs arretes
Write-Host "Stopped containers" -ForegroundColor Red

docker ps -a --filter "status=exited"





