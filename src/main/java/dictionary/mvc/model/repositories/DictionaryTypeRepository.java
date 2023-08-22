package dictionary.mvc.model.repositories;

import dictionary.mvc.model.entities.DictionaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryTypeRepository extends JpaRepository<DictionaryType, Integer> {
}
