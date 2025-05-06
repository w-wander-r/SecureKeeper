package SecureKeeper.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.Folder;
import SecureKeeper.models.FolderCreationRequest;
import SecureKeeper.models.FolderDTO;
import SecureKeeper.models.FolderUpdateDTO;
import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.FolderService;

// TODO: doc for FolderController, FolderService, FolderDTO, FolderUpdateDTO
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
    public FolderDTO createFolder(@RequestBody FolderCreationRequest request) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername).orElse(null);
        
        Folder folder = new Folder(request.name(), currUser);
        Folder createdFolder = folderService.createFolder(folder);

        return FolderDTO.fromEntity(createdFolder);
    }

    // Endpoint to get all folders from current user
    @GetMapping("/user/{userId}")
    public List<FolderDTO> getAllFoldersByUser(@PathVariable Long userId) {
        // Get current user id to check if it match id from url
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername).orElse(null);

        if (!currUser.getId().equals(userId)) { throw new RuntimeException("You are not allowed to acces this path"); }

        return folderService.getAllFoldersByUser(currUser).stream()
            .map(FolderDTO::fromEntity)
            .collect(Collectors.toList());
    }

    // Endpoint to get a folder
    @GetMapping("/{folderId}")
    public FolderDTO getFolderById(@PathVariable Long folderId) {
        // Get current user id to check if it match id from url
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername).orElse(null);

        Folder folder = folderRepo.findById(folderId).orElse(null);

        if (folder == null || !folder.getUser().getId().equals(currUser.getId())) {
            throw new RuntimeException("You are not allowed to acces this path");
        }

        return FolderDTO.fromEntity(folder);
    }

    // Endpoint to delete folder
    @DeleteMapping("/{folderId}")
    public void deleteFolder(@PathVariable Long folderId) {
        
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername).orElse(null);

        Folder folder = folderRepo.findById(folderId).orElse(null);
        if(folder == null || !folder.getUser().getId().equals(currUser.getId())) throw new RuntimeException("You are not allowed to acces this path");

        folderService.deleteFolder(folderId);
    }

    // Endpoint to update folder name
    @PatchMapping("/{folderId}")
    public FolderDTO updateFolder(
        @PathVariable Long folderId, 
        @RequestBody FolderUpdateDTO folderUpdateDTO
    ) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser  = userRepo.findByUsername(currUsername).orElse(null);

        Folder folder = folderRepo.findById(folderId).orElse(null);
        
        if (folder == null || !folder.getUser ().getId().equals(currUser .getId())) {
            throw new RuntimeException("You are not allowed to access this path");
        }

        if (folderUpdateDTO.name() != null) folder.setName(folderUpdateDTO.name());

        Folder updatedFolder = folderService.createFolder(folder);
        return FolderDTO.fromEntity(updatedFolder);
    }
}