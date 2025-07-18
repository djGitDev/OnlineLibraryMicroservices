# **Online Library Microservice** 



# 📋 Table of Contents



- [Prerequisites](#prerequisites)

- [Usage](#usage)

- [Available Scripts](#available-scripts)

  

## Prerequisites

### System Requirements
- Windows 10/11 64-bit
- PowerShell 5.1+
- WSL 2 enabled

### Required Software
- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Git (optional)

###  Installation

   **Enable WSL 2** (Run as Administrator):
   ```powershell
   wsl --install
   Install Docker Desktop:
   ```

## Usage

### Prerequisites
- Docker Desktop installed and running
- PowerShell execution enabled
- Project dependencies initialized

### Deployment Steps

1. **Execute the clean deployment script**:
   
   ```powershell
   ./FullClean-Deploy-Docker.ps1
   
   Wait for completion:
   [✓] Containers started successfully
   [✓] Services are healthy
   

2. ##### Open your web browser

   ```powershell
   Access the application:
   Navigate to: http://localhost:80
   ```

## Available Scripts 


#### 🚀Docker Clean & Deploy Script
PowerShell script that checks Docker containers, volumes, and images status before performing complete cleanup and redeployment.

##### Objectives
- Verify current Docker state
- Clean up **only** if resources exist
- Restart services with `docker-compose up -d`

```powershell
Execute at project root (where docker-compose.yml is located)

./FullClean-Deploy-Docker.ps1
```


#### 🐞 Debugging Container Logs Script

PowerShell script that displays logs from all running Docker containers.

##### Objectives
- Lists all running Docker containers

- Displays container logs with coloured output

- Shows container name and ID for identification

- Clean formatting with separators


```powershell
Execute at project root (where script is located)

./Display-Logs.ps1
```



####  🗃️ PostgreSQL Database Inspector Scripts

PowerShell script that displays all created tables and data insertions  from all running Docker postgreSQL containers.

##### Objectives

- Lists all success created tables 
- lists all data insertions 

```powershell
Execute at project root (where script is located)

./Display-DBTables.ps1
./Display-DbTablesContains.ps1

```

