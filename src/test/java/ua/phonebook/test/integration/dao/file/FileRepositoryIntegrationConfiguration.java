package ua.phonebook.test.integration.dao.file;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.TestPropertySource;

@Configuration
@Profile("filerepotest")
@ComponentScan(basePackages = "ua.phonebook.dao.filestorage")
public class FileRepositoryIntegrationConfiguration {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertiesResolver() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
}
