package SecureKeeper.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteDTO(
    Long id,
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
    String password,

    @NotNull(message = "Folder ID cannot be null")
    Long folderId
) {
    public static NoteDTO fromEntity(Note note) {
        return new NoteDTO(
            note.getId(),
            note.getTitle(),
            note.getUsername(),
            note.getEmail(),
            note.getPassword(),
            note.getFolder().getId()
        );
    }

    public static Note toEntity(NoteDTO dto, Folder folder) {
        Note note = new Note();
        note.setId(dto.id());
        note.setTitle(dto.title());
        note.setUsername(dto.username());
        note.setEmail(dto.email());
        note.setPassword(dto.password());
        note.setFolder(folder);
        return note;
    }
}
