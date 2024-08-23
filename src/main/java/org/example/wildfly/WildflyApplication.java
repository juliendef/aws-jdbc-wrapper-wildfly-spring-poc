package org.example.wildfly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WildflyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WildflyApplication.class, args);
	}

}
