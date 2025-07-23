#!/usr/bin/env pwsh

# Container configurations
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
    @{
        Name = "db-cart";
        DB = "CartServiceDB";
        User = "postgres";
        Password = "mypass"
    }
)

# Function to list and inspect all tables
function Inspect-AllTables {
    param (
        $container
    )
    
    Write-Host "`n[${$container.Name}] Inspecting database ${$container.DB}..." -ForegroundColor Cyan
    
    try {
        # 1. List all tables
        $tables = docker exec $container.Name psql -U $container.User -d $container.DB -c "
            SELECT table_schema || '.' || table_name AS full_table_name
            FROM information_schema.tables
            WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
            ORDER BY table_schema, table_name;
        " 2>&1

        if ($LASTEXITCODE -ne 0) {
            Write-Host "Error retrieving tables: $tables" -ForegroundColor Red
            return
        }

        $tablesList = $tables | Where-Object { $_ -match "\w+\.\w+" }
        
        if (-not $tablesList) {
            Write-Host "No tables found in this database" -ForegroundColor Yellow
            return
        }

        # 2. For each table, perform a SELECT
        foreach ($table in $tablesList) {
            Write-Host "`nTable: $table" -ForegroundColor Green
            Write-Host "------------------------" -ForegroundColor Green
            
            $query = "SELECT * FROM $table LIMIT 100;"
            $result = docker exec $container.Name psql -U $container.User -d $container.DB -c $query 2>&1
            
            if ($LASTEXITCODE -eq 0) {
                $result
            } else {
                Write-Host "SELECT error: $result" -ForegroundColor Red
            }
            
            Start-Sleep -Milliseconds 500  # Small pause between tables
        }
    }
    catch {
        Write-Host "Error: $_" -ForegroundColor Red
    }
}

# Main execution
foreach ($container in $containers) {
    $status = docker inspect -f '{{.State.Status}}' $container.Name 2>$null
    
    if ($status -eq "running") {
        Inspect-AllTables $container
        Start-Sleep -Seconds 1  # Pause between containers
    }
    else {
        Write-Host "`n[${$container.Name}] Container not running (status: $status)" -ForegroundColor Red
    }
}

Write-Host "`nInspection completed for all containers." -ForegroundColor Green