package dictionary.mvc.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class DictionaryPageDTO {
    private String searchedWord;
    private String foundDefinition;
    private String searchedDefinition;
    private String foundWord;
    private boolean edition;
    private Integer editedNoteId;
    private Integer dictionaryId;
    private String editableNote;
    private String editableDefinition;
    private Integer deletedNoteId;
}
