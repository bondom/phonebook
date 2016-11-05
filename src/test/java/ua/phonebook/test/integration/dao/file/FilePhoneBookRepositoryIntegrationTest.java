package ua.phonebook.test.integration.dao.file;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import ua.phonebook.dao.BasePhoneBookRecordRepository;
import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.model.User;

/**
 * This class does integration tests with files, paths to them are represented in 
 * {@code TestPropertySource}.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FileRepositoryIntegrationConfiguration.class)
@TestPropertySource(properties = {"filestorage.phonebookrecords.path=D:/phonebookrepotest.json",
					"filestorage.users.path=D:/userrepotest.json"})
@ActiveProfiles({"file","filerepotest"})
public class FilePhoneBookRepositoryIntegrationTest {
	
	@Value("${filestorage.phonebookrecords.path}")
	private String filePath;
	
	@Value("${filestorage.users.path}")
	private String filePathToUsers;
	
	@Autowired
	private BasePhoneBookRecordRepository filePhonebookRepo;
	
	private File temporaryStorage;
	
	//we use this variable only for deleting file, created
	//by @PostConstruct of repository implementation
	private static File temporaryUsersStorage;
	
	private User owner;
	
	private PhoneBookRecord firstRecord;
	
	@Before
	public void getInstancesOfTemporaryStoragesAndSaveOneRecord() throws IOException, NoSuchFieldException, 
				SecurityException, IllegalArgumentException, IllegalAccessException{
		temporaryStorage = new File(filePath);
		temporaryUsersStorage = new File(filePathToUsers);
		//creating test file
		temporaryStorage.createNewFile();
		
		owner = new User("testlogin","testpass");
		owner.setFullName("testfullname");
		owner.setId(1);
		firstRecord = new PhoneBookRecord
				("First","First","First","+380(66)111111");
		firstRecord.setUser(owner);
		firstRecord = filePhonebookRepo.save(firstRecord);
	}
	
	@After
	public void deleteTemporaryStorage(){
		temporaryStorage.delete();
	}
	
	@AfterClass
	public static void deleteUsersStorageCreatedByRepoImpl(){
		//because this is integration test, we are doing autowiring,
		//and all file repository implementations are initialized by Spring
		//so we must delete storage, which is created by @PostConstruct method
		temporaryUsersStorage.delete();
	}
	
	@Test
	public void testSave(){
		PhoneBookRecord newRecord = new PhoneBookRecord
				("Test1","Test1","Test1","+380(66)111111");
		newRecord.setUser(this.owner);
		PhoneBookRecord savedRecord = filePhonebookRepo.save(newRecord);
		assertArrayEquals(new String[]{newRecord.getFirstName(),newRecord.getMobilePhone()},
							new String[]{savedRecord.getFirstName(),savedRecord.getMobilePhone()});
		assertTrue(savedRecord.getId()>=1);
	}
	
	@Test
	public void testFindOne(){
		PhoneBookRecord firstSavedRecord = 
				filePhonebookRepo.findOne(firstRecord.getId());
		assertArrayEquals(new String[]{firstRecord.getFirstName(),firstRecord.getMobilePhone()},
				new String[]{firstSavedRecord.getFirstName(),firstSavedRecord.getMobilePhone()});
		assertArrayEquals(new Object[]{firstRecord.getUser()}, new Object[]{firstSavedRecord.getUser()});
		assertArrayEquals(new long[]{firstRecord.getId()}, new long[]{firstSavedRecord.getId()});
	
	}
	
	@Test
	public void testFindByIdAndUserLogin(){
		PhoneBookRecord firstSavedRecord = 
				filePhonebookRepo.findByIdAndUser_Login(firstRecord.getId(), owner.getLogin());
		assertArrayEquals(new String[]{firstRecord.getFirstName(),firstRecord.getMobilePhone()},
				new String[]{firstSavedRecord.getFirstName(),firstSavedRecord.getMobilePhone()});
		assertArrayEquals(new Object[]{firstRecord.getUser()}, new Object[]{firstSavedRecord.getUser()});
		assertArrayEquals(new long[]{firstRecord.getId()}, new long[]{firstSavedRecord.getId()});
	}
	
	@Test
	public void testGetByUser_Login(){
		//creating and saving one more record
		User otherUser = new User("other","other");
		otherUser.setFullName("other");
		otherUser.setId(owner.getId()+1);
		
		PhoneBookRecord otherRecord = 
				new PhoneBookRecord("otherrecord", "otherRecord", "otherRecord", "+380(50)0000000");
		otherRecord.setUser(otherUser);
		filePhonebookRepo.save(otherRecord);
		
		//getting records for owner
		Page<PhoneBookRecord> pageInfo= 
				filePhonebookRepo.getByUser_Login(owner.getLogin(), new PageRequest(0, 10));
		List<PhoneBookRecord> ownerRecords = pageInfo.getContent();
		assertTrue(ownerRecords.size()==1);
		PhoneBookRecord retrievedRecord = ownerRecords.get(0);
		assertArrayEquals(new String[]{firstRecord.getFirstName(),firstRecord.getMobilePhone()},
				new String[]{retrievedRecord.getFirstName(),retrievedRecord.getMobilePhone()});
		assertArrayEquals(new Object[]{firstRecord.getUser()}, new Object[]{retrievedRecord.getUser()});
		assertArrayEquals(new long[]{firstRecord.getId()}, new long[]{retrievedRecord.getId()});
	}
	
	@Test
	public void testDelete(){
		
		filePhonebookRepo.delete(firstRecord);
		
		PhoneBookRecord deletedRecord = 
				filePhonebookRepo.findOne(firstRecord.getId());
		
		assertNull(deletedRecord);
	
	}
	
	@Test
	public void findByUserLoginAndFilter(){
		//creating and saving 4 more records for owner
		
		PhoneBookRecord record1 = 
				new PhoneBookRecord("petia", "petronov", "petrovych", "+380(50)0000000");
		record1.setUser(owner);
		PhoneBookRecord record2 = 
				new PhoneBookRecord("masha", "petrovna", "petrovna", "+380(66)0000000");
		record2.setUser(owner);
		PhoneBookRecord record3 = 
				new PhoneBookRecord("uasia", "uasiovych", "uasiovych", "+380(50)1234567");
		record3.setUser(owner);
		PhoneBookRecord record4 = 
				new PhoneBookRecord("test", "test", "test", "+380(50)1234000");
		record4.setUser(owner);
		
		record1 = filePhonebookRepo.save(record1);
		record2 = filePhonebookRepo.save(record2);
		record3 = filePhonebookRepo.save(record3);
		record4 = filePhonebookRepo.save(record4);
		
		//Creating filter records
		String firstName="a";
		String lastName="petr";
		String mobilePhone="00";
		
		//getting records
		Page<PhoneBookRecord> pageInfo=
				filePhonebookRepo.findFilteredByUserLogin(owner.getLogin(),
														  firstName,
														  lastName,
														  mobilePhone,
														  new PageRequest(0, 10));
	
		List<PhoneBookRecord> filteredRecords = pageInfo.getContent();
		assertTrue(pageInfo.getNumberOfElements()==2);
		assertArrayEquals(new Object[]{filteredRecords.get(0), filteredRecords.get(1)},
							new Object[]{record1,record2});
	
	}

}
