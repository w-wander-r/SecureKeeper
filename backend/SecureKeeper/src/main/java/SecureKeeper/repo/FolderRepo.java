package SecureKeeper.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SecureKeeper.models.FolderModel;

@Repository
public interface  FolderRepo extends JpaRepository<FolderModel, Long> {
    List<FolderModel> findByUserId(Long userId);
}
