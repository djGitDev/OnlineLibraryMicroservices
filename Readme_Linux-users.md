# **Online Library Microservice (Linux)**

## 📋 Table of Contents
- [Prerequisites](#prerequisites)
- [Usage](#usage)
- [Available Scripts](#available-scripts)

---

## ⚙️ Prerequisites

### System Requirements
- Linux (Ubuntu 20.04/22.04 or any compatible distribution)
- Bash or Zsh
- Minimum 4 GB RAM

### Required Software
- Docker  
- Docker Compose  

#### Install Docker & Docker Compose:
```bash
sudo apt-get update
sudo apt-get install -y docker.io docker-compose
sudo systemctl enable docker
sudo systemctl start docker
```

*(Optional)* Allow running Docker without `sudo`:
```bash
sudo usermod -aG docker $USER
newgrp docker
```

---

## 📌 Usage

### Prerequisites
- Docker installed and running  
- `docker-compose.yml` located in the project root

### Deployment Steps

1. **Run the full clean deployment script**:
```bash
./FullClean-Deploy-Docker.sh
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

## 🛠️ Available Scripts

### 🚀 Docker Clean & Deploy Script
```bash
./FullClean-Deploy-Docker.sh
```

### 🐞 Display Logs Script
```bash
./Display-Logs.sh
```

### 🗃️ Database Inspector Scripts
```bash
./Display-DBTables.sh
./Display-DbTablesContains.sh
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