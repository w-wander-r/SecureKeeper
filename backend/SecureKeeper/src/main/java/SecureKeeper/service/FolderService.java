package SecureKeeper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SecureKeeper.models.FolderModel;
import SecureKeeper.repo.FolderRepo;

@Service
public class FolderService {
    
    @Autowired
    private FolderRepo folderRepo;

    public List<FolderModel> getFoldersByUserId(Long userId) {
        return folderRepo.findByUserId(userId);
    }
}
