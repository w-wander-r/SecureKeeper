package SecureKeeper.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NoteCreationRequest(
        @NotBlank String title,
        String username,
        String email,
        @NotBlank String password,
        @NotNull Long folderId
) {}
