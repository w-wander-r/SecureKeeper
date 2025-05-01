package SecureKeeper.models;

public record NoteUpdateDTO(
    String title,
    String username,
    String email,
    String password
) {}
