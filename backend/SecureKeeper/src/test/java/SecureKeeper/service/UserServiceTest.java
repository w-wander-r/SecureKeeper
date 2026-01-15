package SecureKeeper.service;

import SecureKeeper.models.User;
import SecureKeeper.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("plainPassword123");
    }

    // ============ REGISTRATION TESTS ============

    @Test
    void register_ValidUser_EncryptsPasswordAndSaves() {
        // Arrange
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // Act
        User result = userService.register(testUser);

        // Assert
        // 1. Verify password was encrypted (not plain text)
        assertThat(result.getPassword())
                .isNotEqualTo("plainPassword123")  // Not the original
                .isNotNull()
                .startsWith("$2a$12$");  // BCrypt hash format

        // 2. Verify save was called once
        verify(userRepo, times(1)).save(any(User.class));

        // 3. Verify the saved user has encrypted password
        verify(userRepo).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getPassword())
                .isNotEqualTo("plainPassword123")
                .startsWith("$2a$12$");

        System.out.println(capturedUser.getPassword());
    }

    @Test
    void register_ValidUser_ReturnsSavedUserWithId() {
        // Arrange
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("$2a$12$encryptedHash");  // Simulated encrypted password

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.register(testUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepo, times(1)).save(any(User.class));
    }

    // ============ USERNAME EXISTS TESTS ============

    @Test
    void usernameExists_UsernameExists_ReturnsTrue() {
        // Arrange
        when(userRepo.findByUsername("existinguser"))
                .thenReturn(Optional.of(new User()));

        // Act
        boolean exists = userService.usernameExists("existinguser");

        // Assert
        assertThat(exists).isTrue();
        verify(userRepo, times(1)).findByUsername("existinguser");

        System.out.println(userRepo.findByUsername("existinguser"));
    }

    @Test
    void usernameExists_UsernameDoesNotExist_ReturnsFalse() {
        // Arrange
        when(userRepo.findByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        // Act
        boolean exists = userService.usernameExists("nonexistent");

        // Assert
        assertThat(exists).isFalse();
        verify(userRepo, times(1)).findByUsername("nonexistent");
    }

    // ============ VERIFICATION/AUTHENTICATION TESTS ============

    @Test
    void verify_ValidCredentials_ReturnsJwtToken() {
        // Arrange
        String expectedToken = "generated.jwt.token";
        Authentication mockAuth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken("testuser")).thenReturn(expectedToken);

        // Act
        String token = userService.verify(testUser);

        // Assert
        assertThat(token).isEqualTo(expectedToken);
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("testuser");
    }

    @Test
    void verify_InvalidCredentials_ReturnsErrorString() {
        // Arrange
        Authentication mockAuth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(mockAuth.isAuthenticated()).thenReturn(false);

        // Act
        String result = userService.verify(testUser);

        // Assert
        assertThat(result).isEqualTo("err");
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void verify_AuthenticationManagerThrowsException_PropagatesException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.core.AuthenticationException("Invalid credentials") {});

        // Act & Assert
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                        userService.verify(testUser))
                .isInstanceOf(org.springframework.security.core.AuthenticationException.class)
                .hasMessageContaining("Invalid credentials");
    }
}