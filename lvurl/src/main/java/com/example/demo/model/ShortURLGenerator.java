package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ShortURLGenerator {

	@Id
	String id;
	
	private String longURL;
	private String shortURL;
	private int number;
	
	public ShortURLGenerator(String longURL, int number)
	{
		this.longURL = longURL;
		this.number = number;
		setShortURL(number);
	}
	
	public ShortURLGenerator() {
		
	}

	public String getLongURL() {
		return longURL;
	}

	public void setLongURL(String longURL) {
		this.longURL = longURL;
	}

	public String getShortURL() {
		return shortURL;
	}

	public void setShortURL(int number) {
		this.number = number;
		this.shortURL = generator(number);
	}
	
	public void setShortURL(String shortURL)
	{
		this.shortURL = shortURL;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	private String generator(int number)
	{
		return Integer.toString(number);
	}
	// add Duration time ability
	
	public boolean setDuration(long duration)
	{
		return true;
	}
	
	public String toString() {
		return  "Short URL: " + shortURL + " Long URL: " + longURL;
	}
	
	
}
