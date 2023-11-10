package com.highload.course.ga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaServiceApplication.class, args);
	}
}
