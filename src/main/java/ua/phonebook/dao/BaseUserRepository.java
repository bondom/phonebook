package ua.phonebook.dao;


import ua.phonebook.model.User;

public interface BaseUserRepository {
	public User getUserByLogin(String login);
	public User saveAndFlush(User user);
}
