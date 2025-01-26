package com.project.onlybuns;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class OnlybunsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlybunsApplication.class, args);
	}

}
