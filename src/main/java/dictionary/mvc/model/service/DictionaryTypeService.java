package dictionary.mvc.model.service;

import dictionary.mvc.model.entity.DictionaryType;
import dictionary.mvc.model.repository.DictionaryTypeRepository;
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
