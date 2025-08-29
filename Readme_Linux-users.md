# **Online Library Microservice (Linux)**

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Deployment](#deployment)
  - [Deploy with Docker Compose](#deploy-with-docker-compose)
  - [Deploy with Kubernetes](#deploy-with-kubernetes)
- [Available Scripts](#available-scripts)
- [End-to-End Workflow Test](#end-to-end-workflow-test)

---

## ⚙️ Prerequisites

### System Requirements

- Linux 64-bit
- Bash shell
- Docker 20+ installed
- At least 8 GB of RAM recommended for local deployment, especially when using Kubernetes/Minikube

### Required Software

- Docker
- Docker Compose
- Minikube (for Kubernetes)
- `kubectl` CLI

#### Install Docker & Docker Compose:

```bash
sudo apt-get update
sudo apt-get install -y docker.io docker-compose
sudo systemctl enable docker
sudo systemctl start docker
```

_(Optional)_ Allow running Docker without `sudo`:

```bash
sudo usermod -aG docker $USER
newgrp docker
```

---

## Deployment

### 🚀 Deploy with Docker Compose

#### Prerequisites

- Docker installed and running
- `docker-compose.yml` located in the project root

#### Deployment Steps

1. **Run the full clean deployment script**:

```bash
./scripts-docker-compose-bash/FullClean-Deploy-Docker.sh
```

or simply:

```bash
docker compose up -d
```

Wait for completion:

```
[✓] Containers started successfully
[✓] Services are healthy
```

2. **Open your browser**:

```
Go to: http://localhost:80
```

---

### ☸️ Deploy with Kubernetes

#### Prerequisites

- Minikube or a Kubernetes cluster
- `kubectl` CLI installed and configured
- You must have your **own Docker Hub registry**
  - A valid **Docker Hub username**
  - A **Docker Hub access token** (instead of password)
  - Fill in the required `TODO` fields in the deployment script `build-images-deploy-k8s.sh` with your registry, user, and token

#### Deployment Steps

1. **Execute at project root:**

​ The script will automatically perform the following steps:

​ 🔨 Build & push all microservice + official images to your Docker Hub (fill TODO with user/token).

​ ☸️ Start Minikube, create namespace, and apply all Kubernetes YAML manifests in order.

​ 🌐 Wait for pods (takes at least 8 min – please be patient), then auto port-forward Caddy

​ → access app at http://localhost:80.

```bash
./scripts-k8s/build-images-deploy-k8s.sh

```

2. **Check pods and services**:

```
kubectl get pods -A
kubectl get svc -A
```

Then open:

```
http://localhost:80
```

## 🛠️ Available Scripts

### 🚀 Docker Clean & Deploy Script

```bash
./scripts-docker-compose-bash/FullClean-Deploy-Docker.sh
```

### 🐞 Display Logs Script

```bash
./scripts-docker-compose-bash/Display-Logs.sh
```

### 🗃️ Database Inspector Scripts

```bash
./scripts-docker-compose-bash/Display-DBTables.sh
./scripts-docker-compose-bash/Display-DbTablesContains.sh
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
    - `db-profil-test:5432` – user profiles
    - `db-cart-test:5432` – shopping carts
    - `db-payment-test:5432` – payments
    - `db-order-test:5432` – orders & deliveries

### **Network Isolation**

- Same Docker network as services
- No external dependencies
- True inter-service communication validation

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
