package SecureKeeper.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for updating folder information.
 * 
 * <p>This record represents the data required to update a folder's name,
 * with built-in validation constraints to ensure data integrity.
 * 
 * <p>Validation rules:
 * <ul>
 *   <li>Folder name cannot be blank</li>
 *   <li>Folder name must be between 1 and 50 characters</li>
 * </ul>
 * 
 * @param name The new name for the folder, subject to validation constraints
 */
public record FolderUpdateDTO(
    @NotBlank(message = "Folder name cannot be blank")
    @Size(min = 1, max = 50 ,message = "Folder name must be between 1 and 50 characters")
    String name
) {}