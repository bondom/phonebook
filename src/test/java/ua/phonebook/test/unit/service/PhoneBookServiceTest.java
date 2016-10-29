package ua.phonebook.test.unit.service;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ua.phonebook.dao.BasePhoneBookRecordRepository;
import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.model.User;
import ua.phonebook.service.exception.InvalidIdentifier;
import ua.phonebook.service.exception.WrongLoginException;
import ua.phonebook.service.impl.PhoneBookServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PhoneBookServiceTest {
	
	@InjectMocks
	private PhoneBookServiceImpl phoneBookService;

	@Mock
	private BaseUserRepository userRepo;
	
	@Mock
	private BasePhoneBookRecordRepository phoneBookRecordRepo;
	
	private PhoneBookRecord phoneBookRecord;
	private User user;

	@Before
	public void setup(){
		user = new User("test", "testPass");
		phoneBookRecord = new PhoneBookRecord(
				"Petia","Patiak","Petrovych","+380(66)116956",user);
		phoneBookRecord.setId(2);
	}
	
	@Test
	public void testGetPhoneBookRecordById() throws InvalidIdentifier{
		
		long id = phoneBookRecord.getId();
		String login = user.getLogin();
		when(phoneBookRecordRepo.findOne(id))
					.thenReturn(phoneBookRecord);
		when(userRepo.getUserByLogin(login)).thenReturn(user);
		
		phoneBookService.getPhoneBookRecordById(login,id);
	}
	
	@Test(expected = InvalidIdentifier.class)
	public void testGetPhoneBookRecordByInvalidId() throws InvalidIdentifier{
		
		long invalidId = 12345;
		String login = user.getLogin();
		
		when(phoneBookRecordRepo.findOne(invalidId))
					.thenReturn(null);
		
		phoneBookService.getPhoneBookRecordById(login,invalidId);
	}
	
	@Test(expected = InvalidIdentifier.class)
	public void testGetOtherPhoneBookRecord() throws InvalidIdentifier{
		
		long id = 12345;
		String login = "otherlogin";
		
		when(phoneBookRecordRepo.findOne(id))
					.thenReturn(phoneBookRecord);
		when(userRepo.getUserByLogin(login)).thenReturn(new User());
		phoneBookService.getPhoneBookRecordById(login,id);
	}
	
	@Test
	public void testDeletePhoneBookRecord() throws InvalidIdentifier{
		
		long id = phoneBookRecord.getId();
		String login = user.getLogin();
		
		when(phoneBookRecordRepo.findByIdAndUser_Login(id, login))
					.thenReturn(phoneBookRecord);
		
		phoneBookService.deletePhoneBookRecord(login, id);
		
		verify(phoneBookRecordRepo).delete(phoneBookRecord);
	}
	
	@Test(expected = InvalidIdentifier.class)
	public void testDeletePhoneBookRecordWithInvalidInfo() throws InvalidIdentifier{
		
		long invalidId=123;
		String login = user.getLogin();
		
		when(phoneBookRecordRepo.findByIdAndUser_Login(invalidId, login))
					.thenReturn(null);
		try{
			phoneBookService.deletePhoneBookRecord(login, invalidId);
		}catch(InvalidIdentifier ex){
			verify(phoneBookRecordRepo,times(0)).delete(phoneBookRecord);
			throw ex;
		}
	}
	
	@Test
	public void testUpdatePhoneBookRecord() throws InvalidIdentifier{
		long id = phoneBookRecord.getId();
		String login = user.getLogin();
		
		PhoneBookRecord editedRecord = new PhoneBookRecord("update", "update", "update", "+380(66)1234567", user);
		editedRecord.setId(id);
		
		when(phoneBookRecordRepo.findOne(id)).thenReturn(phoneBookRecord);
		when(userRepo.getUserByLogin(login)).thenReturn(user);
		
		phoneBookService.updatePhoneBookRecord(login, editedRecord);
		
		verify(phoneBookRecordRepo,times(1)).save(editedRecord);
	}
	
	@Test(expected = InvalidIdentifier.class)
	public void testUpdatePhoneBookRecordWithInvalidId() throws InvalidIdentifier{
		long invalidId = 12345;
		String login = user.getLogin();
		
		PhoneBookRecord editedRecord = new PhoneBookRecord("update", "update", "update", "+380(66)1234567", user);
		editedRecord.setId(invalidId);
		
		when(phoneBookRecordRepo.findOne(invalidId)).thenReturn(null);
		
		try{
			phoneBookService.updatePhoneBookRecord(login, editedRecord);
		}catch(InvalidIdentifier ex){
			verify(userRepo,times(0)).getUserByLogin(login);
			verify(phoneBookRecordRepo,times(0)).save(editedRecord);
			throw ex;
		}
	}
	
	@Test(expected = InvalidIdentifier.class)
	public void testUpdateOtherPhoneBookRecord() throws InvalidIdentifier{
		long id = phoneBookRecord.getId();
		String otherLogin = "other";
		
		User user = new User(otherLogin,"pass");
		
		PhoneBookRecord editedRecord = new PhoneBookRecord
				("update", "update", "update", "+380(66)1234567", user);
		editedRecord.setId(id);
		
		when(phoneBookRecordRepo.findOne(id)).thenReturn(phoneBookRecord);
		when(userRepo.getUserByLogin(otherLogin)).thenReturn(user);
		
		try{
			phoneBookService.updatePhoneBookRecord(otherLogin, editedRecord);
		}catch(InvalidIdentifier ex){
			verify(phoneBookRecordRepo,times(0)).save(editedRecord);
			throw ex;
		}
	}
	
	@Test(expected = WrongLoginException.class)
	public void testAddPhoneBookRecordByWrongUser(){
		String wrongLogin = "wrong";
		
		when(userRepo.getUserByLogin(wrongLogin)).thenReturn(null);
		
		try{
			phoneBookService.addPhoneBookRecord(wrongLogin, phoneBookRecord);
		}catch(WrongLoginException ex){
			verify(phoneBookRecordRepo,times(0)).save(phoneBookRecord);
			throw ex;
		}
	}
	
}
