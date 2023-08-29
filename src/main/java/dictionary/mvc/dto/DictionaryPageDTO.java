package dictionary.mvc.dto;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class DictionaryPageDTO {
    private String foundWord;
    private String foundDefinition;
    private String searchedDefinition;
    private String foundWords;
    private boolean edition;
    private Integer editedNoteId;
    private Integer dictionaryId;
    private String editableNote;
    private String editableDefinition;
}
