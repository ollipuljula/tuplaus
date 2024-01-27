package fi.ollipuljula.tuplaus.db.entity;

import fi.ollipuljula.tuplaus.db.DoublingChoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class GameTransaction extends AbstractEntity implements Serializable {
    private LocalDateTime executionTime;

    @ManyToOne
    private User user;

    private double bet;

    @Enumerated(EnumType.STRING)
    private DoublingChoice choice;

    private int drawn;

    private double winnings;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    private GameTransaction parent;

    @OneToOne
    private GameTransaction child;

    public enum Status {
        OPEN, SETTLED
    }
}
