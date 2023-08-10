package com.POManager.PO.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.POManager.PO.models.ERole;
import com.POManager.PO.models.Role;



public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
