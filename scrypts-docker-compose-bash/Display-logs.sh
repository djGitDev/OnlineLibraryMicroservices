#!/usr/bin/env bash
# Docker Logs Viewer (docker-logs.sh)

DELAY_SECONDS=1

# Fonction de coloration
color_green='\033[1;32m'
color_yellow='\033[1;33m'
color_reset='\033[0m'

# Récupérer tous les IDs des conteneurs en cours d'exécution
container_ids=$(docker ps -q)

# Vérifier s'il y a des conteneurs en cours d'exécution
if [[ -z "$container_ids" ]]; then
    echo "Aucun conteneur Docker en cours d'exécution."
    exit 0
fi

# Convertir en tableau pour pouvoir compter les éléments
IFS=$'\n' read -d '' -r -a container_array <<< "$container_ids"
total_containers=${#container_array[@]}
current_count=0

# Boucle à travers chaque ID de conteneur
for id in $container_ids; do
    ((current_count++))
    
    # Récupérer le nom du conteneur
    container_name=$(docker inspect --format '{{.Name}}' "$id" | cut -c 2-)
    
    # Afficher l'en-tête
    echo -e "${color_green}=== Logs du conteneur $id ($container_name) ===${color_reset}"
    docker logs "$id"
    echo -e "\n$(printf '%80s' | tr ' ' '-')\n"
    
    # Pause seulement si ce n'est pas le dernier conteneur
    if [[ $current_count -lt $total_containers ]]; then
        echo -e "${color_yellow}Attente de $DELAY_SECONDS secondes avant le prochain conteneur...${color_reset}"
        sleep "$DELAY_SECONDS"
    fi
done