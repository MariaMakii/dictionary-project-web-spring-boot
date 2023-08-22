package dictionary.mvc.model.services;

import dictionary.mvc.model.entities.Note;
import dictionary.mvc.model.repositories.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository repository;

    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public void save(Note note) {
        repository.save(note);
    }

    public List<Note> getAll() {
        return repository.findAll();
    }

    public List<Note> getAll(int dictionary) {
        return repository.findNotesByDictionary(dictionary);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public void delete(String word) {
        repository.deleteByWord(word);
    }

    public List<Note> findNote(String word){
        return repository.findNoteByWord(word);
    }
}
