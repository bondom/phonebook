package ua.phonebook.dao.database;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import ua.phonebook.dao.BasePhoneBookRecordRepository;
import ua.phonebook.model.PhoneBookRecord;

@Profile("db")
@NoRepositoryBean
public interface CustomPhoneBookRecordRepository extends JpaRepository<PhoneBookRecord, Long>,
											  BasePhoneBookRecordRepository{	
	@Query("select record from PhoneBookRecord record left join record.user user"
			+ " where user.login=:#{#login} " 
			+ " and upper(record.firstName) like concat('%',upper(:#{#firstName}),'%') " 
            + " and upper(record.lastName) like  concat('%',upper(:#{#lastName}),'%') "
			+ " and record.mobilePhone like %:#{#mobilePhone}%")
	public Page<PhoneBookRecord>findFilteredByUserLogin(@Param("login") String login,
														@Param("firstName") String firstName,
														@Param("lastName") String lastName,
														@Param("mobilePhone") String mobilePhone,
														Pageable pageable);
}
