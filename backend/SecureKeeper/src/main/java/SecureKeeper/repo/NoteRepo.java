package SecureKeeper.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SecureKeeper.models.NoteModel;

@Repository
public interface NoteRepo extends JpaRepository<NoteModel, Long> {
    List<NoteModel> findByFolderId(Long folderId);
}
