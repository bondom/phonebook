package ua.phonebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@PropertySource(value = { "${lardi.conf}" })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}