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
import SecureKeeper.service.UserService;
import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService service;

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

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

        UserResponseDTO responseDTO = service.register(userDTO);

        logger.info("User registered successfully: " + responseDTO.getUsername());

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        try {
            String token = service.verify(userDTO);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            logger.info("User login successfully: " + userDTO.getUsername());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.info("User login failed: " + userDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}