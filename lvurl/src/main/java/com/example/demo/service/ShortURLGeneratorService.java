package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ShortURLGenerator;
import com.example.demo.model.User;
import com.example.demo.repository.ShortURLGeneratorRepository;
import com.example.demo.repository.UserRepository;

//import java.sql.Struct;
import java.util.*;
@Service
public class ShortURLGeneratorService {

	@Autowired
	private ShortURLGeneratorRepository shortRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	// create a short url
	
	public ShortURLGenerator create(String userName, String longURL, boolean customized, String customizedURL)
	{
		User u = userRepo.findByUserName(userName);
		if( customized)
		{
			if(shortRepo.findByShortURL(customizedURL) == null)
				return shortRepo.save(new ShortURLGenerator(userName, longURL, u.getNumber(), customized, customizedURL));
			else
				return null;
		}
		else
		{
			u.increase();
			userRepo.save(u);
			return shortRepo.save(new ShortURLGenerator(userName, longURL, u.getNumber(), false, ""));
		}
	}
	
	// retrieve
	
	public List<ShortURLGenerator> getAllShortURLs(String userName)
	{
		return shortRepo.findByUserName(userName);
	}
	
	public String getLongURL(String shortURL)
	{
		ShortURLGenerator s;
		try
		{
			s = finder(shortURL);
		}
		catch(Exception ex)
		{
			return null;
		}
		
		return s.getLongURL();
	}
	
	
	public ShortURLGenerator getGenerator(String shortURL)
	{
		ShortURLGenerator s;
		try
		{
			return finder(shortURL);
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	// update
	
	public ShortURLGenerator updateShortURL(String shortURL)
	{
		ShortURLGenerator s;
		try
		{
			s = finder(shortURL);
		}
		catch(Exception ex)
		{
			return null;
		}
		int number = s.getNumber();
		number++;
		s.setShortURL(number);
		return shortRepo.save(s);
	}
	
	public ShortURLGenerator updateLongURL(String shortURL, String longURL)
	{
		ShortURLGenerator s;
		try
		{
			s = finder(shortURL);
		}
		catch(Exception ex)
		{
			return null;
		}
		s.setLongURL(longURL);
		return shortRepo.save(s);
	}
	
	public ShortURLGenerator updateDuration(String shortURL, long duration)
	{
		ShortURLGenerator s;
		try
		{
			s = finder(shortURL);
		}
		catch(Exception ex)
		{
			return null;
		}
		s.setDuration(duration);
		return shortRepo.save(s);
	}
	
	private ShortURLGenerator finder(String shortURL) throws Exception
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		if( s == null)
			throw new Exception();
		else
			return s;
	}
	
	// delete

	/*
	public void deleteAll(){
		shortRepo.deleteAll();
	}
	*/
	
	
	public void deleteUserURLs(String userName) throws Exception
	{
		List<ShortURLGenerator> s = getAllShortURLs(userName);
		if( s.size() <= 0)
			throw new Exception();
		for( int i = 0; i < s.size(); i++)
		{
			shortRepo.delete(s.get(i));
		}
	}
	
	public void delete(String shortURL) throws Exception
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		if( s == null)
			throw new Exception();
		shortRepo.delete(s);
	}
	
}
