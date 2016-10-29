package ua.phonebook.test.unit.web;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.model.User;
import ua.phonebook.security.SecurityConfig;
import ua.phonebook.service.PhoneBookService;
import ua.phonebook.service.exception.InvalidIdentifier;
import ua.phonebook.service.impl.UserDetailsServiceImpl;
import ua.phonebook.support.Message;
import ua.phonebook.web.controller.PhoneBookController;
import ua.phonebook.web.viewbean.FilterPhoneBookRecords;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PhoneBookController.class,
			includeFilters = {@ComponentScan.Filter(
					type = FilterType.ASSIGNABLE_TYPE,
					value = UserDetailsServiceImpl.class)})
@ActiveProfiles("test")
@Import({SecurityConfig.class})
public class PhoneBookControllerTest {
	
	
	private MockMvc mvc;
		
    @Autowired
    private WebApplicationContext context;
    
	@MockBean
	private PhoneBookService phoneBookService;
	
	@MockBean
	private Message message;
	
	@MockBean
	private BaseUserRepository userRepo;
	
	
	@Before
	public void setup(){
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	
	@Test
	@WithMockUser
	public void testGetFilteredPhoneBook() throws Exception{
		String firstName="name";
		String lastName="name";
		String mobilePhone="+380(66)1234567";
		
		FilterPhoneBookRecords filter = 
				new FilterPhoneBookRecords(firstName,lastName,mobilePhone);
		
		List<PhoneBookRecord> list = new ArrayList<>();
		list.add(new PhoneBookRecord("firstName",
									 "lastName",
									 "patronymic",
									 "+380(66)1111111",
									 new User()));
		Page<PhoneBookRecord> pageImpl = new PageImpl<>(list);
		given(phoneBookService
				.getFilteredPhoneBookByUserLogin(anyString(), anyObject(),anyObject()))
				.willReturn(pageImpl);
		
		
		mvc.perform(get("/phonebook")
		   .param("firstName",firstName)
		   .param("lastName",lastName)
		   .param("mobilePhone",mobilePhone))
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentTypeCompatibleWith("text/html"))
			   .andExpect(model().attribute("filter", filter))
			   .andExpect(model().attribute("filtering", true))
			   .andExpect(model().attributeExists("phoneBook"));
		
	}
	
	@Test
	@WithMockUser
	public void testGetPhoneBookWithoutFilter() throws Exception{
		String firstName="";
		String lastName="";
		String mobilePhone="";
		
		List<PhoneBookRecord> list = new ArrayList<>();
		list.add(new PhoneBookRecord("firstName",
									 "lastName",
									 "patronymic",
									 "+380(66)1111111",
									 new User()));
		Page<PhoneBookRecord> pageImpl = new PageImpl<>(list);
		given(phoneBookService
				.getPhoneBookByUserLogin(anyString(),anyObject()))
				.willReturn(pageImpl);
		
		
		mvc.perform(get("/phonebook")
		   .param("firstName",firstName)
		   .param("lastName",lastName)
		   .param("mobilePhone",mobilePhone))
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentTypeCompatibleWith("text/html"))
			   .andExpect(model().attribute("filtering", false))
			   .andExpect(model().attributeExists("phoneBook"));
		
