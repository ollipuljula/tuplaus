package fi.ollipuljula.tuplaus.rest.model;

import fi.ollipuljula.tuplaus.db.DoublingChoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoubleDto {
    private boolean play;

    private Long gameTransactionId;

    private DoublingChoice choice;

}
