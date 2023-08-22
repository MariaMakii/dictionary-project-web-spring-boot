package dictionary.mvc.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "word")
    private String word;

    @Column(name = "definition")
    private String definition;

    @Column(name = "dictionary")
    private Integer dictionary;

    @Override
    public String toString() {
        return this.word + " - " + this.definition;
    }
}
