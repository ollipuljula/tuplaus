package fi.ollipuljula.tuplaus.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fi.ollipuljula.tuplaus.db.DoublingChoice;
import fi.ollipuljula.tuplaus.db.entity.GameTransaction;
import fi.ollipuljula.tuplaus.db.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayResultDto {
    @JsonProperty("gameTransactionId")
    private Long id;

    private LocalDateTime executionTime;

    @JsonIgnore
    private User user;

    private double bet;

    private DoublingChoice choice;

    private int drawn;

    private double winnings;

    private GameTransaction.Status status;

    @JsonProperty
    public String getFirstname() {
        return this.user.getFirstname();
    }

    @JsonProperty
    public String getLastname() {
        return this.user.getLastname();
    }

    @JsonProperty
    public double getBalance() {
        return this.user.getAccount().getBalance();
    }

}
