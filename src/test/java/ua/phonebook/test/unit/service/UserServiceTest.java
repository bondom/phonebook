package ua.phonebook.test.unit.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.User;
import ua.phonebook.service.exception.DuplicateLoginException;
import ua.phonebook.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private BaseUserRepository userRepo;
	
	
	@Test
	public void testRegisterUser() throws DuplicateLoginException{
		
		//Expected objects
		String userLogin = "unique";
		String userPassword="password";
		
		User userToSave = new User(userLogin,userPassword);
		userToSave.setPassword(userPassword);
		
		long userId = 3;
		User savedUser = new User(userLogin,userPassword);
		savedUser.setId(userId);
		
		//Mockito expectations
		when(userRepo.getUserByLogin(userLogin)).thenReturn(null);
		when(userRepo.saveAndFlush(userToSave)).thenReturn(savedUser);
		
		//Invoking
		User newUser = userService.registerUser(userToSave);
		
		//Validiting
		assertArrayEquals(new User[]{savedUser}, new User[]{newUser});
		assertArrayEquals(new String[]{savedUser.getLogin()}, new String[]{newUser.getLogin()});
	}
	
	
	@Test(expected = DuplicateLoginException.class)
	public void testRegisterUserWithDuplicateLogin() throws DuplicateLoginException{
		
		//Expected object
		String userLogin = "unique";
		String userPassword="password";
		
		User userToSave = new User();
		userToSave.setLogin(userLogin);
		userToSave.setPassword(userPassword);
		
		User userWithTheSameLogin = new User();
		userToSave.setLogin(userLogin);
		
		//Mockito expectations
		when(userRepo.getUserByLogin(userLogin)).thenReturn(userWithTheSameLogin);
		
		userService.registerUser(userToSave);
		
	}

}
