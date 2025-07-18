# Script de nettoyage Docker simplifié
Write-Host "Nettoyage Docker en cours..." -ForegroundColor Yellow

# 1. Nettoyer les volumes
docker volume prune -f

# 2. Arrêter tous les conteneurs
docker stop $(docker ps -aq)

# 3. Supprimer tous les conteneurs
docker rm $(docker ps -aq)

Write-Host "Nettoyage des conteuneurs en execution terminé!" -ForegroundColor Green

#4. lancer docker compose
docker compose up -d

#5. afficher lles conteneurs en mode running
Write-Host "Running containers" -ForegroundColor Green

docker ps

#6. afficher les conteuneurs arretes
Write-Host "Stopped containers" -ForegroundColor Red

docker ps -a --filter "status=exited"
