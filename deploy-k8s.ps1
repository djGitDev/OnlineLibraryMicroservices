# -----------------------------
# deploy-k8s.ps1
# -----------------------------

# Namespace name
$namespace = "app"

# Folders
$k8sFolder = ".\k8s"
$dbFolder = ".\k8s\db"
$utilFolder = ".\k8s\util"

Write-Host "📦 Deploying Kubernetes resources in namespace '$namespace'..."

# 1️⃣ Check if namespace exists
$nsExists = kubectl get namespace $namespace -o json 2>$null

if (-not $nsExists) {
    Write-Host "🌐 Namespace '$namespace' does not exist. Creating..."
    kubectl apply -f "$k8sFolder\namespace.yaml"
} else {
    Write-Host "✅ Namespace '$namespace' already exists."
}

# Function to apply YAMLs from a given folder
function Apply-YamlsFromFolder($folderPath, $skipFile = "namespace.yaml") {
    if (Test-Path $folderPath) {
        $yamlFiles = Get-ChildItem -Path $folderPath -Filter *.yaml | Where-Object { $_.Name -ne $skipFile }
        foreach ($file in $yamlFiles) {
            Write-Host "`n🔹 Applying $($file.Name) ..."
            kubectl apply -f $file.FullName
        }
    } else {
        Write-Host "⚠️ Folder $folderPath does not exist. Skipping..."
    }
}

# 2️⃣ Apply database YAMLs
Write-Host "`n🗄️ Deploying Database resources..."
Apply-YamlsFromFolder $dbFolder

# 3️⃣ Apply utility YAMLs (Kafka, Caddy, etc.)
Write-Host "`n🛠️ Deploying Utility resources..."
Apply-YamlsFromFolder $utilFolder

# 4️⃣ Apply service YAMLs (main k8s folder)
Write-Host "`n🚀 Deploying Application services..."
Apply-YamlsFromFolder $k8sFolder "namespace.yaml"

Write-Host "`n🎉 All Kubernetes resources deployed successfully in namespace '$namespace'!"