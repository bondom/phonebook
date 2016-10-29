package ua.phonebook.test.integration.dao;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import ua.phonebook.dao.filestorage.FileUserRepository;
import ua.phonebook.model.User;

/**
 * This class does integration tests with file, path to it is represented in 
 * {@code TestPropertySource}
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FileRepositoryIntegrationConfiguration.class)
@ActiveProfiles({"file","filerepotest"})
public class FileUserRepositoryIntegrationTest {
	
	@Value("${filestorage.users.path}")
	private String filePath;
	
	@Value("${filestorage.phonebookrecords.path}")
	private String filePathToPhoneBookRecords;
	
	@Autowired
	private FileUserRepository fileRepo;
	
	private File temporaryStorage;
	
	private File temporaryRecordsStorage;
	
	private User firstUser;	
	
	@Before
	public void getInstanceOfTemporaryStoragesAndSaveOneUser() throws IOException, NoSuchFieldException, 
				SecurityException, IllegalArgumentException, IllegalAccessException{
		temporaryStorage = new File(filePath);
		temporaryRecordsStorage = new File(filePathToPhoneBookRecords);
		firstUser = new User("InBefore","InBefore");
		firstUser.setFullName("Tolik");
		fileRepo.saveAndFlush(firstUser);
	}
	
	@After
	public void deleteTemporaryStorages(){
		temporaryStorage.delete();
		temporaryRecordsStorage.delete();
	}
	
	@Test
	public void testSaveAndFlush(){
		User newUser = new User("login", "password");
		newUser.setFullName("testfullName");
		User savedUser = fileRepo.saveAndFlush(newUser);
		assertArrayEquals(new String[]{newUser.getLogin()}, new String[]{savedUser.getLogin()});
		assertTrue(savedUser.getId()>1);
	}
	
	@Test
	public void testGetUser(){
		User userFromFile = fileRepo.getUserByLogin(firstUser.getLogin());
		assertArrayEquals(new String[]{userFromFile.getLogin(),userFromFile.getFullName()}, 
							new String[]{firstUser.getLogin(),firstUser.getFullName()});
	}
}
