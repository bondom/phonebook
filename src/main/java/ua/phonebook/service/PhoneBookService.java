package ua.phonebook.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.service.exception.InvalidIdentifier;

public interface PhoneBookService {
	
	/**
	 * Gets 
	 * @param login
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	public List<PhoneBookRecord> getPhoneBookByUserLogin(String login);
	
	@PreAuthorize("isAuthenticated()")
	public PhoneBookRecord getPhoneBookRecordById(long phoneBookRecordId) 
									throws InvalidIdentifier;
	
	@PreAuthorize("isAuthenticated()")
	public PhoneBookRecord addPhoneBookRecord
						(String login,PhoneBookRecord phoneBookRecord);
	
	@PreAuthorize("isAuthenticated()")
	public void deletePhoneBookRecord(String login, long phoneBookRecordId) 
										throws InvalidIdentifier;
	
	@PreAuthorize("isAuthenticated()")
	public PhoneBookRecord updatePhoneBookRecord(String login, PhoneBookRecord phoneBookRecord)
										throws InvalidIdentifier;
}
