package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Document
public class ShortURLGenerator {

	@Id
	String id;
	String userName;
	private String longURL;
	private String shortURL;
	private int number;
	
	public ShortURLGenerator(String userName, String longURL, int number)
	{
		this.longURL = longURL;
		this.userName = userName;
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
	
	private static String generator(int number)
	{
		// Map to store 62 possible characters
		char map[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

		StringBuffer shorturl = new StringBuffer();

		while (number > 0) {
			shorturl.append(map[number % 62]);
			number = number / 62;
		}
		String input = shorturl.reverse().toString();
		try {
			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			//  of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext.substring(0,6);
		}
		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	// add Duration time ability
	
	public boolean setDuration(long duration)
	{
		return true;
	}
	
	public String toString() {
		return  "User Name: " + userName + " Short URL: " + shortURL + " Long URL: " + longURL;
	}

	public String getUserName() {
		return userName;
	}

	public static void main (String[] args){
		int n = 12345;
		String shorturl = generator(n);
		System.out.println("Generated short url in MD5: " + shorturl);
	}
}
