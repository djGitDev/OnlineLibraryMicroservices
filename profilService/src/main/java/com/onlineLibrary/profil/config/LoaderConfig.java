package com.onlineLibrary.profil.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class LoaderConfig {

    private final Properties properties = new Properties();
    private final String profile;
    private final String propertiesFile;

    public LoaderConfig() {
        String envProfile = System.getenv("SPRING_PROFILES_ACTIVE");
        this.profile = (envProfile == null || envProfile.isEmpty()) ? "dev" : envProfile;
        this.propertiesFile = "/application-" + profile + ".properties";
        System.out.println("Loading " + propertiesFile);
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new RuntimeException("Fichier " + propertiesFile + " non trouvé");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du fichier " + propertiesFile, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProfile() {
        return profile;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }
}

