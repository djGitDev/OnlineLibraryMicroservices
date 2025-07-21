#!/usr/bin/env pwsh

# Configuration
$DELAY_SECONDS = 1

# Récupérer tous les IDs des conteneurs en cours d'exécution
$containerIds = docker ps -q

# Vérifier s'il y a des conteneurs en cours d'exécution
if (-not $containerIds) {
    Write-Host "Aucun conteneur Docker en cours d'exécution."
    exit 0
}

# Boucle à travers chaque ID de conteneur
foreach ($id in $containerIds) {
    # Récupérer le nom du conteneur pour un affichage plus clair
    $containerName = docker inspect --format '{{.Name}}' $id | ForEach-Object { $_.Substring(1) }
    
    Write-Host "=== Logs du conteneur $id ($containerName) ===" -ForegroundColor Green
    docker logs $id
    Write-Host "`n" + ("-" * 80) + "`n"
    
    # Pause seulement si ce n'est pas le dernier conteneur
    if ($id -ne $containerIds[-1]) {
        Write-Host "Attente de $DELAY_SECONDS secondes avant le prochain conteneur..." -ForegroundColor Yellow
        Start-Sleep -Seconds $DELAY_SECONDS
    }
}