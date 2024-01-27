package fi.ollipuljula.tuplaus.domain;

import fi.ollipuljula.tuplaus.db.entity.GameTransaction;
import fi.ollipuljula.tuplaus.rest.model.PlayDto;
import fi.ollipuljula.tuplaus.rest.model.DoubleDto;

public interface DoublingService {
    GameTransaction play(PlayDto dto);

    GameTransaction playDouble(DoubleDto dto);

}
