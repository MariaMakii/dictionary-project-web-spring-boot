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

    public Note getById(int id) {
        return repository.getNoteById(id);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public void delete(String word) {
        repository.deleteByWord(word);
    }

    @Transactional
    public void update(int id, String word, String definition) {
        repository.update(id, word, definition);
    }

    public List<Note> findNote(String word, int dictionary) {
        return repository.findNoteByWord(word, dictionary);
    }

    public List<Note> findNoteByDefinition(String definition, int dictionary) {
        return repository.findNoteByDefinition(definition, dictionary);
    }

    public List<Note> findNoteByDefinition(String definition) {
        return repository.findNoteByDefinition(definition);
    }
}
