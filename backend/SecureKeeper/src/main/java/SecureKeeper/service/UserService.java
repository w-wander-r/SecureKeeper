package SecureKeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.UserRepo;

// TODO: split this class (interface + impl)
/**
 * Service layer for user authentication and registration operations.
 * Handles core business logic for user management including password encryption,
 * user registration, and JWT token generation.
 *
 * <p>This service works in conjunction with Spring Security's {@link AuthenticationManager}
 * and uses BCrypt password hashing for secure credential storage.</p>
 *
 * @see UserRepo
 * @see JWTService
 * @see UsersModel
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepo repo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * Registers a new user with encrypted password storage.
     *
     * <p>This method performs the following operations:
     * <ol>
     *   <li>Encrypts the raw password using BCrypt with strength 12</li>
     *   <li>Persists the user entity with encrypted password</li>
     *   <li>Returns the saved user entity</li>
     * </ol>
     * </p>
     *
     * @param user The user model containing registration details (must include username and raw password)
     * @return The persisted user entity with encrypted password
     *
     * @implNote Uses BCrypt password hashing with strength 12 (2^12 iterations)
     */
    public UsersModel register(UsersModel user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    /**
     * Authenticates a user and generates a JWT token upon successful verification.
     *
     * <p>This method performs the following operations:
     * <ol>
     *   <li>Authenticates credentials against stored user data</li>
     *   <li>Generates a JWT token for valid credentials</li>
     *   <li>Returns an error indicator for failed authentication</li>
     * </ol>
     * </p>
     *
     * @param user The user model containing authentication credentials (username and password)
     * @return JWT token string if authentication succeeds, "err" string if authentication fails
     * @throws org.springframework.security.core.AuthenticationException if authentication fails
     *
     * @apiNote The returned token should be included in the Authorization header of subsequent requests
     * @see JWTService#generateToken(String)
     */
    public String verify(UsersModel user) {
        Authentication authentication =
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return authentication.isAuthenticated() ? jwtService.generateToken(user.getUsername()) : "err";
    }

    public boolean usernameExists(String username) {
        return repo.findByUsername(username).isPresent();
    }
}
