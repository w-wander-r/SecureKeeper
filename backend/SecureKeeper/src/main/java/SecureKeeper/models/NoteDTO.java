package SecureKeeper.models;

public record NoteDTO(
    Long id,
    String title,
    String username,
    String email,
    String password,
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
