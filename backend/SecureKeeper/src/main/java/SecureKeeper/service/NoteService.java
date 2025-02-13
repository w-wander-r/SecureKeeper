package SecureKeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SecureKeeper.models.Note;
import SecureKeeper.models.Folder;
import SecureKeeper.repo.NoteRepo;

import java.util.List;

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