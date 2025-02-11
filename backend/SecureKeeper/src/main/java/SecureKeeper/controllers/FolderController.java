package SecureKeeper.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.FolderModel;
import SecureKeeper.service.FolderService;

@RestController
public class FolderController {
    
    @Autowired
    private FolderService folderService;

    // endpoint to get all folders for a user
    @GetMapping("api/users/{userId}/folders")
    public List<FolderModel> getUserFolders(@PathVariable Long userId) {
        return folderService.getFoldersByUserId(userId);
    }
}
