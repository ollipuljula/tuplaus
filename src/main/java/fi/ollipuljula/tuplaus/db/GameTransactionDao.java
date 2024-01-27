package fi.ollipuljula.tuplaus.db;

import fi.ollipuljula.tuplaus.db.entity.GameTransaction;
import org.springframework.stereotype.Repository;

@Repository
public class GameTransactionDao extends AbstractDao<GameTransaction> {

    public GameTransactionDao() {
        super(GameTransaction.class);
    }
}
