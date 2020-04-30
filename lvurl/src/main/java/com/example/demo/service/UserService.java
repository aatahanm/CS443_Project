package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import java.util.ArrayList;

@Service("UserService")
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	public User createUser(String userName, String password)
	{
		return userRepo.save(new User(userName, password));
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

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepo.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),new ArrayList<>());
	}
	
	public int updateNumber(String userName)
	{
		User u  = userRepo.findByUserName(userName);
		u.increase();
		return u.getNumber();
	}
}

