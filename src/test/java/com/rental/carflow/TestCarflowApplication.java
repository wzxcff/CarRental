package com.rental.carflow;

import org.springframework.boot.SpringApplication;

public class TestCarflowApplication {

	public static void main(String[] args) {
		SpringApplication.from(CarflowApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
