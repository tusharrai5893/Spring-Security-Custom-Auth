package com.tushar.supportportal.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class SecurityFilterChain {

	public DefaultSecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.
				userDetailsService(getUserDetailsManager())
				.build();
	}

	@Bean
	public InMemoryUserDetailsManager getUserDetailsManager() {
		return new InMemoryUserDetailsManager(User.builder()
				.username("tushar")
				.password("{noop}tushar")
				.roles("USER")
				.build());
	}

}