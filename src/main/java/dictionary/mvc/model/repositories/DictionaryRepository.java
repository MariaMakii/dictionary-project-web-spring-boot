package dictionary.mvc.model.repositories;

import dictionary.mvc.model.entities.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {

    @Query("FROM Dictionary WHERE id = ?1")
    public Dictionary findDictionaryById(Integer id);
}
