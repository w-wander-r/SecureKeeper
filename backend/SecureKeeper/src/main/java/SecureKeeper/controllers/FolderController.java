package SecureKeeper.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.Folder;
import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.FolderService;

@RestController
@RequestMapping("api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired UserRepo userRepo;

    @PostMapping
    public Folder createFolder(@RequestBody Folder folder) {
        // Fetch the user from the database using the user ID
        UsersModel user = userRepo.findById(folder.getUser ().getId()).orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        folder.setUser (user); // Set the user for the folder

        return folderService.createFolder(folder);
    }

    @GetMapping("/user/{userId}")
    public List<Folder> getAllFoldersByUser (@PathVariable Long userId) {
        UsersModel user = new UsersModel();
        user.setId(userId);
        return folderService.getAllFoldersByUser (user);
    }

    @GetMapping("/{id}")
    public Folder getFolderById(@PathVariable Long id) {
        return folderService.getFolderById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
    }
}