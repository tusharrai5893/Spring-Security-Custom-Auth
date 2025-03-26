package com.tushar.supportportal.resource;

import com.tushar.supportportal.domain.User;
import com.tushar.supportportal.repository.UserMongoRepos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = {"/api/v1/auth"})
public class Authentication {

	private final UserMongoRepos userMongoRepos;

	public Authentication(UserMongoRepos userMongoRepos) {this.userMongoRepos = userMongoRepos;}

	@GetMapping("/users")
	public ResponseEntity<List<User>> users() {
		return ResponseEntity.ok().body(userMongoRepos.findAll());
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<Optional<User>> userById(@PathVariable String id) {
		return ResponseEntity.ok().body(userMongoRepos.findByUsername(id));
	}
}