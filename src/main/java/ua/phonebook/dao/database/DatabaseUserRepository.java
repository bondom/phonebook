package ua.phonebook.dao.database;

import org.springframework.context.annotation.Profile;

@Profile("db")
public interface DatabaseUserRepository extends CustomUserRepository {

}
