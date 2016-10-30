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
	
	/**
	 * Gets page with {@code list} of {@link PhoneBookRecord}s, which are linked with {@link User},
	 * login of which is passed to method. 
	 * <p>Records in result {@code list} meet following demands:
	 * <ul>
	 * 	<li>record.firstName contains {@code firstName} regardless of case</li>
	 *  <li>record.lastName contains {@code lastName} regardless of case</li>
	 *  <li>record.mobilePhone contains {@code mobilePhone}</li>
	 * </ul>
	 * @param login - login of {@code User}
	 * @param firstName
	 * @param lastName
	 * @param mobilePhone
	 * @param pageable
	 */
	public Page<PhoneBookRecord> findFilteredByUserLogin
						(String login,String firstName,String lastName,String mobilePhone,Pageable pageable);
}
