package ua.phonebook.test.integration.dao;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@Configuration
@Profile("filerepotest")
@ComponentScan(basePackages = "ua.phonebook.dao.filestorage")
@TestPropertySource(properties = {"filestorage.phonebookrecords.path=D:/phonebookrepotest.json",
								  "filestorage.users.path=D:/userrepotest.json"})
public class FileRepositoryIntegrationConfiguration {

}
