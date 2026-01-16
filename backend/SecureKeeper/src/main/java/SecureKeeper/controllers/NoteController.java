package SecureKeeper.controllers;

import SecureKeeper.exceptions.AccessDeniedException;
import SecureKeeper.exceptions.ResourceNotFoundException;
import SecureKeeper.models.*;
import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.NoteRepo;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.EncryptionService;
import SecureKeeper.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private FolderRepo folderRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EncryptionService encryptionService;

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteCreationRequest request) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Folder folder = folderRepo.findById(request.folderId())
                .orElseThrow(() -> new ResourceNotFoundException("Folder", request.folderId()));

        if (!folder.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this folder");
        }

        // Create new Note
        Note note = new Note();
        note.setTitle(sanitizeInput(request.title()));
        note.setUsername(sanitizeInput(request.username()));
        note.setEmail(sanitizeEmail(request.email()));
        note.setPassword(encryptionService.encrypt(sanitizeInput(request.password())));
        note.setFolder(folder);

        Note createdNote = noteService.createNote(note);
        return ResponseEntity.ok(NoteDTO.fromEntity(createdNote));
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<NoteDTO>> getAllNotesByFolder(@PathVariable Long folderId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Folder folder = folderRepo.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder", folderId));

        if (!folder.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this folder");
        }

        List<NoteDTO> notes = noteService.getAllNotesByFolder(folder).stream()
                .map(NoteDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable Long noteId) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", noteId));

        if (!note.getFolder().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this note");
        }

        // Decrypt password for viewing
        String decryptedPassword = encryptionService.decrypt(note.getPassword());
        NoteDTO noteDTO = new NoteDTO(
                note.getId(),
                note.getTitle(),
                note.getUsername(),
                note.getEmail(),
                decryptedPassword,
                note.getFolder().getId()
        );

        return ResponseEntity.ok(noteDTO);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", noteId));

        if (!note.getFolder().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this note");
        }

        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long noteId,
                                              @Valid @RequestBody NoteUpdateDTO updateDTO) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Note existingNote = noteRepo.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", noteId));

        if (!existingNote.getFolder().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this note");
        }

        // Encrypt new password if provided
        String encryptedPassword = updateDTO.password() != null ?
                encryptionService.encrypt(sanitizeInput(updateDTO.password())) :
                existingNote.getPassword();

        existingNote.update(
                updateDTO.title() != null ? sanitizeInput(updateDTO.title()) : existingNote.getTitle(),
                updateDTO.username() != null ? sanitizeInput(updateDTO.username()) : existingNote.getUsername(),
                updateDTO.email() != null ? sanitizeEmail(updateDTO.email()) : existingNote.getEmail(),
                encryptedPassword
        );

        Note updatedNote = noteRepo.save(existingNote);
        return ResponseEntity.ok(NoteDTO.fromEntity(updatedNote));
    }

    @GetMapping("/{noteId}/safe")
    public ResponseEntity<NoteDTO> getNoteSafe(@PathVariable Long noteId) {
        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", noteId));
        return ResponseEntity.ok(NoteDTO.fromEntity(note));
    }

    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[<>\"']", "");
    }

    private String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.replaceAll("[<>\\\"'\\\\\\\\]", "");
    }
}