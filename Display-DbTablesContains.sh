#!/usr/bin/env bash
# PostgreSQL Container Inspector (pg-inspector.sh)

set -eo pipefail  # Enable strict error handling

# Color definitions
COLOR_CYAN='\033[1;36m'
COLOR_GREEN='\033[1;32m'
COLOR_YELLOW='\033[1;33m'
COLOR_RED='\033[1;31m'
COLOR_RESET='\033[0m'

# Container configurations
declare -A CONTAINERS=(
    ["db-profil"]="ProfilServiceDB:postgres:mypass"
    ["db-inventary"]="InventaryServiceDB:postgres:mypass"
    ["db-order"]="OrderServiceDB:postgres:mypass"
    ["db-payment"]="PaymentServiceDB:postgres:mypass"
    ["db-cart"]="CartServiceDB:postgres:mypass"
)

# Function to print colored messages
log() {
    local color="$1"
    local message="$2"
    echo -e "${color}${message}${COLOR_RESET}"
}

# Function to inspect database tables
inspect_all_tables() {
    local container_name="$1"
    IFS=':' read -r db_name db_user db_pass <<< "${CONTAINERS[$container_name]}"
    
    log "$COLOR_CYAN" "\n[$container_name] Inspecting database $db_name..."
    
    # 1. List all tables
    local list_query="SELECT table_schema || '.' || table_name AS full_table_name
                     FROM information_schema.tables
                     WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
                     ORDER BY table_schema, table_name;"
    
    local tables
    if ! tables=$(docker exec "$container_name" psql -U "$db_user" -d "$db_name" -c "$list_query" 2>&1); then
        log "$COLOR_RED" "Error retrieving tables: $tables"
        return 1
    fi
    
    local tables_list=$(echo "$tables" | grep -E '\w+\.\w+')
    if [[ -z "$tables_list" ]]; then
        log "$COLOR_YELLOW" "No tables found in this database"
        return 0
    fi

    # 2. Inspect each table
    while IFS= read -r table; do
        log "$COLOR_GREEN" "\nTable: $table"
        echo "------------------------"
        
        local select_query="SELECT * FROM $table LIMIT 100;"
        if ! docker exec "$container_name" psql -U "$db_user" -d "$db_name" -c "$select_query" 2>&1; then
            log "$COLOR_RED" "SELECT error for table $table"
        fi
        
        sleep 0.5  # Small pause between tables
    done <<< "$tables_list"
}

# Main execution
for container_name in "${!CONTAINERS[@]}"; do
    container_status=$(docker inspect -f '{{.State.Status}}' "$container_name" 2>/dev/null || true)
    
    if [[ "$container_status" == "running" ]]; then
        if ! inspect_all_tables "$container_name"; then
            log "$COLOR_RED" "Failed to inspect $container_name"
        fi
        sleep 1  # Pause between containers
    else
        log "$COLOR_RED" "\n[$container_name] Container not running (status: ${container_status:-not found})"
    fi
done

log "$COLOR_GREEN" "\nInspection completed for all containers."