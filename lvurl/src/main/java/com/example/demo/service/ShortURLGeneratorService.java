package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ShortURLGenerator;
import com.example.demo.repository.ShortURLGeneratorRepository;

import java.sql.Struct;
import java.util.*;
@Service
public class ShortURLGeneratorService {

	@Autowired
	private ShortURLGeneratorRepository shortRepo;
	
	// create a short url
	
	public ShortURLGenerator create(String userName, String longURL, int number)
	{
		while(shortRepo.findByNumber(number) != null){
			number++;
		}
		return shortRepo.save(new ShortURLGenerator(userName, longURL, number));
	}
	
	// create customized url
	public ShortURLGenerator createCustomized()
	{
		return shortRepo.save(new ShortURLGenerator());
	}
	
	// retrieve
	
	public List<ShortURLGenerator> getAllShortURLs(String userName)
	{
		return shortRepo.findByUserName(userName);
	}
	
	public String getLongURL(String shortURL)
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		if (s != null)
			return s.getLongURL();
		else
			return null;
	}
	
	
	public ShortURLGenerator getGenerator(String shortURL)
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		return s;
	}
	
	// update
	
	public ShortURLGenerator updateShortURL(String shortURL, int number)
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		s.setShortURL(number);
		return shortRepo.save(s);
	}
	
	public ShortURLGenerator updateLongURL(String shortURL, String longURL)
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		s.setLongURL(longURL);
		return shortRepo.save(s);
	}
	
	public ShortURLGenerator updateDuration(String shortURL, long duration)
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		s.setDuration(duration);
		return shortRepo.save(s);
	}
	
	// delete

	public void deleteAll(){
		shortRepo.deleteAll();
	}
	
	public void deleteUserURLs(String userName)
	{
		List<ShortURLGenerator> s = getAllShortURLs(userName);
		for( int i = 0; i < s.size(); i++)
		{
			shortRepo.delete(s.get(i));
		}
	}
	
	public void delete(String shortURL)
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		shortRepo.delete(s);
	}
	
}
