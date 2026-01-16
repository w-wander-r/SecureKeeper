package SecureKeeper.controllers;

import SecureKeeper.models.User;
import SecureKeeper.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private String validJsonRequest;

    @BeforeEach
    void setUp() throws Exception {
        validJsonRequest = """
            {
                "username": "testuser",
                "password": "ValidPass123"
            }
            """;
    }

    @Test
    void register_ValidUser_ReturnsUserDTO() throws Exception {
        // Arrange
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("$2a$12$encryptedHash");

        when(userService.usernameExists("testuser")).thenReturn(false);
        when(userService.register(any(User.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(userService, times(1)).usernameExists("testuser");
        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    void login_ValidCredentials_ReturnsJwtToken() throws Exception {
        // Arrange
        String expectedToken = "generated.jwt.token";
        when(userService.verify(any(User.class))).thenReturn(expectedToken);

        // Act & Assert
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));

        verify(userService, times(1)).verify(any(User.class));
    }

    @Test
    void register_InvalidPasswordTooShort_ReturnsBadRequest() throws Exception {
        // Password less than 8 characters
        String invalidJson = """
            {
                "username": "testuser",
                "password": "123"
            }
            """;

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("Password must be between 8 and 64 characters"));
    }

    @Test
    void register_EmptyPassword_ReturnsBadRequest() throws Exception {
        String emptyPasswordJson = """
            {
                "username": "testuser",
                "password": ""
            }
            """;

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyPasswordJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_MissingPassword_ReturnsBadRequest() throws Exception {
        String missingPasswordJson = """
            {
                "username": "testuser"
            }
            """;

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(missingPasswordJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}