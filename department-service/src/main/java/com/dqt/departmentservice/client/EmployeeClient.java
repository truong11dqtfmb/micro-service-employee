package com.dqt.departmentservice.client;

import com.dqt.departmentservice.dto.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface EmployeeClient {
    @GetExchange("/api/employee/department/{departmentId}")
    List<Employee> findByDepartment(@PathVariable Long departmentId);

//    @GetExchange("/api/employee")
//    List<Employee> findAll();

    @GetExchange("/api/employee/client")
    List<Employee> findAllClient();

}
