package dictionary.mvc.model.service;

import dictionary.mvc.model.entity.Validator;
import dictionary.mvc.model.repository.ValidatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidatorService {
    private final ValidatorRepository repository;

    public ValidatorService(ValidatorRepository repository) {
        this.repository = repository;
    }

    public List<Validator> getAllValidators(){
        return repository.findAll();
    }

    public Validator getValidator(Integer type){
        return repository.getValidatorByType(type);
    }

    public void saveNewValidator(Validator validator){
        repository.save(validator);
    }
}
