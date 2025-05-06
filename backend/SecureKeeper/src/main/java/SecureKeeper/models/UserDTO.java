package SecureKeeper.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for user authentication operations.
 * 
 * <p>This class serves as a secure container for transferring user credentials between
 * client and server, with special handling for sensitive password data.</p>
 * 
 * <p>Key security features:
 * <ul>
 *   <li>Password field is write-only (never serialized to JSON responses)</li>
 *   <li>Follows the principle of least privilege for credential exposure</li>
 * </ul>
 * </p>
 * 
 * @see com.fasterxml.jackson.annotation.JsonProperty
 */
public class UserDTO {
    /**
     * The username for authentication or registration.
     * Must be unique across the system for registration purposes.
     */
    @NotBlank(message = "Username cannot blank")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers and underscores")
    private String username;

    /**
     * The user's password for authentication.
     * 
     * <p>Annotated with {@code @JsonProperty(access = WRITE_ONLY)} to ensure:
     * <ul>
     *   <li>Password is accepted in request bodies (deserialization)</li>
     *   <li>Password is never included in JSON responses (serialization)</li>
     * </ul>
     * </p>
     * 
     * @see com.fasterxml.jackson.annotation.JsonProperty.Access
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}