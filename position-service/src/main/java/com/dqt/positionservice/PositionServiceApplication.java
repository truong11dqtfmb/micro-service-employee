package com.dqt.positionservice;

import com.dqt.positionservice.dto.Employee;
import com.dqt.positionservice.model.Position;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class PositionServiceApplication {

	public static List<Employee> listEmployeeKafka = null;


	public static void main(String[] args) {
		listEmployeeKafka = new ArrayList<>();
		SpringApplication.run(PositionServiceApplication.class, args);
	}

	@Bean
	JsonMessageConverter converter() {
		return new JsonMessageConverter();
	}

	@Bean
	NewTopic produce(){
		return new NewTopic("topicPosition",1, (short) 1);
	}

	@Bean
	NewTopic produceMethod(){
		return new NewTopic("topicPositionMethod",1, (short) 1);
	}
}
