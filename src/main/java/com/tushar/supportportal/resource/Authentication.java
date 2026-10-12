package com.tushar.supportportal.resource;

import com.tushar.supportportal.domain.User;
import com.tushar.supportportal.repository.UserMongoRepos;
import com.tushar.supportportal.util.interfaces.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = { "/api/v1/auth" })
public class Authentication {

	private final UserMongoRepos userMongoRepos;
	private final JwtTokenProvider jwtTokenProvider;

	public Authentication(UserMongoRepos userMongoRepos, JwtTokenProvider jwtTokenProvider) {
		this.userMongoRepos = userMongoRepos;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> users() {
		Cookie cookie = new Cookie("tushar", "tushar");
		cookie.setMaxAge(3600);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		return ResponseEntity.ok().body(userMongoRepos.findAll());
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<Optional<User>> userById(@PathVariable String id) {
		return ResponseEntity.ok().body(userMongoRepos.findByUsername(id));
	}

	@GetMapping("/login")
	public ResponseEntity<?> login() {
		return ResponseEntity.ok().body(jwtTokenProvider.generateToken(null));
	}
}
