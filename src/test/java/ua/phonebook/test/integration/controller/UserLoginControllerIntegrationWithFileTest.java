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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.User;
import ua.phonebook.security.SecurityConfig;
import ua.phonebook.service.UserService;
import ua.phonebook.service.exception.DuplicateLoginException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;

/**
 * This class does integration tests with real file, all beans are autowired
 * and paths to file storages must exist for correct initialization.
 * <p>Class rewrites path to user storage, but not for phonebook storage<b>!</b>,
 * because it doesn't interact with phonebook storage, 
 * therefore Java arg lardi.conf must exists in run configuration.
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"filestorage.users.path=D:/userrepotest.json"})
@ActiveProfiles({"file"})
public class UserLoginControllerIntegrationWithFileTest {
	
	@Value("${filestorage.users.path}")
	private String filePath;
	
	@Autowired
    private MockMvc mvc;
 
	@Autowired
	private BaseUserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * The HTTP parameter to place the username. 
	 * Is specified in {@link SecurityConfig}
	 */
	private final String usernameparameter="login";
	/**
	 * The HTTP parameter to place the password. 
	 * Is specified in {@link SecurityConfig}
	 */
	private final String passwordparameter="password";
	
	private File temporaryStorage;
	
	private User existedUser;
	
	@Before
	public void getInstanceOfTemporaryStorageAndSaveOneUser() throws DuplicateLoginException{
		temporaryStorage = new File(filePath);
		existedUser = new User("login",passwordEncoder.encode("client"));
		existedUser.setFullName("Tolik");
		userRepo.saveAndFlush(existedUser);
	}
	
	@After
	public void deleteTemporaryStorage(){
		temporaryStorage.delete();
	}
	
	@Test
    public void testRegistrationSuccess() throws Exception{
    	String userLogin = "Testlogin";
    	String userPassword= "Testpassword";
    	String userFullName= "Testfullname";
    	User newUser = new User(userLogin,userPassword);
    	newUser.setFullName(userFullName);
    	mvc.perform(post("/registration").with(csrf())
    				.param("login", userLogin)
    				.param("password", userPassword)
    				.param("fullName", userFullName))
    						.andExpect(redirectedUrl("/login"))
    						.andExpect(flash().attributeExists("success"))
    						.andDo(print());
    }
    
    @Test
    @WithAnonymousUser
    public void testRegistrationFailure() throws Exception{
    	String userLogin = existedUser.getLogin();
    	String userPassword= "Testpassword";
    	String userFullName= "Testfullname";
    	User newUser = new User(userLogin,userPassword);
    	newUser.setFullName(userFullName);
    	mvc.perform(post("/registration").with(csrf())
    				.param("login", userLogin)
    				.param("password", userPassword)
    				.param("fullName", userFullName))
    						.andExpect(redirectedUrl("/login"))
    						.andExpect(flash().attributeExists("error"))
    						.andDo(print());
    }
    
    @Test
    public void testFailureLogin() throws Exception{
    	
    	mvc.perform(formLogin("/login_process").userParameter(this.usernameparameter)
								   				.passwordParam(this.passwordparameter)
				    							.user("Unexistedlogin")
				    							.password("Unexistedpassword"))
    		.andDo(print())
    		.andExpect(redirectedUrl("/login?error"));
    	
    }
    
    @Test
    public void testSuccessLogin() throws Exception{
    	mvc.perform(formLogin("/login_process").userParameter(this.usernameparameter)
    										   .passwordParam(this.passwordparameter)
    										   .user(existedUser.getLogin())
    										   	.password("client"))
				.andDo(print())
				.andExpect(redirectedUrl("/phonebook"));
    }
    
    @Test
    public void testLogout() throws Exception{
    	
    	mvc.perform(logout("/logout"))
    					.andDo(print())
    					.andExpect(redirectedUrl("/login?logout"));
    	
    }
    
}
