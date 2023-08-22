package dictionary.mvc.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "type")
public class DictionaryType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "type")
    private String type;
}
