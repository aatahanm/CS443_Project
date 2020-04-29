package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.*;

import com.example.demo.model.ShortURLGenerator;
import com.example.demo.service.ShortURLGeneratorService;


@RestController
public class ShortURLGeneratorController {

	@Autowired
	private ShortURLGeneratorService shortService;
	

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void redirect(@PathVariable String id, HttpServletResponse resp) throws Exception
    {
    	
    	final String url = shortService.getLongURL(id);
        if (url != null)
            resp.sendRedirect(url);
        else
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
	@RequestMapping("/")
	public String homePage()
	{
		return "Welcome, It works!";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@RequestParam String userName, @RequestParam String longURL, @RequestParam int number, HttpServletResponse resp)
	{
		ShortURLGenerator s = shortService.create(userName, longURL, number);
		return s.toString();
	}
	
	@RequestMapping("/createSpecialShortURL")
	public String createSpecial(@RequestParam String longURL, @RequestParam String shortURL)
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
	public List<ShortURLGenerator> getAllShorts(@RequestParam String userName)
	{
		return shortService.getAllShortURLs(userName);
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
		return "All database has been deleted!";
	}
	
	@RequestMapping("/deleteUserURL")
	public String deleteUserURL(@RequestParam String userName)
	{
		shortService.deleteUserURLs(userName);
		return userName +  "All short URLs are deleted!";
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam String shortURL)
	{
		shortService.delete(shortURL);
		return shortURL + " is deleted";
	}
	
}
