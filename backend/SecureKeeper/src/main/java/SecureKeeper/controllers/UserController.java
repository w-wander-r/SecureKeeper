package SecureKeeper.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import SecureKeeper.dtos.UserDTO;
import SecureKeeper.dtos.UserResponseDTO;
import SecureKeeper.mapper.UserMapper;
import SecureKeeper.models.User;
import SecureKeeper.service.UserService;
import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    /**
     * Registers a new user in the system.
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

        // Map DTO to Entity
        User user = userMapper.toEntity(userDTO);

        // Save user
        User savedUser = service.register(user);

        // Map Entity to ResponseDTO (without sensitive data)
        UserResponseDTO responseDTO = userMapper.toResponseDTO(savedUser);

        logger.info("User registered successfully: " + responseDTO.getUsername());

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Authenticates a user and generates an access token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        // Map DTO to Entity for verification
        User user = userMapper.toEntity(userDTO);

        try {
            String token = service.verify(user);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            logger.info("User login successfully");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.info("User login failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}