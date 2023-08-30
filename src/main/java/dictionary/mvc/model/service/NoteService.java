package dictionary.mvc.model.service;

import dictionary.mvc.model.entity.Note;
import dictionary.mvc.model.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository repository;

    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public void saveNewNote(Note note) {
        repository.save(note);
    }

    public List<Note> getAllNotes() {
        return repository.findAll();
    }

    public List<Note> getAllNotesInDictionary(Integer dictionary) {
        return repository.findNotesByDictionary(dictionary);
    }

    public Note getById(Integer id) {
        return repository.getNoteById(id);
    }

    @Transactional
    public void deleteNoteById(Integer id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteNoteByWord(String word) {
        repository.deleteByWord(word);
    }

    @Transactional
    public void updateNoteById(Integer id, String word, String definition) {
        repository.update(id, word, definition);
    }

    public List<Note> findNoteByNameInDictionary(String word, Integer dictionary) {
        return repository.findNotesByWordAndDictionary(word, dictionary);
    }

    public List<Note> findNoteByDefinitionInDictionary(String definition, Integer dictionary) {
        return repository.findNoteByDefinition(definition, dictionary);
    }

    public List<Note> findNoteByDefinitionInDictionary(String definition) {
        return repository.findNoteByDefinition(definition);
    }
}
