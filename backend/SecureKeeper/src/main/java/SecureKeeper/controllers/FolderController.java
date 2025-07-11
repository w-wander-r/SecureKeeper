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
import SecureKeeper.models.User;
import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.FolderService;
import jakarta.validation.Valid;

/**
 * REST controller for managing folder operations
 * Provides endpoints for creating, retrieving, updating, and deleting folders.
 * All operations can only be performed by the authenticated user
 * who owns the folders.
 * 
 * <p>This controller ensures that:
 * <ul>
 *   <li>Users can only access their own folders</li>
 *   <li>All input is sanitized</li>
 *   <li>Folder operations are validated</li>
 * </ul>
 * 
 * @see FolderService
 * @see FolderDTO
 * @see FolderUpdateDTO
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
    public FolderDTO createFolder(@Valid @RequestBody FolderCreationRequest request) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername).orElseThrow(() -> new RuntimeException("User not found"));
        
        String sanitizedInput = sanitizeInput(request.name());

        Folder folder = new Folder(sanitizedInput, currUser);
        Folder createdFolder = folderService.createFolder(folder);

        return FolderDTO.fromEntity(createdFolder);
    }

    // Endpoint to get all folders from current user
    @GetMapping("/user/{userId}")
    public List<FolderDTO> getAllFoldersByUser(@PathVariable Long userId) {
        // Get current user id to check if it match id from url
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername).orElseThrow(() -> new RuntimeException("User not found"));

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
        User currUser = userRepo.findByUsername(currUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Folder folder = folderRepo.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getUser().getId().equals(currUser.getId())) {
            throw new RuntimeException("You are not allowed to acces this path");
        }

        return FolderDTO.fromEntity(folder);
    }

    // Endpoint to delete folder
    @DeleteMapping("/{folderId}")
    public void deleteFolder(@PathVariable Long folderId) {
        
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Folder folder = folderRepo.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));
        if(!folder.getUser().getId().equals(currUser.getId())) throw new RuntimeException("You are not allowed to acces this path");

        folderService.deleteFolder(folderId);
    }

    // Endpoint to update folder name
    @PatchMapping("/{folderId}")
    public FolderDTO updateFolder(
        @PathVariable Long folderId, 
        @Valid @RequestBody FolderUpdateDTO folderUpdateDTO
    ) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Folder folder = folderRepo.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));
        
        if (!folder.getUser ().getId().equals(currUser .getId())) {
            throw new RuntimeException("You are not allowed to access this path");
        }

        if (folderUpdateDTO.name() != null) folder.setName(folderUpdateDTO.name());

        String sanitizedInput = sanitizeInput(folderUpdateDTO.name());
        folder.setName(sanitizedInput);

        Folder updatedFolder = folderService.createFolder(folder);
        return FolderDTO.fromEntity(updatedFolder);
    }

    /**
     * Sanitizes input by removing potentially dangerous characters.
     * 
     * @param input The string to sanitize
     * @return The sanitized string with HTML tags and quotes removed, or null if input was null
     */
    private String sanitizeInput(String input) {
        if (input == null) { return null; }
        
        return input.replaceAll("[<>\"'']", "");
    }
}