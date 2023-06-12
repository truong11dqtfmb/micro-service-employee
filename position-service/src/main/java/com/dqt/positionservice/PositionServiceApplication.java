package com.dqt.positionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PositionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PositionServiceApplication.class, args);
	}

}
