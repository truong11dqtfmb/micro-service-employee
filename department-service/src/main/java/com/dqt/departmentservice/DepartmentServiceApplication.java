package com.dqt.departmentservice;

import com.dqt.departmentservice.dto.Employee;
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
public class DepartmentServiceApplication {
	public static List<Employee> listEmployeeKafka = null;


	public static void main(String[] args) {

		listEmployeeKafka = new ArrayList<>();

		SpringApplication.run(DepartmentServiceApplication.class, args);
	}


	@Bean
	JsonMessageConverter converter() {
		return new JsonMessageConverter();
	}

	@Bean
	NewTopic produce(){
		return new NewTopic("topicDepartment",1, (short) 1);
	}

	@Bean
	NewTopic produceMethod(){
		return new NewTopic("topicDepartmentMethod",1, (short) 1);
	}
}
