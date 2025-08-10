package com.onlineLibrary.profil.ControllerRestProfilApi;

import com.onlineLibrary.profil.Flux.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profil/facebook")
public class FacebookAuthController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtService jwtService;


    @Value("${facebook.oauth.url}")
    private String oauthUrl;

    @Value("${facebook.oauth.token_url}")
    private String tokenUrl;

    @Value("${facebook.graph.api.url}")
    private String facebookGraphApiUrl;

    @Value("${facebook.client.id}")
    private String clientId;

    @Value("${facebook.client.secret}")
    private String clientSecret;

    @Value("${facebook.redirect.uri}")
    private String redirectUri;

    @Value("${facebook.scope}")
    private String scope;

    public FacebookAuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @GetMapping("/auth_url")
    public ResponseEntity<Map<String, String>> getFacebookAuthUrl() {
        String state = "random_state_token"; // génère un vrai token en production
        String url = oauthUrl + "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&state=" + state +
                "&scope=" + scope;

        Map<String, String> response = new HashMap<>();
        response.put("auth_url", url);
        response.put("state", state);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> facebookCallback(@RequestParam("code") String code) {
        // Construire URL pour récupérer le token
        URI tokenRequestUri = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .build()
                .toUri();

        try {
            // Appeler Facebook pour obtenir le token
            Map<String, Object> tokenResponse = restTemplate.getForObject(tokenRequestUri, Map.class);

            if (tokenResponse != null && tokenResponse.containsKey("access_token")) {
                String accessToken = (String) tokenResponse.get("access_token");
                String graphUrl = facebookGraphApiUrl + accessToken;
                ResponseEntity<Map> graphResponse = restTemplate.getForEntity(graphUrl, Map.class);

                Map<String, Object> userData = graphResponse.getBody();
                String email = (String) userData.get("email");

                String appJwtToken = jwtService.generateToken(email, "ROLE_USER");


                return ResponseEntity.ok(Map.of(
                        "facebook_access_token", accessToken,
                        "app_jwt_token", appJwtToken,
                        "user_email", email
                ));
            } else {
                return ResponseEntity.status(400).body("Erreur lors de la récupération du token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Exception: " + e.getMessage());
        }
    }


    @GetMapping("/delete_data")
    public ResponseEntity<Map<String, String>> fetchDeleteInstructions() {
        // TODO

        Map<String, String> response = new HashMap<>();
        response.put("url", "https://largely-great-terrier.ngrok-free.app/suppression-status"); // page de suivi suppression
        response.put("confirmation_code", "1234567890"); // code arbitraire

        return ResponseEntity.ok(response);
    }

}
