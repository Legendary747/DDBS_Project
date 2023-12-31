package com.ddbs.datacenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class DatacenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatacenterApplication.class, args);
		System.out.println("Hallo");
	}

}
