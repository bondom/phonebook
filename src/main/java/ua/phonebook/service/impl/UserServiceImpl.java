package ua.phonebook.service.impl;


import java.util.concurrent.locks.ReentrantLock;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.User;
import ua.phonebook.service.UserService;
import ua.phonebook.service.exception.DuplicateLoginException;


@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private BaseUserRepository repository;
	
	private final ReentrantLock lock = new ReentrantLock(true);

	@Override
	public void registerUser(User user) throws DuplicateLoginException{
		String enteredPassword = user.getPassword();
		String encodedPassword = new BCryptPasswordEncoder().encode(enteredPassword);
		user.setPassword(encodedPassword);
		
		lock.lock();
		try{
			User userInDataStorageWithTheSameLogin = 
					repository.getUserByLogin(user.getLogin());
			if(userInDataStorageWithTheSameLogin == null){
				//flushing is need, because is the time 
				//between invoking lock.unlock() and flushing,
				//during which other user can check data storage for user with 
				//the same login, but previous user isn't yet saved in data storage
				repository.saveAndFlush(user);
			}else{
				throw new DuplicateLoginException();
			}
		}finally{
			lock.unlock();
		}
	}
}
