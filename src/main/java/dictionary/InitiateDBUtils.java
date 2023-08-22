package dictionary;

import dictionary.mvc.model.entities.Dictionary;
import dictionary.mvc.model.entities.DictionaryType;
import dictionary.mvc.model.entities.Validator;
import dictionary.mvc.model.services.DictionaryService;
import dictionary.mvc.model.services.DictionaryTypeService;
import dictionary.mvc.model.services.ValidatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class InitiateDBUtils implements CommandLineRunner {

    private final DictionaryService dictionaryService;
    private final DictionaryTypeService dictionaryTypeService;
    private final ValidatorService validatorService;

    Pattern latinPattern = Pattern.compile("[a-zA-Z]{4}");
    Pattern numbersPattern = Pattern.compile("\\d{5}");

    public InitiateDBUtils(DictionaryService dictionaryService, DictionaryTypeService dictionaryTypeService, ValidatorService validatorService) {
        this.dictionaryService = dictionaryService;
        this.dictionaryTypeService = dictionaryTypeService;
        this.validatorService = validatorService;
    }

    @Override
    public void run(String... args) {
        System.out.println("run");

//        List<DictionaryType> types = dictionaryTypeService.getAll();
//        if (types.isEmpty()) {
//            DictionaryType type = new DictionaryType();
//            type.setType("LATIN");
//
//            DictionaryType type2 = new DictionaryType();
//            type2.setType("NUMBERS");
//
//            dictionaryTypeService.save(type);
//            dictionaryTypeService.save(type2);
//        }
//
//        List<Dictionary> dictionaries = dictionaryService.getAll();
//        if (dictionaries.isEmpty()) {
//            types = dictionaryTypeService.getAll();
//            types.forEach(type -> {
//                Dictionary dictionary = new Dictionary();
//                dictionary.setType(type.getId());
//                dictionary.setTitle("dictionary " + type.getType());
//                dictionaryService.save(dictionary);
//            });
//        }
//
//        List<Validator> validators = validatorService.getAll();
//        if (validators.isEmpty()) {
//            types = dictionaryTypeService.getAll();
//            types.forEach(type -> {
//                Validator validator = new Validator();
//                String regex = null;
//                validator.setType(type.getId());
//                if (type.getType().equals("LATIN")) {
//                    regex = latinPattern.pattern();
//                } else if (type.getType().equals("NUMBERS")) {
//                    regex = numbersPattern.pattern();
//                }
//                validator.setRegex(regex);
//                validatorService.save(validator);
//            });
//        }
    }
}
