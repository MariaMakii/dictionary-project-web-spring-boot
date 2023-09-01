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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/openDictionary")
    public String openDictionary(DictionaryPageDTO dictionaryPageDTO, RedirectAttributes attributes) {
        attributes.addFlashAttribute("DictionaryPageDTOFlash", dictionaryPageDTO);
        return "redirect:/dictionary/" + dictionaryPageDTO.getDictionaryId();
    }

    @GetMapping("/dictionary/{id}")
    public String goToDictionaryPage(@ModelAttribute("DictionaryPageDTOFlash") DictionaryPageDTO pageDTO, @PathVariable(name = "id") Integer dictionaryId, Model model) {
        Dictionary dictionary = dictionaryService.getDictionary(dictionaryId);
        model.addAttribute("dictionaryId", dictionaryId);
        model.addAttribute("dictionaryName", dictionary.getTitle());
        model.addAttribute("validator", validatorService.getValidator(dictionary.getType()).getRegex());
        model.addAttribute("notes", noteService.getAllNotesInDictionary(dictionaryId));
        model.addAttribute("isEditionFormOpened", pageDTO.isEditionFormOpened());
        model.addAttribute("editedNoteId", pageDTO.getEditedNoteId());

        Note note = new Note();
        if (pageDTO.getEditedNoteId() != null) {
            note = noteService.getById(pageDTO.getEditedNoteId());
        }

        model.addAttribute("editedWord", note.getWord());
        model.addAttribute("editedDefinition", note.getDefinition());

        model.addAttribute("searchedWord", pageDTO.getSearchedWord());
        model.addAttribute("foundDefinition", pageDTO.getFoundDefinition());
        model.addAttribute("searchedDefinition", pageDTO.getSearchedDefinition());
        model.addAttribute("foundWords", pageDTO.getFoundWord());
        return "dictionary";
    }

    @PostMapping("/addWord")
    public String addWord(DictionaryPageDTO requestBody, RedirectAttributes redirectAttributes) {
        Note note = new Note();
        note.setWord(requestBody.getNewWord());
        note.setDefinition(requestBody.getNewDefinition());
        note.setDictionary(requestBody.getDictionaryId());
        noteService.saveNewNote(note);
        redirectAttributes.addFlashAttribute("DictionaryPageDTOFlash", requestBody);
        return "redirect:/dictionary/" + requestBody.getDictionaryId();
    }

    @PostMapping("/deleteWord")
    public String deleteWord(DictionaryPageDTO requestBody, RedirectAttributes redirectAttributes) {
        noteService.deleteNoteById(requestBody.getDeletedNoteId());
        redirectAttributes.addFlashAttribute("DictionaryPageDTOFlash", requestBody);
        return "redirect:/dictionary/" + requestBody.getDictionaryId();
    }

    @GetMapping("/findDefinition")
    public String findDefinition(@RequestParam Integer dictionaryId, @RequestParam String searchedWord, RedirectAttributes redirectAttributes) {
        System.out.println("FIND DEF");
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
        redirectAttributes.addFlashAttribute("DictionaryPageDTOFlash", dictionaryPageDTO);
        return "redirect:/dictionary/" + dictionaryId;
    }


    @GetMapping("/findWord")
    public String findWord(@RequestParam Integer dictionaryId,
                           @RequestParam String searchedDefinition,
                           @RequestParam(required = false) boolean allDictionaries,
                           RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("DictionaryPageDTOFlash", dictionaryPageDTO);
        return "redirect:/dictionary/" + dictionaryId;
    }

    @GetMapping("/openEditor")
    public String editWord(@RequestParam Integer dictionaryId, @RequestParam Integer editedNoteId, RedirectAttributes redirectAttributes) {
        DictionaryPageDTO dictionaryPageDTO = new DictionaryPageDTO();
        dictionaryPageDTO.setDictionaryId(dictionaryId);
        dictionaryPageDTO.setEditionFormOpened(true);
        dictionaryPageDTO.setEditedNoteId(editedNoteId);
        redirectAttributes.addFlashAttribute("DictionaryPageDTOFlash", dictionaryPageDTO);
        return "redirect:/dictionary/" + dictionaryId;
    }


    @PostMapping(value = "/edit", params = "action=save")
    public String editSave(DictionaryPageDTO requestBody, RedirectAttributes redirectAttributes) {
        noteService.updateNoteById(requestBody.getEditedNoteId(), requestBody.getEditedWord(), requestBody.getEditedDefinition());
        requestBody.setEditionFormOpened(false);
        redirectAttributes.addFlashAttribute("DictionaryPageDTOFlash", requestBody);
        return "redirect:/dictionary/" + requestBody.getDictionaryId();
    }

    @PostMapping(value = "/edit", params = "action=cancel")
    public String editCancel(DictionaryPageDTO requestBody, RedirectAttributes redirectAttributes) {
        requestBody.setEditionFormOpened(false);
        redirectAttributes.addFlashAttribute("DictionaryPageDTOFlash", requestBody);
        return "redirect:/dictionary/" + requestBody.getDictionaryId();
    }
}