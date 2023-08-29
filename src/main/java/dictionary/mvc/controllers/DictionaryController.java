package dictionary.mvc.controllers;

import dictionary.mvc.dto.DictionaryPageDTO;
import dictionary.mvc.model.entities.Dictionary;
import dictionary.mvc.model.entities.Note;
import dictionary.mvc.model.services.DictionaryService;
import dictionary.mvc.model.services.NoteService;
import dictionary.mvc.model.services.ValidatorService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DictionaryController {
    private final DictionaryService dictionaryService;

    private final NoteService noteService;

    private final ValidatorService validatorService;

    private final DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();

    public DictionaryController(DictionaryService dictionaryService, NoteService noteService, ValidatorService validatorService) {
        this.dictionaryService = dictionaryService;
        this.noteService = noteService;
        this.validatorService = validatorService;
    }

    @GetMapping("/dictionary/{id}")
    public String goToDictionary(@PathVariable(name = "id") Integer dictionaryId, Model model) {
        dictionaryPageDTO.setDictionaryId(dictionaryId);
        Dictionary dictionary = dictionaryService.getDictionary(dictionaryId);
        model.addAttribute("dictionaryId", dictionaryId);
        model.addAttribute("dictionaryName", dictionary.getTitle());
        model.addAttribute("validator", validatorService.getValidator(dictionary.getType()).getRegex());
        model.addAttribute("notes", noteService.getAll(dictionaryId));
        model.addAttribute("note", new Note());
        model.addAttribute("edit", dictionaryPageDTO.isEdition());
        model.addAttribute("editedNoteId", dictionaryPageDTO.getEditedNoteId());

        if (dictionaryPageDTO.getEditedNoteId() != null) {
            Note note = noteService.getById(dictionaryPageDTO.getEditedNoteId());
            model.addAttribute("editableNote", note.getWord());
            model.addAttribute("editableDefinition", note.getDefinition());
        } else {
            model.addAttribute("editedWord", "");
            model.addAttribute("editedDefinition", "");
        }

        Note note = new Note();
        if (dictionaryPageDTO.getFoundWord() != null) {
            note.setWord(dictionaryPageDTO.getFoundWord());
            note.setDefinition(dictionaryPageDTO.getFoundDefinition());
        }
        model.addAttribute("foundDefinition", note);
        model.addAttribute("searchedDefinition", dictionaryPageDTO.getSearchedDefinition());
        model.addAttribute("foundWords", dictionaryPageDTO.getFoundWords());

        return "dictionary";
    }

    @PostMapping("/addWord")
    public String addWord(@ModelAttribute Note note) {
        note.setDictionary(dictionaryPageDTO.getDictionaryId());
        noteService.save(note);
        return "redirect:/dictionary/" + dictionaryPageDTO.getDictionaryId();
    }

    @DeleteMapping("/deleteWord")
    public String deleteWord(@RequestParam int noteId) {
        noteService.delete(noteId);
        return "redirect:/dictionary/" + dictionaryPageDTO.getDictionaryId();
    }

    @GetMapping("/findDefinition")
    public String findDefinition(@RequestParam String word) {
        int dictionaryId = dictionaryPageDTO.getDictionaryId();
        List<Note> notes = noteService.findNote(word, dictionaryId);
        String result;
        if (notes.isEmpty()) {
            result = "Definition not found.";
        } else {
            result = notes.stream().map(Note::getDefinition).collect(Collectors.joining(", "));
        }
        dictionaryPageDTO.setFoundWord(word);
        dictionaryPageDTO.setFoundDefinition(result);
        return "redirect:/dictionary/" + dictionaryId;
    }

    @GetMapping("/findWord")
    public String findWord(@RequestParam String searchedDefinition, @RequestParam(required = false) boolean allDictionaries) {
        int dictionaryId = dictionaryPageDTO.getDictionaryId();
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
        dictionaryPageDTO.setSearchedDefinition(searchedDefinition);
        dictionaryPageDTO.setFoundWords(result);
        return "redirect:/dictionary/" + dictionaryId;
    }

    @GetMapping("/openEditor")
    public String editWord(@RequestParam int editId) {
        dictionaryPageDTO.setEdition(true);
        dictionaryPageDTO.setEditedNoteId(editId);
        return "redirect:/dictionary/" + dictionaryPageDTO.getDictionaryId();
    }

    @PostMapping(value = "/edit", params = "action=save")
    public String editSave(DictionaryPageDTO requestBody) {
        noteService.update(requestBody.getEditedNoteId(), requestBody.getEditableNote(), requestBody.getEditableDefinition());
        dictionaryPageDTO.setEdition(false);
        return "redirect:/dictionary/" + dictionaryPageDTO.getDictionaryId();
    }

    @PostMapping(value = "/edit", params = "action=cancel")
    public String editCancel() {
        dictionaryPageDTO.setEdition(false);
        return "redirect:/dictionary/" + dictionaryPageDTO.getDictionaryId();
    }
}
