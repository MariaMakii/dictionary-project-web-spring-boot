package dictionary.mvc.model.services;

import dictionary.mvc.model.entities.DictionaryType;
import dictionary.mvc.model.repositories.DictionaryTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryTypeService {
    private final DictionaryTypeRepository repository;

    public DictionaryTypeService(DictionaryTypeRepository repository) {
        this.repository = repository;
    }

    public void save(DictionaryType type) {
        repository.save(type);
    }

    public List<DictionaryType> getAll() {
        return repository.findAll();
    }
}
