package ua.phonebook.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.phonebook.dao.BaseUserRepository;
import ua.phonebook.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private BaseUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = userRepository.getUserByLogin(login);
		if(user == null){
			throw new UsernameNotFoundException(
					String.format("User with login=%s was not found", login));
		}
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		
		org.springframework.security.core.userdetails.User secureUser = 
				new org.springframework.security.core.userdetails.User
						(user.getLogin(), user.getPassword(), 
								true, true, true, true, authorities);
		return secureUser;
	}

}
