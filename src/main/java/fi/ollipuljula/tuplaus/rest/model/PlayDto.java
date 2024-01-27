package fi.ollipuljula.tuplaus.rest.model;

import fi.ollipuljula.tuplaus.db.DoublingChoice;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayDto {
    @NotNull
    private Long userId;

    @Positive
    private double bet;

    @NotNull
    private DoublingChoice choice;
}
