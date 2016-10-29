package ua.phonebook.test.integration.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ua.phonebook.model.User;
import ua.phonebook.security.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;

/**
 * This class does integration tests with real database,
 * therefore Java arg lardi.conf must exists in run configurations
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"db"})
public class UserLoginControllerIntegrationWithDbTest {
	
	@Autowired
    private MockMvc mvc;
 
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
	
	/**
	 * Login of existed User, saved in db in SQL script
	 */
	private final String login = "login";
	
	private final String wrongLogin="wrong";
	
	
	@Test
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD,
						scripts="/afterSuccessRegistration.sql")
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
    @SqlGroup({
    	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,scripts="/saveUser.sql"),
    	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD,scripts="/deleteUser.sql")
    })
    public void testRegistrationFailure() throws Exception{
    	String userLogin = this.login;
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
    	
    	mvc.perform(formLogin("/login_process").userParameter(usernameparameter)
    										   .passwordParam(passwordparameter)
    										   .user(this.wrongLogin)
    										   .password("Unexistedpassword"))
    		.andDo(print())
    		.andExpect(redirectedUrl("/login?error"));
    	
    }
    
    @Test
    @SqlGroup({
    	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,scripts="/saveUser.sql"),
    	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD,scripts="/deleteUser.sql")
    })
    public void testSuccessLogin() throws Exception{
    	mvc.perform(formLogin("/login_process").userParameter(usernameparameter)
    										   .passwordParam(passwordparameter)
    										   .user(this.login)
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
