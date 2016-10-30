package ua.phonebook.test.unit.dao.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ua.phonebook.dao.BasePhoneBookRecordRepository;
import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.model.User;

@RunWith(SpringRunner.class)
@ActiveProfiles("db")
@DataJpaTest
public class DatabasePhoneBookRepositoryUnitTest {
	
	@Autowired
	private BasePhoneBookRecordRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
		
	private User owner;
	
	private PhoneBookRecord record1;

	private PhoneBookRecord record2;
	
	private PhoneBookRecord record3;
	
	private PhoneBookRecord record4;
	
	@Before
	public void savingEntities(){
		owner = new User("testlogin","testpass");
		owner.setFullName("testfullname");
		
		entityManager.persistAndFlush(owner);
		
		record1 = 
				new PhoneBookRecord("petia", "petronov", "petrovych", "+380(50)0000000");
		record1.setUser(owner);
		record2 = 
				new PhoneBookRecord("masha", "petrovna", "petrovna", "+380(66)0000000");
		record2.setUser(owner);
		record3 = 
				new PhoneBookRecord("uasia", "uasiovych", "uasiovych", "+380(50)1234567");
		record3.setUser(owner);
		record4 = 
				new PhoneBookRecord("test", "test", "test", "+380(50)1234000");
		record4.setUser(owner);
		
		entityManager.persistAndFlush(record1);
		entityManager.persistAndFlush(record2);
		entityManager.persistAndFlush(record3);
		entityManager.persistAndFlush(record4);
		
	}
	
	@Test
	public void testFindByUser_LoginAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndMobilePhoneContaining(){
		
		//Getting with empty filter
		Page<PhoneBookRecord> pageInfoWithoutFilter=
				repo.findFilteredByUserLogin(owner.getLogin(),"","","",
												new PageRequest(0, 10));
		
		assertTrue(pageInfoWithoutFilter.getNumberOfElements()==4);
				
		//Creating filter records
		String firstName="a";
		String lastName="petr";
		String mobilePhone="00";
				
		//getting records
		Page<PhoneBookRecord> pageInfo=
				repo.findFilteredByUserLogin(owner.getLogin(),
											firstName,
											lastName,
											mobilePhone,
											new PageRequest(0, 10));
		
		List<PhoneBookRecord> filteredRecords = pageInfo.getContent();
		assertTrue(pageInfo.getNumberOfElements()==2);
		assertArrayEquals(new Object[]{filteredRecords.get(0), filteredRecords.get(1)},
							new Object[]{record1,record2});
		
		//Creating second filter records, reusing previous variables
		firstName="t";
		lastName="t";
		mobilePhone="50";
		
		pageInfo=repo.findFilteredByUserLogin(owner.getLogin(),
											  firstName,
											  lastName,
											  mobilePhone,
											  new PageRequest(0, 10));
		filteredRecords = pageInfo.getContent();
		assertTrue(pageInfo.getNumberOfElements()==2);
		assertArrayEquals(new Object[]{filteredRecords.get(0), filteredRecords.get(1)},
							new Object[]{record1,record4});
		
		
	}
}
