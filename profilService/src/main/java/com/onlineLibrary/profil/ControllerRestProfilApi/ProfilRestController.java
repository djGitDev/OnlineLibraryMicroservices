package com.onlineLibrary.profil.ControllerRestProfilApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onlineLibrary.profil.Entities.DTO.*;
import com.onlineLibrary.profil.Flux.IProfilServiceDispatcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/profil")
public class ProfilRestController {

    private static final Logger logger = LoggerFactory.getLogger(ProfilRestController.class);

    private final IProfilServiceDispatcher dispatcher;
    private final ObjectMapper objectMapper;
    private ObjectNode userId;


    @Autowired
    public ProfilRestController(IProfilServiceDispatcher dispatcher,ObjectMapper objectMapper) {
        this.dispatcher = dispatcher;
        this.objectMapper = objectMapper;
    }


    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with personal information and address details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Registration",
                                            summary = "Example of a complete registration",
                                            value = "{\n" +
                                                    "  \"action\": \"register\",\n" +
                                                    "  \"user\": {\n" +
                                                    "    \"first_name\": \"Lucas\",\n" +
                                                    "    \"last_name\": \"Dupuis\",\n" +
                                                    "    \"email\": \"lucas.dupuis2@example.com\",\n" +
                                                    "    \"phone\": \"+14165551234\",\n" +
                                                    "    \"password\": \"Passw0rd!2025\"\n" +
                                                    "  },\n" +
                                                    "  \"address\": {\n" +
                                                    "    \"street\": \"789 Rue Sainte-Catherine\",\n" +
                                                    "    \"city\": \"Montreal\",\n" +
                                                    "    \"postal_code\": \"H2X 1Z3\",\n" +
                                                    "    \"province\": \"QC\",\n" +
                                                    "    \"country\": \"Canada\"\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User successfully registered",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"status\": \"success\",\n" +
                                                    "  \"user_id\": 2,\n" +
                                                    "  \"address_id\": 2\n" +
                                                    "}"
                                    )
                            )
                    ),
            }
    )
    @PostMapping(path = "/register")
    public ResponseEntity<JsonNode> registerUser(@RequestBody JsonNode dataJacksson) throws Exception {
        logger.info("start registering user - received data: {}", dataJacksson);
        try {
            RegisterRequestDTO requestDto = objectMapper.treeToValue(dataJacksson, RegisterRequestDTO.class);
            RegisterResponseDTO responseDto = dispatcher.handleRegistration(requestDto);
            JsonNode result = objectMapper.valueToTree(responseDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(errorResponse(e).getBody());
        }
    }


    @Operation(
            summary = "Authentify use",
            description = "Connect user with pass and email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "LoginExample",
                                            summary = "Connexion request example",
                                            value = """
                    {
                      "action": "login",
                      "credentials": {
                        "email": "lucas.dupuis2@example.com",
                        "password": "Passw0rd!2025"
                      }
                    }"""
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "success connexion",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                    {
                      "status": "success",
                      "user_id": 1,
                      "email": "lucas.dupuis2@example.com",
                      "accessToken": 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkam1haWwuZGVudGFsM…DM4fQ.RAh_vzSlbphtzMqLc0e-M4ETDS5YPuwiMS97OXquwBQ'
                    }"""
                                    )
                            )
                    ),
            }
    )
    @PostMapping("/login")
    public ResponseEntity<JsonNode> authentifyUser(@RequestBody JsonNode dataJacksson,
                                          HttpServletResponse response) throws Exception {
        logger.info("check user, connection ... - received data : {}", dataJacksson);
        try {

        JsonNode credentialsNode = dataJacksson.get("credentials");
        LoginRequestDTO loginRequestDTO = objectMapper.treeToValue(credentialsNode, LoginRequestDTO.class);
        LoginResponseDTO login = dispatcher.handleLogin(loginRequestDTO);

        // create  cookie HttpOnly for  refresh token
        Cookie refreshCookie = new Cookie("refreshToken", login.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // if https
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 3600); // 7 days
        response.addCookie(refreshCookie);

        // Body to send only acces token
        ObjectNode result = objectMapper.createObjectNode();
        result.put("status", "success");
        result.put("user_id",login.getUserId());
        result.put("email", login.getEmail());
        result.put("accessToken", login.getJwt());
        result.put("role", login.getRole());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse(e).getBody());
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<JsonNode> refreshToken(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                ObjectNode error = objectMapper.createObjectNode();
                error.put("error", "Refresh token missing");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String refreshToken = null;
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
            if (refreshToken == null ) {
                ObjectNode error = objectMapper.createObjectNode();
                error.put("error", "Invalid refresh token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            RefreshTokenResponseDTO responseDto = dispatcher.handleRefreshToken(refreshToken);
            JsonNode result = objectMapper.valueToTree(responseDto);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            ObjectNode error = objectMapper.createObjectNode();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }


    @Operation(
            summary = "Get user profile",
            description = "Retrieves a user's address information by ID",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user to retrieve",
                            required = true,
                            example = "3",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address information retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            example = """
                    {
                      "street": "789 Rue Sainte-Catherine",
                      "city": "Montreal",
                      "postal_code": "H2X 1Z3",
                      "province": "QC",
                      "country": "Canada"
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping(path = "/{userId}")
    public ResponseEntity<JsonNode> getUserProfil(@PathVariable int userId) throws Exception {
        logger.info("Fetch  profile by user ID: {}", userId);
        try {
            AddressDTO addressDTO = dispatcher.getProfile(userId);
            JsonNode result = objectMapper.valueToTree(addressDTO);

            if (result == null) {
                logger.warn("Profile not found with user ID: {}", userId);
                return ResponseEntity.notFound().build();
            }
            logger.debug("Profile found: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error while fetching user profile - ID: {} - Erreur: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(errorResponse(e).getBody());
        }
    }


    @Operation(
            summary = "Get user data",
            description = "Retrieves a user's personal information by ID",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user to retrieve",
                            required = true,
                            example = "1",
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User data retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            example = """
                    {
                      "id": 1,
                      "firstName": "Lucas",
                      "lastName": "Dupuis",
                      "email": "lucas.dupuis8@example.com",
                      "phone": "+14165551234"
                    }"""
                                    )
                            )
                    )
            }
    )
    @GetMapping(path = "/data/{userId}")
    public ResponseEntity<JsonNode> getUserData(@PathVariable int userId) throws Exception {
        logger.info("Fetch user date with user ID: {}", userId);
        try {
            UserDTO userDTO = dispatcher.getUserData(userId);
            JsonNode result = objectMapper.valueToTree(userDTO);

            if (result == null) {
                logger.warn("Data not found with user ID: {}", userId);
                return ResponseEntity.notFound().build();
            }
            logger.debug("User data found: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error while fetching data by user - ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(errorResponse(e).getBody());
        }
    }

    private ResponseEntity<JsonNode> errorResponse(Exception e) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("error", e.getMessage());
        return ResponseEntity.internalServerError().body(errorNode);
    }

}
