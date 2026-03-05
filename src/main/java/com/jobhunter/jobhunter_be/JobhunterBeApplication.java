package com.jobhunter.jobhunter_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JobhunterBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobhunterBeApplication.class, args);
	}

}
