package ua.phonebook.dao.database;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Primary
@Profile({"db"})
public interface DatabaseUserRepository extends CustomUserRepository {

}
