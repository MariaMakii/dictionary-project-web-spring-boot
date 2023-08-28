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
                                 @RequestParam(value = "searchedDefinition", required = false) String searchedDefinition,
                                 @RequestParam(value = "foundWords", required = false) String foundWords,
                                 @RequestParam(value = "edit", required = false) boolean edit,
                                 @RequestParam(value = "editId", required = false) Integer editId,
                                 Model model) {
        Dictionary dictionary = dictionaryService.getDictionary(dictionaryId);
        model.addAttribute("dictionaryId", dictionaryId);
        model.addAttribute("dictionaryName", dictionary.getTitle());
        model.addAttribute("validator", validatorService.getValidator(dictionary.getType()).getRegex());
        model.addAttribute("notes", noteService.getAll(dictionaryId));
        model.addAttribute("note", new Note());
        model.addAttribute("edit", edit);
        model.addAttribute("editId", editId);

        if (editId != null) {
            Note note = noteService.getById(editId);
            model.addAttribute("editedWord", note.getWord());
            model.addAttribute("editedDefinition", note.getDefinition());
        } else {
            model.addAttribute("editedWord", "");
            model.addAttribute("editedDefinition", "");
        }

        Note note = new Note();
        if (foundWord != null) {
            note.setWord(foundWord);
            note.setDefinition(foundDefinition);
        }
        model.addAttribute("foundDefinition", note);
        model.addAttribute("searchedDefinition", searchedDefinition);
        model.addAttribute("foundWords", foundWords);

        return "dictionary";
    }

    @PostMapping("/addWord")
    public String addWord(@ModelAttribute Note note, @RequestParam Integer dictionaryId) {
        note.setDictionary(dictionaryId);
        noteService.save(note);
        return "redirect:/dictionary?dictionaryId=" + dictionaryId;
    }

    @DeleteMapping("/deleteWord")
    public String deleteWord(@RequestParam Integer dictionaryId, @RequestParam int noteId) {
        noteService.delete(noteId);
        return "redirect:/dictionary?dictionaryId=" + dictionaryId;
    }

    @GetMapping("/findDefinition")
    public String findDefinition(@RequestParam Integer dictionaryId, @RequestParam String word) {
        List<Note> notes = noteService.findNote(word, dictionaryId);
        String result;
        if (notes.isEmpty()) {
            result = "Definition not found.";
        } else {
            result = notes.stream().map(Note::getDefinition).collect(Collectors.joining(", "));
        }
        return "redirect:/dictionary?dictionaryId=" + dictionaryId + "&foundWord=" + word + "&foundDefinition=" + result;
    }

    @GetMapping("/findWord")
    public String findWord(@RequestParam Integer dictionaryId, @RequestParam String searchedDefinition, @RequestParam(required = false) boolean allDictionaries) {
        List<Note> notes;
        if (allDictionaries) {
            notes = noteService.findNoteByDefinition(searchedDefinition);
        } else {
            notes = noteService.findNoteByDefinition(searchedDefinition, dictionaryId);
        }
        String result;
        if (notes.isEmpty()) {
            result = "Words not found.";
        } else {
            result = notes.stream().map(Note::getWord).collect(Collectors.joining(", "));
        }
        return "redirect:/dictionary?dictionaryId=" + dictionaryId + "&searchedDefinition=" + searchedDefinition + "&foundWords=" + result;
    }

    @GetMapping("/openEditor")
    public String editWord(@RequestParam Integer dictionaryId, @RequestParam int editId) {
        return "redirect:/dictionary?dictionaryId=" + dictionaryId + "&edit=" + true + "&editId=" + editId;
    }

    @PostMapping(value = "/edit", params = "action=save")
    public String editSave(@RequestParam Integer dictionaryId, @RequestParam int noteId, @RequestParam String editedDefinition, @RequestParam String editedWord, @RequestParam String action) {
        noteService.update(noteId, editedWord, editedDefinition);
        return "redirect:/dictionary?dictionaryId=" + dictionaryId;
    }

    @PostMapping(value = "/edit", params = "action=cancel")
    public String editCancel(@RequestParam Integer dictionaryId, @RequestParam String action) {
        return "redirect:/dictionary?dictionaryId=" + dictionaryId;
    }

    @PostMapping(value = "/edit")
    public String edit() {
        System.out.println("HERE");
        return null;
    }
}
