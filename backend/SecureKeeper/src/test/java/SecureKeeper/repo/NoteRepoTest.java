package SecureKeeper.repo;

import SecureKeeper.models.Folder;
import SecureKeeper.models.Note;
import SecureKeeper.models.User;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL"
})
public class NoteRepoTest {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FolderRepo folderRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Test
    public void NoteRepo_SaveAll_ReturnSavedNoteInFolder() {
        User user = new User();
        user.setUsername("wander");
        user.setPassword("wander");
        user = userRepo.save(user);

        Folder folder = new Folder();
        folder.setName("testFolder");
        folder.setUser(user);
        folder = folderRepo.save(folder);

        Note note = Note.builder()
                .title("testNote")
                .username("wander")
                .email("t@t.com")
                .password("123")
                .folder(folder)
                .build();

        note = noteRepo.save(note);

        Assertions.assertThat(note).isNotNull();
        Assertions.assertThat(note.getId()).isGreaterThan(0);
        Assertions.assertThat(note.getFolder()).isNotNull();
        Assertions.assertThat(note.getFolder().getId()).isEqualTo(folder.getId());

        Optional<Note> foundNote = noteRepo.findById(note.getId());
        Assertions.assertThat(foundNote).isPresent();
        Assertions.assertThat(foundNote.get().getFolder().getId()).isEqualTo(folder.getId());
    }
}
