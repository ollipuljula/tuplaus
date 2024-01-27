package fi.ollipuljula.tuplaus.db;

import fi.ollipuljula.tuplaus.db.entity.Account;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao extends AbstractDao<Account> {

    public AccountDao() {
        super(Account.class);
    }
}
