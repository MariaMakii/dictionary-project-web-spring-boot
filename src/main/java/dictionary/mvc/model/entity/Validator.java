package dictionary.mvc.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Validator{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "regex")
    private String regex;

    @Column(name = "type")
    private Integer type;
}
