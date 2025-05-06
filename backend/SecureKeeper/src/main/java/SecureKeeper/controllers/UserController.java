package SecureKeeper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.UserDTO;
import SecureKeeper.models.UsersModel;
import SecureKeeper.service.UserService;

// TODO: validation
/**
 * Controller handling user authentication operations including registration and login.
 * 
 * <p>Cross-origin requests are allowed from http://localhost:8080 for development purposes.</p>
 * 
 * @see UserService
 * @see UsersModel
 * @see UserDTO
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    private UserService service;

    /**
     * Registers a new user in the system.
     * 
     * <p>This endpoint accepts user credentials and creates a new user account.
     * The password hashed before storage by the UserService.</p>
     * 
     * @param userDTO Data Transfer Object containing username and password for registration
     * @return UserDTO containing the registered user's information (excluding sensitive data)
     * 
     * @apiNote Example request body in JSON:
     * <pre>
     * {
     *     "username": "user",
     *     "password": "password"
     * }
     * </pre>
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        
        if(service.usernameExists(userDTO.getUsername())) {
            return ResponseEntity.
                badRequest()
                .body("Username already exists");
        }
        
        UsersModel user = new UsersModel();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        UsersModel savedUser = service.register(user);

        UserDTO savedUserDTO = new UserDTO();
        savedUserDTO.setUsername(savedUser.getUsername());
        savedUserDTO.setPassword(savedUser.getPassword());

        return ResponseEntity.ok(userDTO);
    }

    /**
     * Authenticates a user and generates an access token.
     * 
     * <p>This endpoint verifies user credentials and returns an authentication token
     * upon successful verification. The token should be used for subsequent authenticated requests.</p>
     * 
     * @param userDTO Data Transfer Object containing username and password for authentication
     * @return String containing the JWT token or session identifier upon successful authentication
     * @throws SecurityException if authentication fails due to invalid credentials
     * @throws RuntimeException if there's an issue during the authentication process
     * 
     * @apiNote Example request body:
     * <pre>
     * {
     *     "username": "existingUser",
     *     "password": "correctPassword"
     * }
     * </pre>
     */
    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        UsersModel user = new UsersModel();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        return service.verify(user);
    }
}