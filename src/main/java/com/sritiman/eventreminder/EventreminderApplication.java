package com.sritiman.eventreminder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class EventreminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventreminderApplication.class, args);
	}

}
