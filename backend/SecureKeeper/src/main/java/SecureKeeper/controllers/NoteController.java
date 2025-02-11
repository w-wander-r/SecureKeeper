package SecureKeeper.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.NoteModel;
import SecureKeeper.service.NoteService;


@RestController
public class NoteController {
    
    @Autowired
    private NoteService noteService;

    // endpoint to get all notes from a folder
    @GetMapping("api/folders/{folderId}/notes")
    public List<NoteModel> getNotesByFolder(@PathVariable Long folderId) {
        return noteService.getNotesByFolderId(folderId);
    }
    
    // endpoint to post note
    @PostMapping("/api/folders/{folderId}/notes")
    public NoteModel createNote(@RequestBody NoteModel noteModel) {
        return noteService.createNoteModel(noteModel);
    }
}
