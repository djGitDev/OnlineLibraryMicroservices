#!/usr/bin/env pwsh

# Database container configurations
$containers = @(
    @{
        Name = "db-profil-test";
        Port = 5432;
        DB = "ProfilServiceTestDB";
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
        Name = "db-order-test";
        Port = 5434;
        DB = "OrderServiceTestDB";
        User = "postgres";
        Password = "mypass"
    }
    @{
        Name = "db-payment-test";
        Port = 5435;
        DB = "PaymentServiceTestDB";
        User = "postgres";
        Password = "mypass"
    }
    @{
        Name = "db-cart-test";
        Port = 5436;
        DB = "CartServiceTestDB";
        User = "postgres";
        Password = "mypass"
    }
)

# Function to list database tables
function Get-PGTables {
    param (
        $container
    )
    
    Write-Host "`n[${$container.Name}] Connecting to database ${$container.DB}..." -ForegroundColor Cyan
    
    try {
        $query = @"
SELECT table_schema || '.' || table_name AS table_full_name
FROM information_schema.tables
WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
ORDER BY table_schema, table_name;
"@

        $result = docker exec $container.Name psql -U $container.User -d $container.DB -c $query 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "Tables found in ${$container.DB}:" -ForegroundColor Green
            $result | Where-Object { $_ -match "\w+\.\w+" } | ForEach-Object {
                Write-Host "  $_" -ForegroundColor Yellow
            }
        } else {
            Write-Host "Error: $result" -ForegroundColor Red
        }
    }
    catch {
        Write-Host "Connection error to ${$container.Name}: $_" -ForegroundColor Red
    }
}

# Process each container
foreach ($container in $containers) {
    # Check if container is running
    $status = docker inspect -f '{{.State.Status}}' $container.Name 2>$null
    
    if ($status -eq "running") {
        Get-PGTables $container
        Start-Sleep -Seconds 2  # Pause between containers
    }
    else {
        Write-Host "`n[${$container.Name}] Container not found or not running (status: $status)" -ForegroundColor Red
    }
}

Write-Host "`nAnalysis completed for all containers." -ForegroundColor Green