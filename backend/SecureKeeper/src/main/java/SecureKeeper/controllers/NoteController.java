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
import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.NoteRepo;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.NoteService;

// TODO: implement DTO
/* 
 * 
 * Endpoints for post/get/delete methods
 *
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
    public NoteDTO createNote(@RequestBody NoteDTO noteDTO) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currentUser = userRepo.findByUsername(currentUsername);
        
        Folder folder = folderRepo.findById(noteDTO.folderId()).orElse(null);
        
        if (folder == null || !folder.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to access this path");
        }

        Note note = NoteDTO.toEntity(noteDTO, folder);
        Note createdNote = noteService.createNote(note);
        return NoteDTO.fromEntity(createdNote);
    }

    // Endpoint to get all notes associeted with folder
    @GetMapping("/folder/{folderId}")
    public List<Note> getAllNotesByFolder(@PathVariable Long folderId) {
        // Get the currently authenticated user
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currentUser  = userRepo.findByUsername(currentUsername);

        Folder folder = folderRepo.findById(folderId).orElse(null);
        
        if (folder == null || !folder.getUser().getId().equals(currentUser .getId())) throw new RuntimeException("You are not allowed to acces this path");

        return noteService.getAllNotesByFolder(folder);
    }

    // Endpoint to get a note
    @GetMapping("/{noteId}")
    public Note getNoteById(@PathVariable Long noteId) {
        // Get current user id to check if it match id from url
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername);

        Note note = noteRepo.findById(noteId).orElse(null);
        if(note == null) throw new RuntimeException("Note not found");

        Folder folder = note.getFolder();

        if(folder == null || !folder.getUser().getId().equals(currUser.getId())) throw new RuntimeException("You are not allowed to access this note");
        
        return note;
    }

    // Endpoint to delete note
    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername);

        Note note = noteRepo.findById(noteId).orElse(null);
        if(note == null || !note.getFolder().getUser().getId().equals(currUser.getId())) throw new RuntimeException("You are not allowed to access this note");

        noteService.deleteNote(noteId);
    }

    // Endpoint to update note
    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId, @RequestBody Note note) {
        String currUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersModel currUser = userRepo.findByUsername(currUsername);
        
        // Current note will be updated in `updateNote` variable
        Note updatedNote = noteRepo.findById(noteId).orElse(null);

        if(updatedNote == null || !updatedNote.getFolder().getUser().getId().equals(currUser.getId())) throw new RuntimeException("You are not allowed to access this note");
        
        updatedNote.update(note.getTitle(), note.getUsername(), note.getEmail(), note.getPassword());

        noteRepo.save(updatedNote);

        return updatedNote;
    }
}