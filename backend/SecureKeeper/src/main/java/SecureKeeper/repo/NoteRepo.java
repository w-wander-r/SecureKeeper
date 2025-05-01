package SecureKeeper.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SecureKeeper.models.Folder;
import SecureKeeper.models.Note;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    // not used
    List<Note> findByFolder(Folder folder);
}