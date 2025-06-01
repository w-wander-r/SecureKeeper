package SecureKeeper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SecureKeeper.models.Folder;
import SecureKeeper.models.Note;
import SecureKeeper.repo.NoteRepo;


/**
 * Service layer for note-related operations in the SecureKeeper application.
 * Handles business logic for creating, retrieving, and deleting notes.
 * Works in conjunction with NoteRepository to persist note data.
 */
@Service
public class NoteService {
    @Autowired
    private NoteRepo noteRepo;
    
    @Autowired
    private EncryptionService encryptionService;

    // Create note with encrypted password
    public Note createNote(Note note) {
        String encryptedPassword = encryptionService.encrypt(note.getPassword());
        note.setPassword(encryptedPassword);
        return noteRepo.save(note);
    }

    // Get note (password remains encrypted)
    public Note getEncryptedNote(Long id) {
        return noteRepo.findById(id).orElseThrow();
    }

    // Get decrypted password (only when needed)
    public String getDecryptedPassword(Long noteId) {
        Note note = getEncryptedNote(noteId);
        return encryptionService.decrypt(note.getPassword());
    }

    // Update note with password encryption
    public Note updateNote(Note note) {
        if (note.getPassword() != null) {
            String encryptedPassword = encryptionService.encrypt(note.getPassword());
            note.setPassword(encryptedPassword);
        }
        return noteRepo.save(note);
    }

     public List<Note> getAllNotesByFolder(Folder folder) {
        return noteRepo.findByFolder(folder);
    }

    public void deleteNote(Long id) {
        noteRepo.deleteById(id);
    }
}