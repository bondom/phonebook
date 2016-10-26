package ua.phonebook.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.phonebook.model.PhoneBookRecord;

/**
 * Base interface for saving, getting, updating and deleting {@link PhoneBookRecord}s
 * in data storage.
 * 
 * @author Yuriy Phediv
 *
 */
public interface BasePhoneBookRecordRepository {
	
	public Page<PhoneBookRecord> getByUser_Login(String login,Pageable pageable);
	
	public PhoneBookRecord findByIdAndUser_Login(long id,String login);
	
	public PhoneBookRecord save(PhoneBookRecord phoneBookRecord);
	
	public void delete(PhoneBookRecord phoneBookRecord);
	
	public PhoneBookRecord findOne(long id);
	
	public Page<PhoneBookRecord> 
	findByUser_LoginAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndMobilePhoneContaining
						(String login,String firstName,String lastName,String phoneNumber,Pageable pageable);
}
