package SecureKeeper.service;

import SecureKeeper.dtos.UserDTO;
import SecureKeeper.dtos.UserResponseDTO;
import SecureKeeper.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SecureKeeper.models.User;
import SecureKeeper.repo.UserRepo;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public UserResponseDTO register(UserDTO userDTO) {
        // Hash password
        String hashedPWD = encoder.encode(userDTO.getPassword());

        // Create user with hashed password
        User userToSave = User.createNewUser(userDTO.getUsername(), hashedPWD);

        // Save to database
        User savedUser = repo.save(userToSave);

        // Return safe DTO
        return userMapper.toResponseDTO(savedUser);
    }

    public String verify(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
        );

        return authentication.isAuthenticated()
                ? jwtService.generateToken(userDTO.getUsername())
                : "no token found";
    }

    public boolean usernameExists(String username) {
        return repo.findByUsername(username).isPresent();
    }
}