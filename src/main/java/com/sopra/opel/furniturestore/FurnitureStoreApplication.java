package com.sopra.opel.furniturestore;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
@EnableRetry
public class FurnitureStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FurnitureStoreApplication.class, args);
	}

}
