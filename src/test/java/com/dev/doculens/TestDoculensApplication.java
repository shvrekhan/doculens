package com.dev.doculens;

import org.springframework.boot.SpringApplication;

public class TestDoculensApplication {

	public static void main(String[] args) {
		SpringApplication.from(DoculensApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
