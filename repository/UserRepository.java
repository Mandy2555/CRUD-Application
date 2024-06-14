package com.myapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.myapp.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
	@Query(value = "{'_id' : ?0}")
	public User findByCustomUserId(String userId );


	public List<User> findByFirstName(String firstName);
	
	
}


