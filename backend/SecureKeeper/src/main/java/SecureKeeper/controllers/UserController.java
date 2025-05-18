package SecureKeeper.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.UserDTO;
import SecureKeeper.models.User;
import SecureKeeper.service.UserService;
import jakarta.validation.Valid;

/**
 * Controller handling user authentication operations including registration and login.
 * 
 * <p>Cross-origin requests are allowed for development purposes.</p>
 * 
 * @see UserService
 * @see User
 * @see UserDTO
 */
@RestController
@CrossOrigin(origins = "*")
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
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            
            return ResponseEntity.badRequest().body(errors);
        }

        if (service.usernameExists(userDTO.getUsername())) {
            return ResponseEntity
                .badRequest()
                .body("Username already exists");
        }
        
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        User savedUser = service.register(user);

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
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            
            return ResponseEntity.badRequest().body(errors);
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        try {
            return ResponseEntity.ok(service.verify(user));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}