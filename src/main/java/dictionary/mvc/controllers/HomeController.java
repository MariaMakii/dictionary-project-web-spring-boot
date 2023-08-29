package dictionary.mvc.controllers;

import dictionary.mvc.model.services.DictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final DictionaryService dictionaryService;

    public HomeController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("dictionaries", dictionaryService.getAll());
        return "home";
    }
}
