package ua.phonebook.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.phonebook.dao.BasePhoneBookRecordRepository;
import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.model.User;
import ua.phonebook.service.PhoneBookService;
import ua.phonebook.service.exception.InvalidIdentifier;
import ua.phonebook.web.viewbean.FilterPhoneBookRecords;

@Service
@Transactional
public class PhoneBookServiceImpl implements PhoneBookService {

	@Autowired
	private BasePhoneBookRecordRepository phoneBookRecordRepository;
	
	@Autowired
	private BaseUserRepository userRepository;
	
	@Override
	public Page<PhoneBookRecord> getPhoneBookByUserLogin(String login,Pageable pageable) {
		Page<PhoneBookRecord> page = 
				phoneBookRecordRepository.getByUser_Login(login,pageable);
		return page;
	}

	@Override
	public PhoneBookRecord getPhoneBookRecordById(long phoneBookRecordId) 
					throws InvalidIdentifier{
		PhoneBookRecord phoneBookRecord = 
				phoneBookRecordRepository.findOne(phoneBookRecordId);
		if(phoneBookRecord == null){
			throw new InvalidIdentifier();
		}
		return phoneBookRecord;
	}
	
	@Override
	public PhoneBookRecord addPhoneBookRecord(String login, PhoneBookRecord phoneBookRecord) {
		User user = userRepository.getUserByLogin(login);
		phoneBookRecord.setUser(user);
		phoneBookRecordRepository.save(phoneBookRecord);
		return phoneBookRecord;
	}

	@Override
	public void deletePhoneBookRecord(String login, long phoneBookRecordId)
												throws InvalidIdentifier{
		PhoneBookRecord phoneBookRecord = 
				phoneBookRecordRepository.findByIdAndUser_Login(phoneBookRecordId,login);
		if(phoneBookRecord!=null){
			phoneBookRecordRepository.delete(phoneBookRecord);
		}else{
			throw new InvalidIdentifier();
		}
	}

	@Override
	public PhoneBookRecord updatePhoneBookRecord(String login, PhoneBookRecord phoneBookRecord) 
													throws InvalidIdentifier{
		PhoneBookRecord phoneBookRecordFromDataStorage = 
				phoneBookRecordRepository.findOne(phoneBookRecord.getId());
		User user = userRepository.getUserByLogin(login);
		if(!phoneBookRecordFromDataStorage.getUser().equals(user)){
			throw new InvalidIdentifier();
		}
		phoneBookRecord.setUser(user);
		phoneBookRecordRepository.save(phoneBookRecord);
		return phoneBookRecord;
	}

	@Override
	public Page<PhoneBookRecord> getFilteredPhoneBookByUserLogin(String login,FilterPhoneBookRecords filter,
													Pageable pageable) {
		Page<PhoneBookRecord> pageInfo= phoneBookRecordRepository
				.findByUser_LoginAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndMobilePhoneContaining
					(login,filter.getFirstName(), filter.getLastName(), filter.getMobilePhone(),pageable);
		return pageInfo;
	}

	

}
