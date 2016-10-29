package ua.phonebook.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.phonebook.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void configure(WebSecurity web){
		web 
			.ignoring()
			.antMatchers(new String[]{"/resources/**"});
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/login","/registration").permitAll()
				.anyRequest().authenticated()
			.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/phonebook")
					.failureUrl("/login?error")
					.usernameParameter("login")
					.passwordParameter("password")
					.loginProcessingUrl("/login_process")
					.permitAll()
			.and()
				.logout()
					.deleteCookies("JSESSIONID")
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout");
	
	}
	
	
	@Bean
	public AuthenticationProvider daoAuthenticationProvider() {
	    DaoAuthenticationProvider impl = new DaoAuthenticationProvider();
	    impl.setUserDetailsService(userDetailsService);
	    impl.setPasswordEncoder(bcryptEncoder());
	    impl.setHideUserNotFoundExceptions(false);
	    return impl;
	}
	
	@Bean(name = "authenticationManager")
	public ProviderManager getProviderManager(){
		List<AuthenticationProvider> providers = new ArrayList<>();
		providers.add(daoAuthenticationProvider());
		ProviderManager providerManager = new ProviderManager(providers);
		return providerManager;
	}
	
	@Bean
	public PasswordEncoder bcryptEncoder(){
		return new BCryptPasswordEncoder();
	}
	
}
