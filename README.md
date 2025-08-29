# **Online Library Microservice**

# 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Deployment](#deployment)
  - [Deploy with Docker Compose](#deploy-with-docker-compose)
  - [Deploy with Kubernetes](#deploy-with-kubernetes)
- [Available Scripts](#available-scripts)
- [End-to-End Workflow Test](#end-to-end-workflow-test)

## Prerequisites

### System Requirements

- Windows 10/11 64-bit
- PowerShell 5.1+
- WSL 2 enabled
- At least 16 GB of RAM recommended for local deployment, especially when using Kubernetes/Minikube

### Required Software

- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Git (optional)

### Installation

**Enable WSL 2** (Run as Administrator):

```powershell
wsl --install
```

Install Docker Desktop.

## Deployment

### 🚀 Deploy with Docker Compose

#### Prerequisites

- Docker Desktop installed and running  
- PowerShell execution enabled  
- Project dependencies initialized  

#### Deployment Steps

1. **Execute the clean deployment script**:

```powershell
./scrypts-docker-compose-powershell/FullClean-Deploy-Docker.ps1
```

Wait for completion:

```
[✓] Containers started successfully
[✓] Services are healthy
```

2. **Open your web browser**:

```powershell
Navigate to: http://localhost:80
```

---

### ☸️ Deploy with Kubernetes

#### Prerequisites

- Minikube or a Kubernetes cluster  `https://minikube.sigs.k8s.io/docs/`
- `kubectl` CLI installed and configured  
- You must have your **own Docker Hub registry**  
  - A valid **Docker Hub username**  
  - A **Docker Hub access token** (instead of password)  
  - Fill in the required `TODO` fields in the deployment script `build-images-deploy-k8s `with your registry, user, and token  

#### Deployment Steps

1. **Execute the build images deployment script**:

   The script will automatically perform the following steps:  

   1. 🔨 Build & push all microservice + official images to your Docker Hub (fill TODO with user/token).  
   2. ☸️ Start Minikube, create namespace, and apply all Kubernetes YAML manifests in order.  
   3. 🌐 Wait for pods (takes at least 8 min – please be patient), then auto port-forward Caddy → access app at http://localhost:80.  

   ```powershell
   ./scrypts-k8s/build-images-deploy-k8s.ps1
   ```

   

2. **Check pods and services**:

```powershell
kubectl get pods -A
kubectl get svc -A
```



Then open:

```
http://localhost:80
```

---

## Available Scripts

### 🚀 Docker Clean & Deploy Script

PowerShell script that checks Docker containers, volumes, and images status before performing complete cleanup and redeployment.

#### Objectives

- Verify current Docker state
- Clean up **only** if resources exist
- Restart services with `docker-compose up -d`

```powershell
Execute at project root (where docker-compose.yml is located)
./scrypts-docker-compose-powershell/FullClean-Deploy-Docker.ps1
```

### 🐞 Debugging Container Logs Script

PowerShell script that displays logs from all running Docker containers.

#### Objectives

- Lists all running Docker containers
- Displays container logs with coloured output
- Shows container name and ID for identification
- Clean formatting with separators

```powershell
Execute at project root (where script is located)
./scrypts-docker-compose-powershell/Display-Logs.ps1
```

### 🗃️ PostgreSQL Database Inspector Scripts

PowerShell scripts that display all created tables and data insertions from all running Docker PostgreSQL containers.

#### Objectives

- Lists all successfully created tables
- Lists all data insertions

```powershell
Execute at project root (where script is located)

./scrypts-docker-compose-powershell/Display-DBTables.ps1
./scrypts-docker-compose-powershell/Display-DbTablesContains.ps1
```

# End-to-End Workflow Test

## Overview

Cypress E2E test that validates the **entire workflow** of the system by verifying **real database states** across all microservices, not just API responses.

## Workflow Coverage

- **User registration**
- **Login**
- **Cart operations** (add items, clear after order)
- **Payment triggering** & **invoice generation**
- **Order creation** & **delivery generation**

## Key Points

### **Primary Validation** – Database-level checks

- **ProfilService**: User creation & profile data
- **CartService**: Cart operations and clearing
- **PaymentService**: Invoice generation
- **OrderService**: Order creation, line items, and deliveries

### **Secondary Validation** – Client-side

- API response received from the **webapp**

## Test Environment Architecture

- Runs in a **dedicated Docker container** on the **internal microservices network**
- Direct access to:
  - **WebApp** (`webapp:5173`) – workflow initiation and UI verification
  - **Microservice DBs**:
    - `dbProfilTest:5432` – user profiles
    - `dbCartTest:5432` – shopping carts
    - `dbPaymentTest:5432` – payments
    - `dbOrderTest:5432` – orders & deliveries

### **Network Isolation**

- Same Docker network as services  
- No external dependencies  
- True inter-service communication validation

## Displaying Cypress UI on Docker Desktop

1. **Install VcXsrv (Windows X Server)**:

```bash
choco install vcxsrv -y
```

2. **Configure environment variable**:

```bash
setx DISPLAY host.docker.internal:0
```

3. **Required variables inside container**:

```bash
DISPLAY=host.docker.internal:0
QT_X11_NO_MITSHM=1
```

## State Management

- All DBs **cleaned before each test**
- **No mocking or stubbing**
- Tests validate **real system state** across all services

## Benefits

- **True end-to-end validation**
- **Data consistency verification** across services
- **Confidence in production-like workflows**
- Eliminates “works on my machine” scenarios

## Verification Matrix

| Component      | Verification Method | Validation Scope           |
| -------------- | ------------------- | -------------------------- |
| Web Interface  | API response check  | Client-side confirmation   |
| ProfilService  | Direct SQL queries  | User record creation       |
| CartService    | Direct SQL queries  | Cart operations & clearing |
| PaymentService | Direct SQL queries  | Invoice generation         |
| OrderService   | Direct SQL queries  | Order & delivery creation  |

