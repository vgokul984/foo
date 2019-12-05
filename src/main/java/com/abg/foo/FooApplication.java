package com.abg.foo;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FooApplication {

	public static void main(String[] args) {
		Properties props = System.getProperties();
		props.put("https.proxyHost", "170.225.13.50");
		props.put("https.proxyPort", "8080");
		SpringApplication.run(FooApplication.class, args);
	}

}
