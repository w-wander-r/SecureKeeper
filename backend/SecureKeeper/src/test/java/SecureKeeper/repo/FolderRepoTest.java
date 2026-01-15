package SecureKeeper.repo;

import SecureKeeper.models.Folder;
import SecureKeeper.models.Note;
import SecureKeeper.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL"
})
public class FolderRepoTest {

    @Autowired
    private FolderRepo folderRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void FolderRepo_SaveAll_ReturnSavedFolder() {

        Folder folder = Folder.builder()
                .name("testFolder")
                .user(userRepo.save(User.builder().username("wander").password("wander").build()))
                .notes(null)
                .build();

        Folder savedFolder = folderRepo.save(folder);

        Assertions.assertThat(savedFolder).isNotNull();
        Assertions.assertThat(savedFolder.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateFolder() {
        // Setup
        User user = userRepo.save(User.builder().username("test").password("pass").build());
        Folder folder = folderRepo.save(new Folder("OldName", user)); // AllArgsConstructor enabled

        // Log before
        System.out.println("Before: ID=" + folder.getId() + ", Name=" + folder.getName());

        // Update
        folder.setName("NewName");
        folderRepo.save(folder);

        // Log after
        Folder updated = folderRepo.findById(folder.getId()).get();
        System.out.println("After: ID=" + updated.getId() + ", Name=" + updated.getName());

        // Verify
        Assertions.assertThat(updated.getName()).isEqualTo("NewName");
        Assertions.assertThat(updated.getId()).isEqualTo(folder.getId());
    }
}
