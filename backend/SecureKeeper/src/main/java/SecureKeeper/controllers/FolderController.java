package SecureKeeper.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.Folder;
import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.FolderService;

/* 
*
* Endpoints for post/get/delete methods
*
*/
@RestController
@RequestMapping("api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FolderRepo folderRepo;

    @PostMapping
    public Folder createFolder(@RequestBody Folder folder) {
        // Fetch the user from the database using the user ID
        UsersModel user = userRepo.findById(folder.getUser().getId()).orElse(null);
        
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername);

        if (user == null) throw new RuntimeException("User not found");
        if(!currUser.getId().equals(user.getId())) throw new RuntimeException("You are not allowed to acces this path");

        // Linking user with current folder
        folder.setUser(user);

        return folderService.createFolder(folder);
    }

    // Endpoint to get all folders from current user
    @GetMapping("/user/{userId}")
    public List<Folder> getAllFoldersByUser (@PathVariable Long userId) {
        // Get current user id to check if it match id from url
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername);

        if (!currUser.getId().equals(userId)) throw new RuntimeException("You are not allowed to acces this path");

        return folderService.getAllFoldersByUser(currUser);
    }

    // Endpoint to get a folder
    @GetMapping("/{folderId}")
    public Folder getFolderById(@PathVariable Long folderId) {
        // Get current user id to check if it match id from url
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername);

        Folder folder = folderRepo.findById(folderId).orElse(null);

        if (folder == null || !folder.getUser().getId().equals(currUser.getId())) throw new RuntimeException("You are not allowed to acces this path");

        return folder;
    }

    // Endpoint to delete folder
    @DeleteMapping("/{folderId}")
    public void deleteFolder(@PathVariable Long folderId) {
        
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername);

        Folder folder = folderRepo.findById(folderId).orElse(null);
        if(folder == null || !folder.getUser().getId().equals(currUser.getId())) throw new RuntimeException("You are not allowed to acces this path");

        folderService.deleteFolder(folderId);
    }

    /* 
     * Update method
    */
}