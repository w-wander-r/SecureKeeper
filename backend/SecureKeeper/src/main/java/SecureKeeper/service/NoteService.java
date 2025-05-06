package SecureKeeper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SecureKeeper.models.Folder;
import SecureKeeper.models.Note;
import SecureKeeper.repo.NoteRepo;

@Service
public class NoteService {

    @Autowired
    private NoteRepo noteRepo;

    public Note createNote(Note note) {
        return noteRepo.save(note);
    }

    public List<Note> getAllNotesByFolder(Folder folder) {
        return noteRepo.findByFolder(folder);
    }

    public Note getNoteById(Long id) {
        return noteRepo.findById(id).orElse(null);
    }

    public void deleteNote(Long id) {
        noteRepo.deleteById(id);
    }
}