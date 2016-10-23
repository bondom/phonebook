package ua.phonebook.dao.database;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import ua.phonebook.dao.BasePhoneBookRecordRepository;
import ua.phonebook.model.PhoneBookRecord;

@Profile("db")
@NoRepositoryBean
public interface CustomPhoneBookRecordRepository extends JpaRepository<PhoneBookRecord, Long>,
											  BasePhoneBookRecordRepository{	

}
