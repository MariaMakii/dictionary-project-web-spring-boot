package dictionary.mvc.model.services;

import dictionary.mvc.model.entities.Validator;
import dictionary.mvc.model.repositories.ValidatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidatorService {

    @Autowired
    private ValidatorRepository repository;

    public ValidatorService(){

    }

    public ValidatorService(ValidatorRepository repository) {
        this.repository = repository;
    }

    public List<Validator> getAll(){
        return repository.findAll();
    }

    public Validator getValidator(Integer type){
        return repository.getValidatorByType(type);
    }

    public void save(Validator validator){
        repository.save(validator);
    }
}
