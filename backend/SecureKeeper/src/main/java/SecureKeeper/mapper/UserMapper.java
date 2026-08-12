package SecureKeeper.mapper;

import SecureKeeper.dtos.UserDTO;
import SecureKeeper.dtos.UserResponseDTO;
import SecureKeeper.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for converting between User entity and DTOs.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Converts User entity to UserResponseDTO
     */
    UserResponseDTO toResponseDTO(User user);

    /**
     * Converts UserDTO to User entity
     * Ignores id field as it will be auto-generated
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "folders", ignore = true)
    User toEntity(UserDTO userDTO);

    /**
     * Converts User entity to UserDTO (for internal operations)
     */
    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);
}