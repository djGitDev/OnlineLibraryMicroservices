#!/usr/bin/env pwsh

# Configuration des conteneurs
$containers = @(
    @{
        Name = "db-profil";
        DB = "ProfilServiceDB";
        User = "postgres";
        Password = "mypass"
    },
    @{
        Name = "db-inventary";
        DB = "InventaryServiceDB";
        User = "postgres";
        Password = "mypass"
    }
     @{
        Name = "db-order";
        DB = "OrderServiceDB";
        User = "postgres";
        Password = "mypass"
    }
    @{
        Name = "db-payment";
        DB = "PaymentServiceDB";
        User = "postgres";
        Password = "mypass"
    }
)

# Fonction pour lister et inspecter toutes les tables
function Inspect-AllTables {
    param (
        $container
    )
    
    Write-Host "`n[${$container.Name}] Inspection de la base ${$container.DB}..." -ForegroundColor Cyan
    
    try {
        # 1. Lister toutes les tables
        $tables = docker exec $container.Name psql -U $container.User -d $container.DB -c "
            SELECT table_schema || '.' || table_name AS full_table_name
            FROM information_schema.tables
            WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
            ORDER BY table_schema, table_name;
        " 2>&1

        if ($LASTEXITCODE -ne 0) {
            Write-Host "Erreur lors de la récupération des tables: $tables" -ForegroundColor Red
            return
        }

        $tablesList = $tables | Where-Object { $_ -match "\w+\.\w+" }
        
        if (-not $tablesList) {
            Write-Host "Aucune table trouvée dans cette base" -ForegroundColor Yellow
            return
        }

        # 2. Pour chaque table, faire un SELECT
        foreach ($table in $tablesList) {
            Write-Host "`nTable: $table" -ForegroundColor Green
            Write-Host "------------------------" -ForegroundColor Green
            
            $query = "SELECT * FROM $table LIMIT 5;"
            $result = docker exec $container.Name psql -U $container.User -d $container.DB -c $query 2>&1
            
            if ($LASTEXITCODE -eq 0) {
                $result
            } else {
                Write-Host "Erreur lors du SELECT: $result" -ForegroundColor Red
            }
            
            Start-Sleep -Milliseconds 500  # Petite pause entre les tables
        }
    }
    catch {
        Write-Host "Erreur: $_" -ForegroundColor Red
    }
}

# Exécution principale
foreach ($container in $containers) {
    $status = docker inspect -f '{{.State.Status}}' $container.Name 2>$null
    
    if ($status -eq "running") {
        Inspect-AllTables $container
        Start-Sleep -Seconds 1  # Pause entre les conteneurs
    }
    else {
        Write-Host "`n[${$container.Name}] Conteneur non démarré (status: $status)" -ForegroundColor Red
    }
}

Write-Host "`nInspection terminée pour tous les conteneurs." -ForegroundColor Green