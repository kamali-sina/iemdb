package com.example.iemdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import repository.IemdbRepository;

@SpringBootApplication
public class IemdbApplication {
	private static final String externalServicesUrl = "http://138.197.181.131:5000";
	private IemdbRepository repository = IemdbRepository.getInstance();
	public static void main(String[] args) {
		SpringApplication.run(IemdbApplication.class, args);
	}
}
