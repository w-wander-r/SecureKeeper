package SecureKeeper.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.Folder;
import SecureKeeper.models.Note;
import SecureKeeper.models.NoteDTO;
import SecureKeeper.models.NoteUpdateDTO;
import SecureKeeper.models.User;
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
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private FolderRepo folderRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private UserRepo userRepo;

    @PostMapping
    public NoteDTO createNote(@Valid @RequestBody NoteDTO noteDTO) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));
        
        Folder folder = folderRepo.findById(noteDTO.folderId()).orElseThrow(() -> new RuntimeException("Folder not found"));
        
        if (!folder.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this path");
        }

        Note note = NoteDTO.toEntity(
            new NoteDTO(
                noteDTO.id(),
                sanitizeInput(noteDTO.title()),
                sanitizeInput(noteDTO.username()),
                sanitizeInput(noteDTO.password()),
                sanitizeEmail(noteDTO.email()),
                noteDTO.folderId()), 
            folder
        );

        Note createdNote = noteService.createNote(note);
        return NoteDTO.fromEntity(createdNote);
    }

    // Endpoint to get all notes associeted with folder
    @GetMapping("/folder/{folderId}")
    public List<NoteDTO> getAllNotesByFolder(@PathVariable Long folderId) {
        // Get the currently authenticated user
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Folder folder = folderRepo.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getUser().getId().equals(currentUser .getId())) {
            throw new RuntimeException("You are not allowed to acces this path");
        }

        return noteService.getAllNotesByFolder(folder).stream()
            .map(NoteDTO::fromEntity)
            .toList();
    }

    // Endpoint to get a note
    @GetMapping("/{noteId}")
    public NoteDTO getNoteById(@PathVariable Long noteId) {
        // Get current user id to check if it match id from url
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepo.findById(noteId).orElse(null);
        if(note == null) throw new RuntimeException("Note not found");

        Folder folder = note.getFolder();

        if(!folder.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this note");
        }
        
        return NoteDTO.fromEntity(note);
    }

    // Endpoint to delete note
    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepo.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
        if(!note.getFolder().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this note");
        }

        noteService.deleteNote(noteId);
    }

    // Endpoint to update note
    @PutMapping("/{noteId}")
    public NoteDTO updateNote(@PathVariable Long noteId, @RequestBody NoteUpdateDTO updateDTO) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));
        
        Note existingNote = noteRepo.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));

        if(!existingNote.getFolder().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this note");
        }

        existingNote.update(
            updateDTO.title() != null ? sanitizeInput(updateDTO.title()) : existingNote.getTitle(),
            updateDTO.username() != null ? sanitizeInput(updateDTO.username()) : existingNote.getUsername(),
            updateDTO.email() != null ? sanitizeEmail(updateDTO.email()) : existingNote.getEmail(),
            updateDTO.password() != null ? sanitizeInput(updateDTO.password()) : existingNote.getPassword()
        );

        Note updateNote = noteRepo.save(existingNote);
        return NoteDTO.fromEntity(updateNote);
    }

    // TODO: same method in FolderController
    /**
     * Sanitizes input by removing potentially dangerous characters.
     * 
     * @param input The string to sanitize
     * @return Sanitized string with HTML/script tags removed, or null if input was null
     */
    private String sanitizeInput(String input) {
        if(input == null) { return null; }

        return input.replaceAll("[<>\"']", "");
    }

    /**
     * Sanitizes email input by removing potentially dangerous characters.
     * 
     * @param email The email string to sanitize
     * @return Sanitized email string with dangerous characters removed, or null if input was null
     */
    private String sanitizeEmail(String email) {
        if(email == null) { return null; }

        return email.replaceAll("[<>\\\"'\\\\\\\\]", "");
    }
}