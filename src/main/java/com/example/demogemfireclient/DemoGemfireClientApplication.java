package com.example.demogemfireclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@SpringBootApplication
@EnableGemfireRepositories("com.example.demogemfireclient")
public class DemoGemfireClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoGemfireClientApplication.class, args);
	}
}

