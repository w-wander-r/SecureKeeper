package SecureKeeper.models;

/**
 * Data Transfer Object (DTO) for folder information.
 * 
 * <p>This record represents a simplified, serializable view of a {@link Folder} entity,
 * containing only the essential fields needed for client communication.
 * 
 * <p>Provides bidirectional conversion methods between DTO and entity representations:
 * <ul>
 *   <li>{@link #fromEntity(Folder)} - Converts from entity to DTO</li>
 *   <li>{@link #toEntity(FolderDTO, User)} - Converts from DTO to entity</li>
 * </ul>
 * 
 * @param id The unique identifier of the folder
 * @param name The name of the folder
 */
public record FolderDTO(
    Long id,
    String name
) {
    /**
     * Converts a Folder entity to its DTO representation.
     * 
     * @param folder The Folder entity to convert
     * @return FolderDTO containing the entity's essential data
     * @throws NullPointerException if the input folder is null
     */
    public static FolderDTO fromEntity(Folder folder) {
        return new FolderDTO(
            folder.getId(),
            folder.getName()
        );
    }

    /**
     * Converts this DTO to a Folder entity, associating it with the specified user.
     * 
     * @param dto The DTO to convert
     * @param user The user who will own the created folder
     * @return New Folder entity populated with DTO data and associated user
     * @throws NullPointerException if either parameter is null
     */
    public static Folder toEntity(FolderDTO dto, User user) {
        Folder folder = new Folder();
        folder.setName(dto.name());
        folder.setUser(user);
        return folder;
    }
}
