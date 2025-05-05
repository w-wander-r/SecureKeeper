package SecureKeeper.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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