package ua.phonebook.test.unit.web;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.User;
import ua.phonebook.security.SecurityConfig;
import ua.phonebook.service.UserService;
import ua.phonebook.service.exception.DuplicateLoginException;
import ua.phonebook.service.impl.UserDetailsServiceImpl;
import ua.phonebook.support.Message;
import ua.phonebook.web.controller.UserLoginController;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserLoginController.class,
			includeFilters = @ComponentScan.Filter(
					type = FilterType.ASSIGNABLE_TYPE,
					value = UserDetailsServiceImpl.class))
@ActiveProfiles("test")
@Import({SecurityConfig.class})
public class UserLoginControllerTest {
	
    private MockMvc mvc;
	
    @Autowired
    private WebApplicationContext context;
    
	@MockBean
	private UserService userService;
	
	@MockBean
	private Message message;
	
	@MockBean
	private BaseUserRepository userRepo;
	
	@Before
	public void setup(){
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	@Test
	public void testLoginPage() throws Exception{
		mvc.perform(get("/login").accept(MediaType.TEXT_PLAIN))
		   .andExpect(status().isOk())
		   .andExpect(content().contentTypeCompatibleWith("text/html"))
		   .andExpect(model().attributeExists("user"))
		   .andExpect(view().name("login"));
	}
	
	@Test
	public void testLoginPageWithExistedFlashAttr() throws Exception{
		User user = new User("login","password");
		mvc.perform(get("/login").flashAttr("user", user))
		   .andExpect(status().isOk())
		   .andExpect(content().contentTypeCompatibleWith("text/html"))
		   .andExpect(model().attribute("user",user))
		   .andExpect(view().name("login"));
	}
	
	@Test
	public void testRegistrationSuccess() throws Exception{
		given(message.getMessage("user.registration.success"))
							.willReturn("Successfull registration!");
		mvc.perform(post("/registration")
		   .with(csrf()))
		   .andDo(print())
		   .andExpect(redirectedUrl("/login"))
		   .andExpect(flash().attributeExists("success"));
		
	}
	
	@Test
	public void testRegistrationFailing() throws Exception{
		given(message.getMessage("user.registration.failure"))
							.willReturn("Failure!");
		String login="login";
		String password="password";
		String fullName="fullName";
		User user = new User(login, password);
		user.setFullName(fullName);
		
		
		given(userService.registerUser(user)).willThrow(DuplicateLoginException.class);
		mvc.perform(post("/registration")
		   .param(login,login)
		   .param(password,password)
		   .param(fullName,fullName)
		   .with(csrf()))
		   .andDo(print())
		   .andExpect(redirectedUrl("/login"))
		   .andExpect(flash().attributeExists("error"));
		   //.andExpect(status().isOk());
		
	}
}
