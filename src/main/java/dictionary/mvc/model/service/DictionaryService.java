package dictionary.mvc.model.service;

import dictionary.mvc.model.repository.DictionaryRepository;
import dictionary.mvc.model.entity.Dictionary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryService {
    private final DictionaryRepository repository;

    public DictionaryService(DictionaryRepository repository) {
        this.repository = repository;
    }

    public void saveNewDictionary(Dictionary dictionary){
        repository.save(dictionary);
    }

    public List<Dictionary> getAllDictionaries(){
        return repository.findAll();
    }

    public Dictionary getDictionary(Integer id){
        return repository.findDictionaryById(id);
    }
}
