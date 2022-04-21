package com.example.iemdb;

import input.DataReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IemdbApplication {
	private static final String externalServicesUrl = "http://138.197.181.131:5000";
	public static void main(String[] args) {
		try {
			DataReader.readDataFromUrl(externalServicesUrl);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		SpringApplication.run(IemdbApplication.class, args);
	}
}
