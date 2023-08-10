package com.POManager.PO.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.POManager.PO.models.User;

public interface UserRepository extends MongoRepository<User, String> {
	  Optional<User> findByUsername(String username);

	  Optional<User> findByUserId(String userId);
	  
	  Boolean existsByUsername(String username);

	  Boolean existsByEmail(String email);
	  
	  Boolean existsByUserId (String userId);
	  
	  void deleteByUserId(String userId);
}
