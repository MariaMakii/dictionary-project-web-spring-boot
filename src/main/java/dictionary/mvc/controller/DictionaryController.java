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

    @PostMapping("/dictionary")
    public String dictionaryRequest(DictionaryPageDTO dictionaryPageDTO, Model model) {
        Integer dictionaryId = dictionaryPageDTO.getDictionaryId();
        Dictionary dictionary = dictionaryService.getDictionary(dictionaryId);
        model.addAttribute("dictionaryId", dictionaryId);
        model.addAttribute("dictionaryName", dictionary.getTitle());
        model.addAttribute("validator", validatorService.getValidator(dictionary.getType()).getRegex());
        model.addAttribute("notes", noteService.getAllNotesInDictionary(dictionaryId));
        model.addAttribute("isEditionFormOpened", dictionaryPageDTO.isEditionFormOpened());
        model.addAttribute("editedNoteId", dictionaryPageDTO.getEditedNoteId());

        Note note = new Note();
        if (dictionaryPageDTO.getEditedNoteId() != null) {
            note = noteService.getById(dictionaryPageDTO.getEditedNoteId());
        }

        model.addAttribute("editedWord", note.getWord());
        model.addAttribute("editedDefinition", note.getDefinition());

        model.addAttribute("searchedWord", dictionaryPageDTO.getSearchedWord());
        model.addAttribute("foundDefinition", dictionaryPageDTO.getFoundDefinition());
        model.addAttribute("searchedDefinition", dictionaryPageDTO.getSearchedDefinition());
        model.addAttribute("foundWords", dictionaryPageDTO.getFoundWord());
        return "dictionary";
    }

    @PostMapping("/addWord")
    public String addWord(@ModelAttribute Note note, DictionaryPageDTO requestBody, Model model) {
        note.setDictionary(requestBody.getDictionaryId());
        noteService.saveNewNote(note);
        return dictionaryRequest(requestBody, model);
    }

    @DeleteMapping("/deleteWord")
    public String deleteWord(DictionaryPageDTO requestBody, Model model) {
        noteService.deleteNoteById(requestBody.getDeletedNoteId());
        return dictionaryRequest(requestBody, model);
    }

    @GetMapping("/findDefinition")
    public String findDefinition(@RequestParam Integer dictionaryId, @RequestParam String searchedWord, Model model) {
        List<Note> notes = noteService.findNoteByNameInDictionary(searchedWord, dictionaryId);
        String result;
        if (notes.isEmpty()) {
            result = "Definition not found.";
        } else {
            result = notes.stream().map(Note::getDefinition).collect(Collectors.joining(", "));
        }
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(dictionaryId);
        dictionaryPageDTO.setSearchedWord(searchedWord);
        dictionaryPageDTO.setFoundDefinition(result);
        return dictionaryRequest(dictionaryPageDTO, model);
    }

    @GetMapping("/findWord")
    public String findWord(@RequestParam Integer dictionaryId,
                           @RequestParam String searchedDefinition,
                           @RequestParam(required = false) boolean allDictionaries,
                           Model model) {
        List<Note> notes;
        if (allDictionaries) {
            notes = noteService.findNoteByDefinitionInDictionary(searchedDefinition);
        } else {
            notes = noteService.findNoteByDefinitionInDictionary(searchedDefinition, dictionaryId);
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
        return dictionaryRequest(dictionaryPageDTO, model);
    }

    @GetMapping("/openEditor")
    public String editWord(@RequestParam Integer dictionaryId, @RequestParam Integer editedNoteId, Model model) {
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(dictionaryId);
        dictionaryPageDTO.setEditionFormOpened(true);
        dictionaryPageDTO.setEditedNoteId(editedNoteId);
        return dictionaryRequest(dictionaryPageDTO, model);
    }

    @PostMapping(value = "/edit", params = "action=save")
    public String editSave(DictionaryPageDTO requestBody, Model model) {
        noteService.updateNoteById(requestBody.getEditedNoteId(), requestBody.getEditedWord(), requestBody.getEditedDefinition());
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(requestBody.getDictionaryId());
        dictionaryPageDTO.setEditionFormOpened(false);
        return dictionaryRequest(dictionaryPageDTO, model);
    }

    @PostMapping(value = "/edit", params = "action=cancel")
    public String editCancel(DictionaryPageDTO requestBody, Model model) {
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(requestBody.getDictionaryId());
        dictionaryPageDTO.setEditionFormOpened(false);
        return dictionaryRequest(dictionaryPageDTO, model);
    }
}
