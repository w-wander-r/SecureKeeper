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

    public List<NoteModel> getNotesByFolderId(Long folderid) {
        return noteRepo.findByFolderId(folderid);
    }
}
