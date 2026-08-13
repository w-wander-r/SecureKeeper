package SecureKeeper.dtos;

import lombok.*;

/**
 * Data Transfer Object for user responses.
 * This DTO is used to send user data back to the client
 * without exposing sensitive information like password and internal ID.
 */
@Getter
@AllArgsConstructor
public class UserResponseDTO {
    private String username;
}