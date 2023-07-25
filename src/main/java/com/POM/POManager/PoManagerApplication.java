package com.POM.POManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PoManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoManagerApplication.class, args);
		
	}

}
