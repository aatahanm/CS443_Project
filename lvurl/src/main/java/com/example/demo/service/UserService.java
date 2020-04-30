package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	public User createUser(String userName)
	{
		return userRepo.save(new User(userName));
	}
	
	public User getUser(String userName)
	{
		return userRepo.findByUserName(userName);
	}
	public int getNumber(String userName)
	{
		User u  = userRepo.findByUserName(userName);
		return u.getNumber();
	}
	
	public int updateNumber(String userName)
	{
		User u  = userRepo.findByUserName(userName);
		u.increase();
		return u.getNumber();
	}
}
