package com.example.gakusei.playzz.turf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlayzzTurfApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayzzTurfApplication.class, args);
	}

}
