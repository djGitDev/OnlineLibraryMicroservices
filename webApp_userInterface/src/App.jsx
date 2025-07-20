import { useEffect, useState } from "react";
import './App.css';

function App() {
    const [files, setFiles] = useState([]);
    const [selectedFile, setSelectedFile] = useState(null);
    const [content, setContent] = useState(null);
    const [apiResponse, setApiResponse] = useState(null); // Nouvel état pour la réponse API
    const [isProcessing, setIsProcessing] = useState(false); // État pour le loading
    const backendUrl = import.meta.env.VITE_API_SERVICE_ORCHESTRE_URL || 'http://localhost:8080/api/workflow';

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
        setApiResponse(null); // Réinitialise aussi la réponse
    };

    const handleProcess = () => {
        if (!content || !Array.isArray(content)) {
            alert("Invalid or empty workflow data.");
            return;
        }

        setIsProcessing(true);
        fetch(backendUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(content),
        })
            .then((res) => res.json())
            .then((data) => {
                setApiResponse(data); // Stocke la réponse
                console.log("Réponse backend :", data);
            })
            .catch((err) => {
                console.error("Erreur d'envoi du workflow :", err);
                alert("Erreur lors du traitement du workflow.");
            })
            .finally(() => {
                setIsProcessing(false);
            });
    };

    const handleBackFromResponse = () => {
        setApiResponse(null); // Permet de revenir à l'affichage précédent
    };

    return (
        <div className="app-container">
            <h1>Supported Workflows List</h1>

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
                        <pre className="json-box">
                            {JSON.stringify(content, null, 2)}
                        </pre>
                    ) : (
                        <p>Loading file content...</p>
                    )}

                    <div className="action-buttons">
                        <button
                            onClick={handleProcess}
                            className="action-button process"
                            disabled={isProcessing}
                        >
                            {isProcessing ? 'Processing...' : 'Process Workflow'}
                        </button>
                        <button
                            onClick={handleCancel}
                            className="action-button cancel"
                        >
                            Cancel
                        </button>
                    </div>
                </div>
            )}

            {/* État réponse API */}
            {apiResponse && (
                <div className="selected-content">
                    <h2>API Response</h2>

                    <pre className="json-box">
                        {JSON.stringify(apiResponse, null, 2)}
                    </pre>

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