		then(phoneBookService).should(times(1))
				.getPhoneBookByUserLogin(anyString(),anyObject());
	}
	
	@Test
    public void testGetPhoneBookByUnuthorizedUser() throws Exception{
    	mvc.perform(get("/phonebook"))
    				.andDo(print())
    				.andExpect(redirectedUrlPattern("**/login"));
    }
	    
	 
	@Test
	@WithMockUser
	public void testAddPhoneBookRecord() throws Exception{
		given(message.getMessage("phonebookrecord.add.success"))
			.willReturn("Some message");
		
		mvc.perform(post("/phonebook/addRecord").with(csrf()))
			.andExpect(flash().attributeExists("success"))
			.andExpect(redirectedUrl("/phonebook"));
		
		then(phoneBookService).should(times(1))
			.addPhoneBookRecord(anyString(),anyObject());
	}
	
	
	@Test
	@WithMockUser
	public void testDeletePhoneBookRecord() throws Exception{
		given(message.getMessage("phonebookrecord.delete.success"))
			.willReturn("Some message");
		
		mvc.perform(post("/phonebook/deleteRecord").param("id","1").with(csrf()))
			.andDo(print())
			.andExpect(flash().attributeExists("success"))
			.andExpect(redirectedUrl("/phonebook"));
		
		then(phoneBookService).should()
			.deletePhoneBookRecord(anyString(),anyLong());
	}
	
	@Test
	@WithMockUser
	public void testDeletePhoneBookRecordWithInvalidId() throws Exception{
		doThrow(InvalidIdentifier.class).when(phoneBookService).deletePhoneBookRecord(anyString(),anyLong());
						
		mvc.perform(post("/phonebook/deleteRecord").param("id","1").with(csrf()))
			.andExpect(flash().attributeCount(0))
			.andExpect(redirectedUrl("/phonebook"));
	}
	
	@Test
	@WithMockUser
	public void testGetPageForEditing() throws Exception{
		long id = 123;
		PhoneBookRecord record = new PhoneBookRecord();
		record.setId(id);
		doReturn(record).when(phoneBookService).getPhoneBookRecordById(anyString(),eq(id));
		
		mvc.perform(get("/phonebook/editRecord").param("id",id+""))
			.andDo(print())
			.andExpect(model().attribute("record",record))
			.andExpect(view().name("editRecord"));
	}
	
	@Test
	@WithMockUser
	public void testGetPageForEditingForInvalidId() throws Exception{
		long invalidId = 123;
		doThrow(InvalidIdentifier.class).when(phoneBookService).getPhoneBookRecordById(anyString(),eq(invalidId));
		
		mvc.perform(get("/phonebook/editRecord").param("id",invalidId+""))
			.andDo(print())
			.andExpect(model().attributeDoesNotExist("record"))
			.andExpect(redirectedUrl("/phonebook"));
	}
		
	@Test
	@WithMockUser
	public void testGetRedirectedPageForEditing() throws Exception{
		long id = 123;
		PhoneBookRecord record = new PhoneBookRecord();
		record.setId(id);
		
		mvc.perform(get("/phonebook/editRecord")
					.param("id",id+"")
					.flashAttr("record", record))
			.andDo(print())
			.andExpect(view().name("editRecord"));
		
		then(phoneBookService).should(times(0))
			.getPhoneBookRecordById(anyString(),eq(id));
	}
	
	@Test
	@WithMockUser
	public void testUpdateRecord() throws Exception{
		given(message.getMessage("phonebookrecord.update.success"))
						.willReturn("Some message");
		long id = 1234;
		PhoneBookRecord recordToUpdate = 
				new PhoneBookRecord("firstName",
									 "lastName",
									 "patronymic",
									 "+380(66)1111111");
		recordToUpdate.setId(id);
		mvc.perform(post("/phonebook/updateRecord")
					.param("id", id+"")
					.param("firstName",recordToUpdate.getFirstName())
					.param("lastName",recordToUpdate.getLastName())
					.param("patronymic",recordToUpdate.getPatronymic())
					.param("mobilePhone",recordToUpdate.getMobilePhone())
					.with(csrf()))
						.andDo(print())
						.andExpect(redirectedUrl("/phonebook"))
						.andExpect(flash().attributeExists("success"));
	}
	
	@Test
	@WithMockUser
	public void testUpdateRecordWithInvalidId() throws Exception{
		long invalidId = 1234;
		PhoneBookRecord recordToUpdate = 
				new PhoneBookRecord("firstName",
									 "lastName",
									 "patronymic",
									 "+380(66)1111111");
		recordToUpdate.setId(invalidId);
		
		doThrow(InvalidIdentifier.class)
			.when(phoneBookService).updatePhoneBookRecord("user",recordToUpdate);
		
		mvc.perform(post("/phonebook/updateRecord")
					.param("id", invalidId+"")
					.param("firstName",recordToUpdate.getFirstName())
					.param("lastName",recordToUpdate.getLastName())
					.param("patronymic",recordToUpdate.getPatronymic())
					.param("mobilePhone",recordToUpdate.getMobilePhone())
					.with(csrf()))
						.andDo(print())
						.andExpect(redirectedUrl("/phonebook"))
						.andExpect(flash().attributeCount(0));
	
		then(message).should(times(0))
			.getMessage(anyString());
	
	}
}
