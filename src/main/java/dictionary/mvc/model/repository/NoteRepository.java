package dictionary.mvc.model.repository;

import dictionary.mvc.model.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    List<Note> findNotesByDictionary(Integer dictionary);

    List<Note> findNotesByWordAndDictionary(String word, Integer dictionary);

    @Modifying
    void deleteByWord(String word);

    @Query("FROM Note WHERE definition LIKE CONCAT('%', ?1, '%') AND dictionary = ?2")
    List<Note> findNoteByDefinition(String definition, Integer dictionary);

    @Query("FROM Note WHERE definition LIKE CONCAT('%', ?1, '%')")
    List<Note> findNoteByDefinition(String definition);

    Note getNoteById(Integer id);

    @Modifying
    @Query("UPDATE Note SET word = ?2, definition = ?3 WHERE id = ?1")
    void update(Integer id, String word, String definition);
}
