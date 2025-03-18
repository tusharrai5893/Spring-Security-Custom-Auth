package com.tushar.supportportal.repository;

import com.tushar.supportportal.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMongoRepos extends MongoRepository<User, String> {

	Optional<User> findByUsername(String id);
}