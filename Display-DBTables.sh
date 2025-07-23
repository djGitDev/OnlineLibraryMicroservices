#!/usr/bin/env bash
# PostgreSQL Container Inspector (pg-inspector.sh)

# Configuration
declare -A containers=(
    ["db-profil"]="5432:ProfilServiceDB:postgres:mypass"
    ["db-inventary"]="5433:InventaryServiceDB:postgres:mypass"
    ["db-order"]="5434:OrderServiceDB:postgres:mypass"
    ["db-payment"]="5435:PaymentServiceDB:postgres:mypass"
    ["db-cart"]="5436:CartServiceDB:postgres:mypass"
)

# Colors
color_cyan='\033[1;36m'
color_green='\033[1;32m'
color_yellow='\033[1;33m'
color_red='\033[1;31m'
color_reset='\033[0m'

# Function to inspect tables
inspect_tables() {
    local container=$1
    IFS=':' read -r port db user pass <<< "${containers[$container]}"
    
    echo -e "\n${color_cyan}[$container] Connecting to database $db...${color_reset}"
    
    # Check if container is running
    if ! status=$(docker inspect -f '{{.State.Status}}' "$container" 2>/dev/null); then
        echo -e "${color_red}[$container] Container not found${color_reset}"
        return 1
    fi
    
    if [ "$status" != "running" ]; then
        echo -e "${color_red}[$container] Container not running (status: $status)${color_reset}"
        return 1
    fi

    # Query to get tables
    local query="SELECT table_schema || '.' || table_name AS table_full_name
                FROM information_schema.tables
                WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
                ORDER BY table_schema, table_name;"

    echo -e "${color_green}Tables found in $db:${color_reset}"
    
    # Execute query and format output
    if ! docker exec "$container" psql -U "$user" -d "$db" -c "$query" 2>&1 | 
         grep -E '\w+\.\w+' | 
         while read -r line; do
             echo -e "${color_yellow}  $line${color_reset}"
         done; then
        echo -e "${color_red}Error executing query on $container${color_reset}"
        return 1
    fi
}

# Main execution
for container in "${!containers[@]}"; do
    inspect_tables "$container"
    sleep 1  # Pause between containers
done

echo -e "\n${color_green}Analysis completed for all containers.${color_reset}"