package com.jbazann.inventorytracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaRepositories("com.jbazann.inventorytracking.db.repositories")
public class InventorytrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorytrackingApplication.class, args);
	}

}
