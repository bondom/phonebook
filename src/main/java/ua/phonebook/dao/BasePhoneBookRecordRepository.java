package ua.phonebook.dao;

import java.util.List;

import ua.phonebook.model.PhoneBookRecord;

public interface BasePhoneBookRecordRepository {
	
	public List<PhoneBookRecord> getByUser_Login(String login);
	
	public PhoneBookRecord findByIdAndUser_Login(long id,String login);
	
	public PhoneBookRecord save(PhoneBookRecord phoneBookRecord);
	
	public void delete(PhoneBookRecord phoneBookRecord);
	
	public PhoneBookRecord findOne(long id);
}
