package SecureKeeper.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating note information.
 * Represents the updatable fields of a note with validation constraints.
 * All fields are optional except where noted, allowing for partial updates.
 */
public record NoteUpdateDTO(

    @NotBlank(message = "Title cannot be null")
    @Size(min = 1, max = 100, message = "Title must be more than 1 and less than 100 characters")
    String title,
    
    @Size(max = 100, message = "Username must be less than 100 characters")
    String username,

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    String email,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    String password
) {}
