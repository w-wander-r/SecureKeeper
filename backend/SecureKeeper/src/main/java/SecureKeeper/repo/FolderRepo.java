package SecureKeeper.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SecureKeeper.models.Folder;
import SecureKeeper.models.UsersModel;

@Repository
public interface FolderRepo extends JpaRepository<Folder, Long> {
    List<Folder> findByUser(UsersModel user);
}