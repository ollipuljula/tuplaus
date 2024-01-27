package fi.ollipuljula.tuplaus.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity implements Serializable {
    private String firstname;

    private String lastname;

    @OneToOne(mappedBy = "user")
    private Account account;
}
