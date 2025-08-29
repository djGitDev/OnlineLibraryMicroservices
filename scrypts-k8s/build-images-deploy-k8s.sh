#!/bin/bash
set -e

# -----------------------------
# build-and-deploy.sh
# -----------------------------

# Docker Hub credentials
DOCKER_USER=
#todo

DOCKER_PASS=
#todo

# List of service folders
services=(
    "cartService"
    "paymentService"
    "inventaryService"
    "profilService"
    "orderService"
    "cypressService"
    "notificationService"
    "webapp_interactive"
)

# List of extra official images (DBs, Kafka) — DO NOT PUSH
extraImages=(
    "bitnamilegacy/kafka"
    "postgres:17-alpine"
)

# -----------------------------
# 1️⃣ Build and push Docker images
# -----------------------------
echo "🔑 Logging in to Docker Hub..."
echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin

for service in "${services[@]}"; do
    echo -e "\n🐳 Processing service: $service ..."
    imageName="$DOCKER_USER/$(echo "$service" | tr '[:upper:]' '[:lower:]'):latest"

    pushd "../$service" > /dev/null
    echo "🔨 Building image $imageName ..."
    docker build -t "$imageName" .

    echo "⬆️ Pushing image $imageName to Docker Hub..."
    docker push "$imageName"
    popd > /dev/null
done

# Pull official images (do NOT push)
for img in "${extraImages[@]}"; do
    echo -e "\n🐳 Pulling official image: $img ..."
    docker pull "$img"
done

echo -e "\n✅ All service images built and pushed; official images pulled successfully!"

# -----------------------------
# 2️⃣ Deploy Kubernetes resources
# -----------------------------
namespace="app"
k8sFolder="../k8s"
dbFolder="../k8s/db"
utilFolder="../k8s/util"
proxyFolder="../k8s/proxy"

echo -e "\n🔍 Checking Minikube version..."
minikube version

echo -e "\n🚀 Starting Minikube cluster..."
minikube start --driver=docker

# Check/create namespace
if ! kubectl get namespace "$namespace" >/dev/null 2>&1; then
    echo "🌐 Namespace '$namespace' does not exist. Creating..."
    kubectl apply -f "$k8sFolder/namespace.yaml"
else
    echo "✅ Namespace '$namespace' already exists."
fi

# Function to apply YAMLs
apply_yamls_from_folder() {
    folderPath="$1"
    skipFile="${2:-namespace.yaml}"

    if [ ! -d "$folderPath" ]; then
        echo "⚠️ Folder $folderPath does not exist. Skipping..."
        return
    fi

    for file in "$folderPath"/*.yaml; do
        [ "$(basename "$file")" = "$skipFile" ] && continue
        echo -e "\n🔹 Processing $(basename "$file") ..."

        yamlContent=$(cat "$file")

        if [ "$folderPath" = "$k8sFolder" ]; then
            yamlContent=$(echo "$yamlContent" | sed "s|\${DOCKER_USER}|$DOCKER_USER|g")
            echo "✅ Replaced \${DOCKER_USER} with '$DOCKER_USER' in $(basename "$file")"
        else
            echo "ℹ️ Keeping images as is in $(basename "$file")"
        fi

        tmpFile=$(mktemp)
        echo "$yamlContent" > "$tmpFile"

        kubectl apply -f "$tmpFile"
        rm -f "$tmpFile"
    done
}

echo -e "\n🗄️ Deploying Database resources..."
apply_yamls_from_folder "$dbFolder"

echo -e "\n🚀 Deploying Application services..."
apply_yamls_from_folder "$k8sFolder" "namespace.yaml"

echo -e "\n🛠️ Deploying Utility resources..."
apply_yamls_from_folder "$utilFolder"

echo -e "\n🛠️ Deploying Reverse proxy at last..."
apply_yamls_from_folder "$proxyFolder"

echo -e "\n🎉 All Kubernetes resources deployed successfully in namespace '$namespace'!"

# Wait a bit
sleep 10

echo -e "\n📦 Listing all pods..."
kubectl get pods -A

# Wait for Caddy pod
echo -e "\n⏳ Waiting for Caddy pod to be ready..."
while [[ "$(kubectl get pod -n app -l app=caddy -o jsonpath='{.items[0].status.phase}' 2>/dev/null)" != "Running" ]]; do
    sleep 2
done

echo -e "\n📦 Listing all pods..."
kubectl get pods -A

# Open Caddy in a new terminal
echo -e "\n🌐 Opening Caddy service in a new terminal..."
gnome-terminal -- bash -c "minikube service caddy -n app; exec bash" &

sleep 2

# Start port-forward in another terminal
echo -e "\n🔑 Starting port-forward in a new terminal..."
gnome-terminal -- bash -c "kubectl port-forward svc/caddy 80:80 -n app; exec bash" &