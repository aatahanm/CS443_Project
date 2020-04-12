package com.example.demo.controller;

import java.util.List;

// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ShortURLGenerator;
import com.example.demo.service.ShortURLGeneratorService;

@RestController
public class ShortURLGeneratorController {

	@Autowired
	private ShortURLGeneratorService shortService;
	
	@RequestMapping("/create")
	public String create(@RequestParam String longURL, @RequestParam int number)
	{
		ShortURLGenerator s = shortService.create(longURL, number);
		return s.toString();
	}
	
	@RequestMapping("/createSpecialShortURL")
	public String create(@RequestParam String longURL, @RequestParam String shortURL)
	{
		ShortURLGenerator s = shortService.createCustomized();
		s.setLongURL(longURL);
		s.setShortURL(shortURL);
		return s.toString();	
	}
	
	@RequestMapping("/get")
	public ShortURLGenerator getGenerator(@RequestParam String shortURL)
	{
		ShortURLGenerator s = shortService.getGenerator(shortURL);
		return s;
	}
	
	
	@RequestMapping("/getLongURL")
	public String getLongURL(@RequestParam String shortURL)
	{
		return shortService.getLongURL(shortURL);
	}
	
	
	@RequestMapping("/getAll")
	public List<ShortURLGenerator> getAllShorts()
	{
		return shortService.getAllShort();
	}
	
	
	@RequestMapping("/updateShortURL")
	public String updateShortURL(@RequestParam String shortURL, @RequestParam int number)
	{
		ShortURLGenerator s = shortService.updateShortURL(shortURL, number);
		return "Short URL " + shortURL + " updated as " +  s.getNumber();
	}
	
	@RequestMapping("/updateLongURL")
	public String updateLongURL(@RequestParam String shortURL, @RequestParam String longURL)
	{
		shortService.updateLongURL(shortURL, longURL);
		return "Long URL is updated";
	}
	
	@RequestMapping("/updateDuration")
	public String updateDuration(@RequestParam String shortURL, @RequestParam long duration)
	{
		shortService.updateDuration(shortURL, duration);
		return "Duration is updated";
	}
	
	@RequestMapping("/deleteAll")
	public String deleteAll()
	{
		shortService.deleteAll();
		return "Deleted";
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam String shortURL)
	{
		shortService.delete(shortURL);
		return shortURL + " is deleted";
	}
	
}
