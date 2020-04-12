package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ShortURLGenerator;
import com.example.demo.repository.ShortURLGeneratorRepository;
import java.util.*;
@Service
public class ShortURLGeneratorService {

	@Autowired
	private ShortURLGeneratorRepository shortRepo;
	
	// create a short url
	
	public ShortURLGenerator create(String longURL, int number)
	{
		return shortRepo.save(new ShortURLGenerator(longURL, number));
	}
	
	// create customized url
	public ShortURLGenerator createCustomized()
	{
		return shortRepo.save(new ShortURLGenerator());
	}
	
	// retrieve
	
	public List<ShortURLGenerator> getAllShort(){
		return shortRepo.findAll();
		
	}
	
	public String getLongURL(String shortURL)
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		return s.getLongURL();
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
	
	public void deleteAll()
	{
		shortRepo.deleteAll();
	}
	
	public void delete(String shortURL)
	{
		ShortURLGenerator s = shortRepo.findByShortURL(shortURL);
		shortRepo.delete(s);
	}
	
}
