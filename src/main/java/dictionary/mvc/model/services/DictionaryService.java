package dictionary.mvc.model.services;

import dictionary.mvc.model.repositories.DictionaryRepository;
import dictionary.mvc.model.entities.Dictionary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryService {
    private final DictionaryRepository repository;

    public DictionaryService(DictionaryRepository repository) {
        this.repository = repository;
    }

    public void save(Dictionary dictionary){
        repository.save(dictionary);
    }

    public List<Dictionary> getAll(){
        return repository.findAll();
    }

    public Dictionary getDictionary(Integer id){
        return repository.findDictionaryById(id);
    }
}
