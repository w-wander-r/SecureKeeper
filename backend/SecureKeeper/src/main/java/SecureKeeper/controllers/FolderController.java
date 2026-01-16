package SecureKeeper.controllers;

import SecureKeeper.exceptions.AccessDeniedException;
import SecureKeeper.exceptions.ResourceNotFoundException;
import SecureKeeper.models.Folder;
import SecureKeeper.models.FolderCreationRequest;
import SecureKeeper.models.FolderDTO;
import SecureKeeper.models.FolderUpdateDTO;
import SecureKeeper.models.User;
import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.FolderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<FolderDTO> createFolder(@Valid @RequestBody FolderCreationRequest request) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String sanitizedInput = sanitizeInput(request.name());
        Folder folder = new Folder(sanitizedInput, currUser);
        Folder createdFolder = folderService.createFolder(folder);

        return ResponseEntity.ok(FolderDTO.fromEntity(createdFolder));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FolderDTO>> getAllFoldersByUser(@PathVariable Long userId) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!currUser.getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to access this path");
        }

        List<FolderDTO> folders = folderService.getAllFoldersByUser(currUser).stream()
                .map(FolderDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(folders);
    }

    @GetMapping
    public ResponseEntity<List<FolderDTO>> getCurrentUserFolders() {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<FolderDTO> folders = folderService.getAllFoldersByUser(currUser).stream()
                .map(FolderDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(folders);
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderDTO> getFolderById(@PathVariable Long folderId) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Folder folder = folderRepo.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder", folderId));

        if (!folder.getUser().getId().equals(currUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this folder");
        }

        return ResponseEntity.ok(FolderDTO.fromEntity(folder));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Folder folder = folderRepo.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder", folderId));

        if (!folder.getUser().getId().equals(currUser.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this folder");
        }

        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderDTO> updateFolder(
            @PathVariable Long folderId,
            @Valid @RequestBody FolderUpdateDTO folderUpdateDTO
    ) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currUser = userRepo.findByUsername(currUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Folder folder = folderRepo.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder", folderId));

        if (!folder.getUser().getId().equals(currUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this folder");
        }

        String sanitizedInput = sanitizeInput(folderUpdateDTO.name());
        folder.setName(sanitizedInput);

        Folder updatedFolder = folderService.createFolder(folder);
        return ResponseEntity.ok(FolderDTO.fromEntity(updatedFolder));
    }

    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[<>\"'']", "");
    }
}