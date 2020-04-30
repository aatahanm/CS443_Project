package com.example.demo.repository;

//import java.com.example.demo.util.*;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;


@Repository
public interface UserRepository extends MongoRepository<User, String>{

	public User findByUserName(String userName);
	public User findByMail(String mail);
}
