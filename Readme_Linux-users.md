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