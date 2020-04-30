package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

// import java.com.example.demo.util.List;

import com.example.demo.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.*;
import com.example.demo.service.ShortURLGeneratorService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;


@RestController
public class ShortURLGeneratorController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ShortURLGeneratorService shortService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtTokenUtil;
	
	

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
	
	@RequestMapping("/create/User")
	public String userCreation(@RequestParam String userName, @RequestParam String password)
	{
		com.example.demo.model.User u = userService.createUser(userName, password);
		return u.toString();
		
	}
	@RequestMapping("/create/URL")
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

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
}

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserService myUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
				.authorizeRequests()
					.antMatchers("/authenticate","/create/User","/").permitAll()
					.antMatchers("/getAll","/create/URL").denyAll()
					.anyRequest().authenticated().and().
				exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}

}
