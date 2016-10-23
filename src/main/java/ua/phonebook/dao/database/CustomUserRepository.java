package ua.phonebook.dao.database;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.User;

@Profile("db")
@NoRepositoryBean
public interface CustomUserRepository extends JpaRepository<User, Long>,
											  BaseUserRepository{	

}
