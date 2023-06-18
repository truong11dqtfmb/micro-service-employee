package com.dqt.employeeservice;

import com.dqt.employeeservice.dto.Department;
import com.dqt.employeeservice.dto.Position;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class EmployeeServiceApplication {


    public static List<Position> listPositionKafka = null;
    public static List<Department> listDepartmentKafka = null;


    public static void main(String[] args) {

        listDepartmentKafka = new ArrayList<>();
        listPositionKafka = new ArrayList<>();
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }

    @Bean
    JsonMessageConverter converter() {
        return new JsonMessageConverter();
    }

    @Bean
    NewTopic produce() {
        return new NewTopic("topicEmployee", 1, (short) 1);
    }

    @Bean
    NewTopic produceMethod() {
        return new NewTopic("topicEmployeeMethod", 1, (short) 1);
    }
}
