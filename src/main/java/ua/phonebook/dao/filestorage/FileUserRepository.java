package ua.phonebook.dao.filestorage;


import org.springframework.context.annotation.Profile;

import ua.phonebook.dao.BaseUserRepository;

@Profile("file")
public interface FileUserRepository extends BaseUserRepository{

}
