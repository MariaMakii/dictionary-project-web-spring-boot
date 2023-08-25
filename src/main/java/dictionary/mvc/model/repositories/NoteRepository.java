package dictionary.mvc.model.repositories;

import dictionary.mvc.model.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("FROM Note where dictionary = ?1")
    List<Note> findNotesByDictionary(Integer dictionary);

    @Query("FROM Note where word = ?1 AND dictionary = ?2")
    List<Note> findNoteByWord(String word, int dictionary);

    @Modifying
    @Query("DELETE Note n WHERE n.word = ?1")
    void deleteByWord(String word);

    @Query("FROM Note WHERE definition LIKE CONCAT('%', ?1, '%') AND dictionary = ?2")
    List<Note> findNoteByDefinition(String definition, int dictionary);
}
