package SecureKeeper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SecureKeeper.models.Folder;
import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.FolderRepo;

/**
 * Service layer for folder-related operations.
 * Handles business logic for folder creation, retrieval, and deletion.
 * 
 * <p>This service works with the {@link FolderRepo} to persist and retrieve folder data,
 * and provides the following functionality:
 * <ul>
 *   <li>Create new folders</li>
 *   <li>Retrieve folders by user or ID</li>
 *   <li>Delete existing folders</li>
 * </ul>
 * 
 * @see FolderRepo
 */
@Service
public class FolderService {

    @Autowired
    private FolderRepo folderRepo;

    public Folder createFolder(Folder folder) {
        return folderRepo.save(folder);
    }

    public List<Folder> getAllFoldersByUser(UsersModel user) {
        return folderRepo.findByUser(user);
    }

    public Folder getFolderById(Long id) {
        return folderRepo.findById(id).orElse(null);
    }

    public void deleteFolder(Long id) {
        folderRepo.deleteById(id);
    }
}