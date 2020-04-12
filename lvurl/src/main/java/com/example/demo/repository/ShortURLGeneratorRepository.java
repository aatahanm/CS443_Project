package com.example.demo.repository;

//import java.util.*;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ShortURLGenerator;
@Repository
public interface ShortURLGeneratorRepository extends MongoRepository<ShortURLGenerator, String>{

	public ShortURLGenerator findByShortURL(String shortURL);
	//public String getLongURL(String shortURL);
	public ShortURLGenerator findByLongURL(String longURL);
	public ShortURLGenerator findByNumber(int number);
}
