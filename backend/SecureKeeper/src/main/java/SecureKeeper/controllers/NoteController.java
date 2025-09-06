package SecureKeeper.controllers;

import java.util.List;

import SecureKeeper.models.*;
import SecureKeeper.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.NoteRepo;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.NoteService;
import jakarta.validation.Valid;

// TODO: doc for NoteController, NoteService, NoteDTO, NoteUpdateDTO

/**
 * REST controller for managing notes in the SecureKeeper application.
 * Provides endpoints for creating, retrieving, updating, and deleting notes.
 * All operations are secured and require authentication, with checks to ensure
 * users can only access their own notes.
 *
 * @see NoteService
 * @see NoteDTO
 * @see NoteUpdateDTO
 */
@RestController
@RequestMapping("api/notes")
//@CrossOrigin(origins = "*")
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
    public NoteDTO createNote(@Valid @RequestBody NoteCreationRequest request) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Folder folder = folderRepo.findById(request.folderId())
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this path");
        }

        // Create new Note directly instead of using NoteDTO
        Note note = new Note();
        note.setTitle(sanitizeInput(request.title()));
        note.setUsername(sanitizeInput(request.username()));
        note.setEmail(sanitizeEmail(request.email()));
        note.setPassword(encryptionService.encrypt(sanitizeInput(request.password())));
        note.setFolder(folder);

        Note createdNote = noteService.createNote(note);
        return NoteDTO.fromEntity(createdNote);
    }

    // Endpoint to get all notes associated with folder
    @GetMapping("/folder/{folderId}")
    public List<NoteDTO> getAllNotesByFolder(@PathVariable Long folderId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Folder folder = folderRepo.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to acces this path");
        }

        return noteService.getAllNotesByFolder(folder).stream()
                .map(NoteDTO::fromEntity)
                .toList();
    }

    // Endpoint to get a note
    @GetMapping("/{noteId}")
    public NoteDTO getNoteById(@PathVariable Long noteId) throws Exception{
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepo.findById(noteId).orElse(null);
        String decryptedPassword = encryptionService.decrypt(note.getPassword());
        if (note == null) throw new RuntimeException("Note not found");

        Folder folder = note.getFolder();

        if (!folder.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this note");
        }

        return new NoteDTO(
                note.getId(),
                note.getTitle(),
                note.getUsername(),
                note.getEmail(),
                decryptedPassword,
                note.getFolder().getId()
        );
    }

    // Endpoint to delete note
    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepo.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getFolder().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this note");
        }

        noteService.deleteNote(noteId);
    }

    // Endpoint to update note
    @PutMapping("/{noteId}")
    public NoteDTO updateNote(@PathVariable Long noteId, @RequestBody NoteUpdateDTO updateDTO) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Note existingNote = noteRepo.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));

        if (!existingNote.getFolder().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this note");
        }

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
        return NoteDTO.fromEntity(updatedNote);
    }

    @GetMapping("/{noteId}/safe")
    public NoteDTO getNoteSafe(@PathVariable Long noteId) {
        return NoteDTO.fromEntity(noteRepo.findById(noteId).orElseThrow());
    }

    // TODO: same method in FolderController

    /**
     * Sanitizes input by removing potentially dangerous characters.
     *
     * @param input The string to sanitize
     * @return Sanitized string with HTML/script tags removed, or null if input was null
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        return input.replaceAll("[<>\"']", "");
    }

    /**
     * Sanitizes email input by removing potentially dangerous characters.
     *
     * @param email The email string to sanitize
     * @return Sanitized email string with dangerous characters removed, or null if input was null
     */
    private String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }

        return email.replaceAll("[<>\\\"'\\\\\\\\]", "");
    }
}