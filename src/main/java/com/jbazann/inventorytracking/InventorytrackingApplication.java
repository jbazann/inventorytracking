package com.jbazann.inventorytracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }) // TODO remove exclude
public class InventorytrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorytrackingApplication.class, args);
	}

}
