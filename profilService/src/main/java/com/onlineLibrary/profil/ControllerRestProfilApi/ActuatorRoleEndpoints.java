package com.onlineLibrary.profil.ControllerRestProfilApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verif/profil")
public class ActuatorRoleEndpoints {

    @GetMapping("/actuator/adminEndpoint")
    public String adminEndpoint() {
        return "Success access with Admin role";
    }

    @GetMapping("/actuator/userEndpoint")
    public String userEndpoint() {
        return "Success access with User role";
    }
}
