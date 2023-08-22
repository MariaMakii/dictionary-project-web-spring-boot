package dictionary.mvc.controllers;

import dictionary.mvc.model.entities.Dictionary;
import dictionary.mvc.model.entities.Note;
import dictionary.mvc.model.services.DictionaryService;
import dictionary.mvc.model.services.DictionaryTypeService;
import dictionary.mvc.model.services.NoteService;
import dictionary.mvc.model.services.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    NoteService noteService;

    @Autowired
    ValidatorService validatorService;

    @Autowired
    DictionaryTypeService dictionaryTypeService;

    @GetMapping("/dictionary")
    public String goToDictionary(@RequestParam Integer dictionaryId,
                                 @RequestParam(value = "foundWord", required = false) String foundWord,
                                 @RequestParam(value = "foundDefinition", required = false) String foundDefinition,
                                 Model model) {
        Dictionary dictionary = dictionaryService.getDictionary(dictionaryId);
        model.addAttribute("dictionaryId", dictionaryId);
        model.addAttribute("dictionaryName", dictionary.getTitle());
        model.addAttribute("validator", validatorService.getValidator(dictionary.getType()).getRegex());
        model.addAttribute("notes", noteService.getAll(dictionaryId));
        model.addAttribute("note", new Note());

        Note note = new Note();
        if (foundWord != null) {
            note.setWord(foundWord);
            note.setDefinition(foundDefinition);
        }
        model.addAttribute("foundDefinition", note);

        return "dictionary";
    }

    @PostMapping("/addWord")
    public String addWord(@ModelAttribute Note note, @RequestParam Integer dictionaryId) {
        note.setDictionary(dictionaryId);
        noteService.save(note);
        return "redirect:/dictionary?dictionaryId=" + dictionaryId;
    }

    @DeleteMapping("/deleteWord")
    public String deleteWord(@RequestParam Integer dictionaryId, @RequestParam String word) {
        noteService.delete(word);
        return "redirect:/dictionary?dictionaryId=" + dictionaryId;
    }

    @GetMapping("/findDefinition")
    public String findDefinition(@RequestParam Integer dictionaryId, @RequestParam String word) {
        List<Note> notes = noteService.findNote(word);
        String result;
        if (notes.isEmpty()) {
            result = "Definition not found.";
        } else {
            result = notes.stream().map(Note::getDefinition).collect(Collectors.joining(", "));
        }
        return "redirect:/dictionary?dictionaryId=" + dictionaryId + "&foundWord=" + word + "&foundDefinition=" + result;
    }
}
