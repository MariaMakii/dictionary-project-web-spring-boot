package dictionary.mvc.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class DictionaryPageDTO {
    private Integer dictionaryId;

    private String searchedWord;
    private String foundDefinition;

    private String searchedDefinition;
    private String foundWord;

    private boolean isEditionFormOpened;
    private Integer editedNoteId;
    private String editedWord;
    private String editedDefinition;

    private Integer deletedNoteId;
}
