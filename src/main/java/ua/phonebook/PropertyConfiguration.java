package ua.phonebook;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Separate {@code Configuration} class with specified {@code Profile} 
 * is needed for some tests,
 * which don't need external .properties file, for example unit tests of web layer.
 */
@Configuration
@Profile({"!test"})
@PropertySource(value = {"${lardi.conf}"})
public class PropertyConfiguration {

}
