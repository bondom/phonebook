package ua.phonebook.service.impl;

import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.model.User;
import ua.phonebook.service.UserService;
import ua.phonebook.service.exception.DuplicateLoginException;


@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private BaseUserRepository repository;
	

	@Override
	public void registerUser(User user) throws DuplicateLoginException{
		String enteredPassword = user.getPassword();
		String encodedPassword = new BCryptPasswordEncoder().encode(enteredPassword);
		user.setPassword(encodedPassword);
		try{
			repository.saveAndFlush(user);
		}catch(ConstraintViolationException e){
			throw new DuplicateLoginException();
		}
	}
}
