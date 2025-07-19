#!/usr/bin/env pwsh

# Configuration
$containers = @(
    @{
        Name = "db-profil";
        Port = 5432;
        DB = "ProfilServiceDB";
        User = "postgres";
        Password = "mypass"
    },
    @{
        Name = "db-inventary";
        Port = 5433;
        DB = "InventaryServiceDB";
        User = "postgres";
        Password = "mypass"
    }
    @{
        Name = "db-order";
        Port = 5434;
        DB = "OrderServiceDB";
        User = "postgres";
        Password = "mypass"
    }
    @{
        Name = "db-payment";
        Port = 5435;
        DB = "PaymentServiceDB";
        User = "postgres";
        Password = "mypass"
    }
)

# Fonction pour lister les tables
function Get-PGTables {
    param (
        $container
    )
    
    Write-Host "`n[${$container.Name}] Connexion a la base ${$container.DB}..." -ForegroundColor Cyan
    
    try {
        $query = @"
SELECT table_schema || '.' || table_name AS table_full_name
FROM information_schema.tables
WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
ORDER BY table_schema, table_name;
"@

        $result = docker exec $container.Name psql -U $container.User -d $container.DB -c $query 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "Tables trouvees dans ${$container.DB}:" -ForegroundColor Green
            $result | Where-Object { $_ -match "\w+\.\w+" } | ForEach-Object {
                Write-Host "  $_" -ForegroundColor Yellow
            }
        } else {
            Write-Host "Erreur: $result" -ForegroundColor Red
        }
    }
    catch {
        Write-Host "Erreur de connexion à ${$container.Name}: $_" -ForegroundColor Red
    }
}

# Exécution pour chaque conteneur
foreach ($container in $containers) {
    # Vérifie que le conteneur est en cours d'exécution
    $status = docker inspect -f '{{.State.Status}}' $container.Name 2>$null
    
    if ($status -eq "running") {
        Get-PGTables $container
        Start-Sleep -Seconds 2  # Pause entre les conteneurs
    }
    else {
        Write-Host "`n[${$container.Name}] Conteneur non trouvé ou non demarre (status: $status)" -ForegroundColor Red
    }
}

Write-Host "`nAnalyse terminée pour tous les conteneurs." -ForegroundColor Green