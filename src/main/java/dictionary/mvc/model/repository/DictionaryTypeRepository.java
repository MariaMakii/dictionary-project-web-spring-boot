package dictionary.mvc.model.repository;

import dictionary.mvc.model.entity.DictionaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryTypeRepository extends JpaRepository<DictionaryType, Integer> {
}
