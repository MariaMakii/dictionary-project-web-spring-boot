package dictionary.mvc.controller;

import dictionary.mvc.dto.DictionaryPageDTO;
import dictionary.mvc.model.entity.Dictionary;
import dictionary.mvc.model.entity.Note;
import dictionary.mvc.model.service.DictionaryService;
import dictionary.mvc.model.service.NoteService;
import dictionary.mvc.model.service.ValidatorService;
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

    public DictionaryController(DictionaryService dictionaryService, NoteService noteService, ValidatorService validatorService) {
        this.dictionaryService = dictionaryService;
        this.noteService = noteService;
        this.validatorService = validatorService;
    }

    private void prepareDictionaryModel(DictionaryPageDTO requestBody, Model model) {
        int dictionaryId = requestBody.getDictionaryId();
        Dictionary dictionary = dictionaryService.getDictionary(dictionaryId);
        model.addAttribute("dictionaryId", dictionaryId);
        model.addAttribute("dictionaryName", dictionary.getTitle());
        model.addAttribute("validator", validatorService.getValidator(dictionary.getType()).getRegex());
        model.addAttribute("notes", noteService.getAll(dictionaryId));
        model.addAttribute("note", new Note());
        model.addAttribute("edit", requestBody.isEdition());
        model.addAttribute("editedNoteId", requestBody.getEditedNoteId());

        if (requestBody.getEditedNoteId() != null) {
            Note note = noteService.getById(requestBody.getEditedNoteId());
            model.addAttribute("editableNote", note.getWord());
            model.addAttribute("editableDefinition", note.getDefinition());
        } else {
            model.addAttribute("editedWord", "");
            model.addAttribute("editedDefinition", "");
        }

        Note note = new Note();
        if (requestBody.getSearchedWord() != null) {
            note.setWord(requestBody.getSearchedWord());
            note.setDefinition(requestBody.getFoundDefinition());
        }
        model.addAttribute("foundDefinition", note);
        model.addAttribute("searchedDefinition", requestBody.getSearchedDefinition());
        model.addAttribute("foundWords", requestBody.getFoundWord());
    }

    @PostMapping("/dictionary")
    public String dictionaryRequest(DictionaryPageDTO requestBody, Model model) {
        prepareDictionaryModel(requestBody, model);
        return "dictionary";
    }

    @PostMapping("/addWord")
    public String addWord(@ModelAttribute Note note, DictionaryPageDTO requestBody, Model model) {
        note.setDictionary(requestBody.getDictionaryId());
        noteService.save(note);
        prepareDictionaryModel(requestBody, model);
        return "dictionary";
    }

    @DeleteMapping("/deleteWord")
    public String deleteWord(DictionaryPageDTO requestBody, Model model) {
        System.out.println(requestBody);
        noteService.delete(requestBody.getDeletedNoteId());
        prepareDictionaryModel(requestBody, model);
        return "dictionary";
    }

    @GetMapping("/findDefinition")
    public String findDefinition(@RequestParam String word, @RequestParam int dictionaryId, Model model) {
        List<Note> notes = noteService.findNote(word, dictionaryId);
        String result;
        if (notes.isEmpty()) {
            result = "Definition not found.";
        } else {
            result = notes.stream().map(Note::getDefinition).collect(Collectors.joining(", "));
        }
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(dictionaryId);
        dictionaryPageDTO.setSearchedWord(word);
        dictionaryPageDTO.setFoundDefinition(result);
        prepareDictionaryModel(dictionaryPageDTO, model);
        return "dictionary";
    }

    @GetMapping("/findWord")
    public String findWord(@RequestParam String searchedDefinition, @RequestParam(required = false) boolean allDictionaries, @RequestParam int dictionaryId, Model model) {
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
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(dictionaryId);
        dictionaryPageDTO.setSearchedDefinition(searchedDefinition);
        dictionaryPageDTO.setFoundWord(result);
        prepareDictionaryModel(dictionaryPageDTO, model);
        return "dictionary";
    }

    @GetMapping("/openEditor")
    public String editWord(@RequestParam int editedNoteId, @RequestParam int dictionaryId, Model model) {
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(dictionaryId);
        dictionaryPageDTO.setEdition(true);
        dictionaryPageDTO.setEditedNoteId(editedNoteId);
        prepareDictionaryModel(dictionaryPageDTO, model);
        return "dictionary";
    }

    @PostMapping(value = "/edit", params = "action=save")
    public String editSave(DictionaryPageDTO requestBody, Model model) {
        System.out.println(requestBody);
        noteService.update(requestBody.getEditedNoteId(), requestBody.getEditableNote(), requestBody.getEditableDefinition());
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(requestBody.getDictionaryId());
        dictionaryPageDTO.setEdition(false);
        prepareDictionaryModel(dictionaryPageDTO, model);
        return "dictionary";
    }

    @PostMapping(value = "/edit", params = "action=cancel")
    public String editCancel(DictionaryPageDTO requestBody, Model model) {
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(requestBody.getDictionaryId());
        dictionaryPageDTO.setEdition(false);
        prepareDictionaryModel(dictionaryPageDTO, model);
        return "dictionary";
    }
}
