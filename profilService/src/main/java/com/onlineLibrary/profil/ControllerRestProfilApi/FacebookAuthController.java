package com.onlineLibrary.profil.ControllerRestProfilApi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profil/facebook")
public class FacebookAuthController {

    @GetMapping("/callback")
    public ResponseEntity<String> facebookCallback(@RequestParam("code") String code) {
        // TODO
        return ResponseEntity.ok("received code : " + code);
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
