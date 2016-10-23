package ua.phonebook.dao.filestorage;


import org.springframework.context.annotation.Profile;

import ua.phonebook.dao.BasePhoneBookRecordRepository;

@Profile("file")
public interface FilePhoneBookRecordRepository extends BasePhoneBookRecordRepository{

}
