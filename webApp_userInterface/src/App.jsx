import { useEffect, useState } from "react";
import "./App.css";
import axios from "axios";

function App() {
  const [files, setFiles] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [content, setContent] = useState(null);
  const [apiResponse, setApiResponse] = useState(null);
  const [isProcessing, setIsProcessing] = useState(false);
  const [fbAuthError, setFbAuthError] = useState(null);

  const backendUrlInternalNetwork = import.meta.env
    .VITE_API_SERVICE_ORCHESTRE_URL_INTERNAL_NETWORK;
  const backendUrlFromProxy = import.meta.env
    .VITE_API_SERVICE_ORCHESTRE_URL_FROM_PROXY;

  useEffect(() => {
    fetch("/workFlowsScenarios/files.json")
      .then((res) => res.json())
      .then((data) => setFiles(data))
      .catch((err) => console.error("Error loading file list:", err));
  }, []);

  useEffect(() => {
    if (selectedFile) {
      fetch(`/workFlowsScenarios/${selectedFile}`)
        .then((res) => res.json())
        .then((data) => setContent(data))
        .catch((err) => {
          console.error("Error reading JSON file:", err);
          setContent(null);
        });
    }
  }, [selectedFile]);

  const handleCancel = () => {
    setSelectedFile(null);
    setContent(null);
    setApiResponse(null);
  };

  const handleProcess = async () => {
    if (!content || !Array.isArray(content)) {
      alert("Invalid or empty workflow data.");
      return;
    }

    setIsProcessing(true);

    try {
      let response;
      try {
        // 1er essai : via Caddy (localhost:80)
        response = await axios.post(backendUrlFromProxy, content, {
          headers: { "Content-Type": "application/json" },
        });
      } catch (e) {
        console.warn(
          "Échec via Caddy, tentative via backend interne…",
          e.message
        );
        // 2ème essai : accès direct au backend
        response = await axios.post(backendUrlInternalNetwork, content, {
          headers: { "Content-Type": "application/json" },
        });
      }

      setApiResponse(response.data);
      console.log("Réponse backend :", response.data);
    } catch (err) {
      console.error("Erreur d'envoi du workflow :", err);
      alert("Erreur lors du traitement du workflow.");
    } finally {
      setIsProcessing(false);
    }
  };

  const handleFacebookAuth = async () => {
    setFbAuthError(null);
    try {
      const res = await axios.get("/api/profil/facebook/auth_url"); // attention au proxy dans vite.config.js ou config frontend
      const authUrl = res.data.auth_url;
      if (authUrl) {
        window.open(authUrl, "FacebookAuth", "width=600,height=700");
      } else {
        setFbAuthError("URL d'authentification introuvable");
      }
    } catch (err) {
      console.error(
        "Erreur lors de la récupération de l'URL d'auth Facebook:",
        err
      );
      setFbAuthError("Erreur lors de la récupération de l'URL d'auth Facebook");
    }
  };

  const handleBackFromResponse = () => {
    setApiResponse(null);
  };

  return (
    <div className="app-container">
      <h1>Supported Workflows List</h1>

      <div style={{ marginBottom: "20px" }}>
        <button onClick={handleFacebookAuth} className="action-button process">
          Connect with Facebook account
        </button>
        {fbAuthError && <p style={{ color: "red" }}>{fbAuthError}</p>}
      </div>

      {/* État initial - Liste des fichiers */}
      {!selectedFile && !apiResponse && (
        <ul className="file-list">
          {Array.isArray(files) &&
            files.map((file, i) => (
              <li key={i}>
                <button
                  onClick={() => setSelectedFile(file)}
                  className="file-button"
                >
                  {file}
                </button>
              </li>
            ))}
        </ul>
      )}

      {/* État fichier sélectionné */}
      {selectedFile && !apiResponse && (
        <div className="selected-content">
          <h2>Selected File: {selectedFile}</h2>

          {content ? (
            <pre className="json-box">{JSON.stringify(content, null, 2)}</pre>
          ) : (
            <p>Loading file content...</p>
          )}

          <div className="action-buttons">
            <button
              onClick={handleProcess}
              className="action-button process"
              disabled={isProcessing}
            >
              {isProcessing ? "Processing..." : "Process Workflow"}
            </button>
            <button onClick={handleCancel} className="action-button cancel">
              Cancel
            </button>
          </div>
        </div>
      )}

      {/* État réponse API */}
      {apiResponse && (
        <div className="selected-content">
          <h2>API Response</h2>

          <pre className="json-box">{JSON.stringify(apiResponse, null, 2)}</pre>

          <div className="action-buttons">
            <button
              onClick={handleBackFromResponse}
              className="action-button cancel"
            >
              Back
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
