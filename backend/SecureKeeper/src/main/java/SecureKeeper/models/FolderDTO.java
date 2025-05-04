package SecureKeeper.models;

/**
 * FolderDTO record hold id, name, userId fields
 * Also it has mapping with methods `fromEntity` and `toEntity`
 */
public record FolderDTO(
    Long id,
    String name
) {
    public static FolderDTO fromEntity(Folder folder) {
        return new FolderDTO(
            folder.getId(),
            folder.getName()
        );
    }

    public static Folder toEntity(FolderDTO dto, UsersModel user) {
        Folder folder = new Folder();
        folder.setName(dto.name());
        folder.setUser(user);
        return folder;
    }
}
