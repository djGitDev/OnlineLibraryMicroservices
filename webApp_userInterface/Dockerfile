# Utilise l'image officielle Node.js 22
FROM node:22

# Crée un dossier pour l'app
WORKDIR /app

# Copie les fichiers package.json et package-lock.json
COPY package*.json ./

# Installe les dépendances
RUN npm install

# Copie le reste du code
COPY . .

# Expose le port Vite
EXPOSE 5173

# Lance le serveur Vite
CMD ["npm", "run", "dev"]