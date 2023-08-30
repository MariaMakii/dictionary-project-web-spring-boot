package dictionary.mvc.model.repository;

import dictionary.mvc.model.entity.Validator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidatorRepository extends JpaRepository<Validator, Integer> {

    Validator getValidatorByType(Integer type);
}
