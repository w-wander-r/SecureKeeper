package SecureKeeper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SecureKeeper.models.NoteModel;
import SecureKeeper.repo.NoteRepo;

@Service
public class NoteService {
    
    @Autowired
    private NoteRepo noteRepo;

    // getting notes
    public List<NoteModel> getNotesByFolderId(Long folderid) {
        return noteRepo.findByFolderId(folderid);
    }

    // posting notes
    public NoteModel createNoteModel(NoteModel noteModel) {
        return noteRepo.save(noteModel);
    }
}
