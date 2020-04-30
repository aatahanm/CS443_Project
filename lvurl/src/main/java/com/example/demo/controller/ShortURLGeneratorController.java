package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.*;

import com.example.demo.model.*;
import com.example.demo.service.ShortURLGeneratorService;
import com.example.demo.service.UserService;


@RestController
public class ShortURLGeneratorController {

	@Autowired
	private ShortURLGeneratorService shortService;
	
	@Autowired
	private UserService userService;
	
	

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
	
	@RequestMapping("/createUser")
	public String userCreation(@RequestParam String userName)
	{
		com.example.demo.model.User u = userService.createUser(userName);
		return u.toString();
		
	}
	@RequestMapping("/create")
	public String createURL(@RequestParam String userName, @RequestParam String longURL, HttpServletResponse resp) throws Exception
	{
		ShortURLGenerator s;
		//try
		//{
			s = shortService.create(userName, longURL);
		//}
		//catch(Exception x)
		//{
		//	resp.sendError(HttpServletResponse.SC_CONFLICT);
		//	return "Insertion failed!";
	//	}
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
	
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public List<ShortURLGenerator> getAllShorts(@RequestParam String userName, HttpServletResponse resp) throws Exception
	{
		com.example.demo.model.User u = userService.getUser(userName);
		List<ShortURLGenerator> lst =  shortService.getAllShortURLs(u.getUserName());
		
		if( lst != null)
		{
			resp.setStatus(resp.SC_OK);
			return lst;	
		}
		else
		{
			resp.sendError(resp.SC_NOT_FOUND);
			return null;
		}
	}
	
	
	@RequestMapping(value = "/updateShortURL", method = RequestMethod.PATCH)
	public String updateShortURL(@RequestParam String shortURL, HttpServletResponse resp)
	{
		ShortURLGenerator s = shortService.updateShortURL(shortURL);
		if( s!= null)
			resp.setStatus(HttpServletResponse.SC_OK);
		return "Short URL " + shortURL + " updated as " +  s.getNumber();
	}
	
	@RequestMapping(value = "/updateLongURL", method = RequestMethod.PATCH)
	public String updateLongURL(@RequestParam String shortURL, @RequestParam String longURL, HttpServletResponse resp)
	{
		if ( shortService.updateLongURL(shortURL, longURL) != null)
			resp.setStatus(HttpServletResponse.SC_OK);
		return "Long URL is updated";
	}
	
	@RequestMapping(value = "/updateDuration",  method = RequestMethod.PATCH)
	public String updateDuration(@RequestParam String shortURL, @RequestParam long duration, HttpServletResponse resp)
	{
		if( shortService.updateDuration(shortURL, duration) != null)
			resp.setStatus(HttpServletResponse.SC_OK);
		return "Duration is updated";
	}

	/*
	@RequestMapping("/deleteAll")
	public String deleteAll()
	{
		shortService.deleteAll();
		return "All database has been deleted!";
	}
	*/
	
	@RequestMapping(value = "/deleteUserURL", method = RequestMethod.DELETE)
	public String deleteUserURL(@RequestParam String userName, HttpServletResponse resp) throws Exception
	{
		try
		{
			shortService.deleteUserURLs(userName);
		}
		catch(Exception x)
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		resp.setStatus(HttpServletResponse.SC_OK);
		return userName +  "All short URLs are deleted!";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public String delete(@RequestParam String shortURL, HttpServletResponse resp) throws Exception
	{
		try
		{
			shortService.delete(shortURL);
		}
		catch(Exception x)
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		resp.setStatus(HttpServletResponse.SC_OK);
		return shortURL + " is deleted";
	}
	
}
