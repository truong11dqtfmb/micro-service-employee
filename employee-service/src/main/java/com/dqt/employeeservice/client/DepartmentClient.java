package com.dqt.employeeservice.client;

import com.dqt.employeeservice.dto.Department;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface DepartmentClient {
    @GetExchange("/api/department/{id}")
    Department findById(@PathVariable Long id);

    @GetExchange("/api/department/client")
    List<Department> findAllClient();

}
