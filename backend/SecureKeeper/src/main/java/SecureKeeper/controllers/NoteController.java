package SecureKeeper.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.NoteRepo;
import SecureKeeper.service.NoteService;


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

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        // Fetch the folder from the database using the folderID
        Folder folder = folderRepo.findById(note.getFolder().getId()).orElse(null);
        
        if (folder == null) {throw new RuntimeException("Folder not found");}

        // Linking user with current folder
        return noteService.createNote(note);
    }

    // Endpoint to get all notes associeted with folder
    @GetMapping("/folder/{folderId}")
    public List<Note> getAllNotesByFolder(@PathVariable Long folderId) {
        Folder folder = new Folder();
        folder.setId(folderId);
        return noteService.getAllNotesByFolder(folder);
    }

    // Endpoint to get a note
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
        // Fetch the note from the database using the noteId
        Note updatedNote = noteRepo.findById(id).orElse(null);
        
        if (updatedNote == null) {throw new RuntimeException("Note not found");}

        if (note.getTitle() != null) updatedNote.setTitle(note.getTitle());
        if (note.getUsername() != null) updatedNote.setUsername(note.getUsername());
        updatedNote.setEmail(note.getEmail());
        if (note.getPassword() != null) updatedNote.setPassword(note.getPassword());


        noteRepo.save(updatedNote);

        return updatedNote;
    }
}