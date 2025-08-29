# -----------------------------
# build-and-deploy.ps1
# -----------------------------

# Docker Hub credentials
$dockerUser =
#todo
$dockerPass = 
#todo

# List of service folders (each folder contains its Dockerfile)
$services = @(
    "cartService",
    "paymentService",
    "inventaryService",
    "profilService",
    "orderService",
    "cypressService",
    "notificationService",
    "webapp_interactive"
)

# List of extra official images (DBs, Kafka)
$extraImages = @(
    "bitnamilegacy/kafka",
    "postgres:17-alpine"
)

# -----------------------------
# 1️⃣ Build and push Docker images
# -----------------------------
Write-Host "🔑 Logging in to Docker Hub..."
$dockerPass | docker login -u $dockerUser --password-stdin

foreach ($service in $services) {
    Write-Host "`n🐳 Processing service: $service ..."

    $imageName = "$dockerUser/$($service.ToLower()):latest"

    Push-Location "../$service"

    Write-Host "🔨 Building image $imageName ..."
    docker build -t $imageName .

    Write-Host "⬆️ Pushing image $imageName to Docker Hub..."
    docker push $imageName

    Pop-Location
}

foreach ($img in $extraImages) {
    Write-Host "`n🐳 Pulling and pushing official image: $img ..."
    docker pull $img
    $taggedImg = "$dockerUser/$(($img -replace ':', '-')):latest"
    docker tag $img $taggedImg
    docker push $taggedImg
}

Write-Host "`n✅ All services and extra images processed successfully!"

# -----------------------------
# 2️⃣ Deploy Kubernetes resources
# -----------------------------
# Add minikube to PATH
$env:Path += ";C:\minikube"

Write-Host "`n🔍 Checking Minikube version..."
minikube version

Write-Host "`n🚀 Starting Minikube cluster..."
minikube start --driver=docker

$namespace = "app"
$k8sFolder = "..\k8s"
$dbFolder = "..\k8s\db"
$utilFolder = "..\k8s\util"
$proxyFolder = "..\k8s\proxy"


# Check/create namespace
$nsExists = kubectl get namespace $namespace -o json 2>$null
if (-not $nsExists) {
    Write-Host "🌐 Namespace '$namespace' does not exist. Creating..."
    kubectl apply -f "$k8sFolder\namespace.yaml"
}
else {
    Write-Host "✅ Namespace '$namespace' already exists."
}

# Function to apply YAMLs
function Apply-YamlsFromFolder($folderPath, $skipFile = "namespace.yaml") {
    if (-not (Test-Path $folderPath)) {
        Write-Host "⚠️ Folder $folderPath does not exist. Skipping..."
        return
    }

    $yamlFiles = Get-ChildItem -Path $folderPath -Filter *.yaml | Where-Object { $_.Name -ne $skipFile }

    foreach ($file in $yamlFiles) {
        Write-Host "`n🔹 Processing $($file.Name) ..."

        $yamlContent = Get-Content $file.FullName -Raw

        if ($folderPath -eq $k8sFolder) {
            $yamlContent = $yamlContent -replace '\$\{DOCKER_USER\}', $dockerUser
            Write-Host "✅ Replaced \${DOCKER_USER} with '$dockerUser' in $($file.Name)"
        }
        else {
            Write-Host "ℹ️ Keeping images as is in $($file.Name)"
        }

        $tmpFile = [System.IO.Path]::GetTempFileName()
        Set-Content -Path $tmpFile -Value $yamlContent

        kubectl apply -f $tmpFile

        Remove-Item $tmpFile
    }
}

# Apply YAMLs
Write-Host "`n🗄️ Deploying Database resources..."
Apply-YamlsFromFolder $dbFolder

Write-Host "`n🚀 Deploying Application services..."
Apply-YamlsFromFolder $k8sFolder "namespace.yaml"

Write-Host "`n🛠️ Deploying Utility resources..."
Apply-YamlsFromFolder $utilFolder

Write-Host "`n🛠️ Deploying Reverse proxy at last..."
Apply-YamlsFromFolder $proxyFolder

Write-Host "`n🎉 All Kubernetes resources deployed successfully in namespace '$namespace'!"

# Wait a bit
Start-Sleep -Seconds 10

Write-Host "`n📦 Listing all pods..."
kubectl get pods -A

# Wait for Caddy pod
Write-Host "`n⏳ Waiting for Caddy pod to be ready..."
do {
    $status = kubectl get pod -n app -l app=caddy -o jsonpath='{.items[0].status.phase}'
    Start-Sleep -Seconds 2
} while ($status -ne "Running")

Write-Host "`n📦 Listing all pods..."
kubectl get pods -A

Write-Host "`n🌐 Opening Caddy service in a new terminal..."
Start-Process powershell -ArgumentList "-NoExit", "-Command", "minikube service caddy -n app"

Start-Sleep -Seconds 2

Write-Host "`n🔑 Starting port-forward in a new terminal..."
Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/caddy 80:80 -n app"



