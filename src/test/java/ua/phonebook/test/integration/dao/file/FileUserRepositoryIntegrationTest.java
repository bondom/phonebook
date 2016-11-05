package ua.phonebook.test.integration.dao.file;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import ua.phonebook.dao.BaseUserRepository;
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
public class FileUserRepositoryIntegrationTest {
	
	@Value("${filestorage.users.path}")
	private String filePath;
	
	@Value("${filestorage.phonebookrecords.path}")
	private String filePathToPhoneBookRecords;
	
	@Autowired
	private BaseUserRepository fileRepo;
	
	private File temporaryStorage;
	
	//we use this variable only for deleting file, created
	//by @PostConstruct of repository implementation
	private static File temporaryRecordsStorage;
	
	private User firstUser;	
	
	@Before
	public void getInstanceOfTemporaryStoragesAndSaveOneUser() throws IOException, NoSuchFieldException, 
				SecurityException, IllegalArgumentException, IllegalAccessException{
		temporaryStorage = new File(filePath);
		temporaryRecordsStorage = new File(filePathToPhoneBookRecords);
		
		temporaryStorage.createNewFile();
		firstUser = new User("InBefore","InBefore");
		firstUser.setFullName("Tolik");
		fileRepo.saveAndFlush(firstUser);
	}
	
	@After
	public void deleteTemporaryStorages(){
		temporaryStorage.delete();
	}
	
	@AfterClass
	public static void deleteUsersStorageCreatedByRepoImpl(){
		//because this is integration test, we are doing autowiring,
		//and all file repository implementations are initialized by Spring
		//so we must delete storage, which is created by @PostConstruct method
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
