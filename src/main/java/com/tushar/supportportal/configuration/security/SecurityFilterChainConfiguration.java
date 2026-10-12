package com.tushar.supportportal.configuration.security;

import com.tushar.supportportal.auth.filters.JwtOPRFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.tushar.supportportal.constant.SecurityConstant.PUBLIC_URLS;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfiguration {

	private final JwtOPRFilter jwtOPRFilter;

	public SecurityFilterChainConfiguration(JwtOPRFilter jwtOPRFilter) {
		this.jwtOPRFilter = jwtOPRFilter;
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable) // disable CSRF for stateless APIs
				.authorizeHttpRequests(ar -> {
					ar.requestMatchers(PUBLIC_URLS).permitAll();
					ar.anyRequest().authenticated();
				})
				.userDetailsService(getUserDetailsManager())
				.addFilterBefore(jwtOPRFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public InMemoryUserDetailsManager getUserDetailsManager() {
		return new InMemoryUserDetailsManager(User.builder()
				.username("tushar")
				.password("{noop}tushar")
				.roles("USER") // Converted to ROLE_USER ( ROLE_USER cannot start with ROLE_ (it is automatically added))
				.build());
	}

//	@Bean
//	public FilterRegistrationBean<Filter> loggingFilter() {
//		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
//		registrationBean.setFilter((request, response, chain) -> {
//			System.out.println("Request URI: " + ( (HttpServletRequest) request ).getRequestURI());
//			chain.doFilter(request, response);
//		});
//		return registrationBean;
//	}

}
