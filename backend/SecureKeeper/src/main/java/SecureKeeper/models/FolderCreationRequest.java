package SecureKeeper.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FolderCreationRequest(
    @NotBlank(message = "Folder name cannot be blank")
    @Size(min = 1, max = 50 ,message = "Folder name must be between 1 and 50 characters")
    String name
) {}
