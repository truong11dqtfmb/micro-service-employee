package com.dqt.positionservice.client;

import com.dqt.positionservice.dto.Employee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface EmployeeClient {
    @GetExchange("/api/employee/position/{positionId}")
    List<Employee> findByPosition(@PathVariable Long positionId);

    @GetExchange("/api/employee")
    List<Employee> findAll();

}
