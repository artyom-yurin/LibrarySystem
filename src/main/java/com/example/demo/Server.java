package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Server {

	private static String[] args;
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		Server.args = args;
		Server.context = SpringApplication.run(Server.class, args);
	}

	public static void restart() {
		// close previous context
		context.close();

		// and build new one
		Server.context = SpringApplication.run(Server.class, args);
	}
}
