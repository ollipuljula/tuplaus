package fi.ollipuljula.tuplaus.db;

import fi.ollipuljula.tuplaus.db.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }
}
