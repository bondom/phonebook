package ua.phonebook.test.integration.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ua.phonebook.dao.BasePhoneBookRecordRepository;
import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.model.User;
import ua.phonebook.service.PhoneBookService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * This class does integration tests with real file, all beans are autowired
 * and paths to file storages must exist for correct initialization.
 * <p>Class rewrites path to phonebook storage, but not for user storage<b>!</b>,
 * bacause it doesn't interact with user storage, 
 * therefore Java arg lardi.conf must exists in run configuration.
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"filestorage.phonebookrecords.path=D:/phonebookrepotest.json"})
@ActiveProfiles({"file"})
public class PhoneBookControllerIntegrationWithFileTest {
	
	@Value("${filestorage.phonebookrecords.path}")
	private String filePath;
	
	@Autowired
    private MockMvc mvc;
 
	@Autowired
	private BaseUserRepository userRepo;
	
	@Autowired
	private BasePhoneBookRecordRepository phoneBookRepo;
	
	@Autowired
	private PhoneBookService phoneBookService;
	
	private File temporaryStorage;
	
	/**
	 * Id of PhoneBookRecord, which is saved in file in 
	 * {@link #getInstanceOfTemporaryFileAndSaveOneUserAndOneRecord()}
	 * method
	 */
	private long recordId;
	
	private final long invalidRecordId=876;
	
	@Before
	public void getInstanceOfTemporaryFileAndSaveOneUserAndOneRecord(){
		temporaryStorage = new File(filePath);
		User user = new User("login","client");
		user.setFullName("Tolik");
		userRepo.saveAndFlush(user);
		
		PhoneBookRecord newRecord = 
				new PhoneBookRecord("test", "test", "test", "+380(66)1111111");
		newRecord.setUser(user);
		newRecord = phoneBookRepo.save(newRecord);
		this.recordId = newRecord.getId();
	}
	
	@After
	public void deleteTemporaryFile(){
		temporaryStorage.delete();
	}
	
    @Test
    @WithMockUser(username="login",password ="client")
    public void testGetPhonebook() throws Exception{
    	
    	mvc.perform(get("/phonebook"))
    			.andDo(print())
    			.andExpect(view().name("phonebook"));
    }
    
    @Test
    @WithMockUser(username="login",password ="client")
    public void testAddRecord() throws Exception{
    	
	   PhoneBookRecord record = new PhoneBookRecord("Uasia","Uasia","Uasovych","+380(66)1111111");
    	mvc.perform(post("/phonebook/addRecord").with(csrf())
    			.param("firstName",record.getFirstName())
    			.param("lastName",record.getLastName())
    			.param("patronymic", record.getPatronymic())
    			.param("mobilePhone",record.getMobilePhone()))
		    		.andDo(print())
		    		.andExpect(redirectedUrl("/phonebook"))
		    		.andExpect(flash().attributeExists("success"));
    }
    
    @Test
    @WithMockUser(username="login",password ="client")
    public void testDeleteRecord() throws Exception{
    	long recordId=this.recordId;
    	mvc.perform(post("/phonebook/deleteRecord").with(csrf()).param("id", recordId+""))
		    		.andDo(print())
		    		.andExpect(redirectedUrl("/phonebook"))
		    		.andExpect(flash().attributeExists("success"));
    }
    
    @Test
    @WithMockUser(username="login",password ="client")
    public void testDeleteRecordWithInvalidId() throws Exception{
    	long invalidId=this.invalidRecordId;
    	mvc.perform(post("/phonebook/deleteRecord").with(csrf()).param("id", invalidId+""))
		    		.andDo(print())
		    		.andExpect(redirectedUrl("/phonebook"))
		    		.andExpect(flash().attributeCount(0));
    }
    
    @Test
    @WithMockUser(username="login",password ="client")
    public void testGetPageForEditing() throws Exception{
    	long recordId=this.recordId;
    	PhoneBookRecord recordFromDataStorage=
    			phoneBookService.getPhoneBookRecordById("login",recordId);
    	mvc.perform(get("/phonebook/editRecord").param("id", recordId+""))
		    		.andDo(print())
		    		.andExpect(view().name("editRecord"))
		    		.andExpect(model().attribute("record",recordFromDataStorage));
    }
    
    @Test
    @WithMockUser(username="login",password ="client")
    public void testGetPageForEditingForInvalidId() throws Exception{
    	long invalidId=this.invalidRecordId;
    	mvc.perform(get("/phonebook/editRecord").param("id", invalidId+""))
		    		.andDo(print())
		    		.andExpect(redirectedUrl("/phonebook"))
		    		.andExpect(model().attributeDoesNotExist("record"));
    }
    
    @Test
    @WithMockUser(username="login",password ="client")
    public void testUpdateRecord() throws Exception{
    	long recordId=this.recordId;
    	 PhoneBookRecord recordToUpdate = 
    			 new PhoneBookRecord("Uasiaupdate","Uasiaupdate","Uasovych","+380(66)1111111");
    	 recordToUpdate.setId(recordId);
    	mvc.perform(post("/phonebook/updateRecord").with(csrf())
    						.param("id", recordToUpdate.getId()+"")
					    	.param("firstName",recordToUpdate.getFirstName())
							.param("lastName",recordToUpdate.getLastName())
							.param("patronymic", recordToUpdate.getPatronymic())
							.param("mobilePhone",recordToUpdate.getMobilePhone()))
					    		.andDo(print())
					    		.andExpect(redirectedUrl("/phonebook"))
					    		.andExpect(flash().attributeExists("success"));
    }
    
    @Test
    @WithMockUser(username="login",password ="client")
    public void testUpdateRecordWithInvalidId() throws Exception{
    	long invalidId=this.invalidRecordId;
    	 PhoneBookRecord recordToUpdate = 
    			 new PhoneBookRecord("Uasiaupdate","Uasiaupdate","Uasovych","+380(66)1111111");
    	 recordToUpdate.setId(invalidId);
    	mvc.perform(post("/phonebook/updateRecord").with(csrf())
    						.param("id", recordToUpdate.getId()+"")
					    	.param("firstName",recordToUpdate.getFirstName())
							.param("lastName",recordToUpdate.getLastName())
							.param("patronymic", recordToUpdate.getPatronymic())
							.param("mobilePhone",recordToUpdate.getMobilePhone()))
					    		.andDo(print())
					    		.andExpect(redirectedUrl("/phonebook"))
					    		.andExpect(flash().attributeCount(0));
    }
    
    
}
