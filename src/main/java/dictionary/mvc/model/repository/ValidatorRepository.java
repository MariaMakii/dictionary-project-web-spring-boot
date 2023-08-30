package dictionary.mvc.model.repository;

import dictionary.mvc.model.entity.Validator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidatorRepository extends JpaRepository<Validator, Integer> {

    @Query("SELECT v FROM Validator v WHERE v.type = ?1")
    public Validator getValidatorByType(Integer type);
}
