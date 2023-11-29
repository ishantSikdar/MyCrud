package com.opalina.MyCrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class MyCrudApplication {
	public static final Logger logger = LoggerFactory.getLogger("com.opalina.MyCrud");
	public static void main(String[] args)  {
		logger.info("Main package logging");
		SpringApplication.run(MyCrudApplication.class, args);
	}

}
