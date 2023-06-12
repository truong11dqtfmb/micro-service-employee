package com.dqt.employeeservice.repository;

import com.dqt.employeeservice.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByDepartmentId(Long departmentId);

    List<Employee> findAllByPositionId(Long positionId);

    Page<Employee> findByNameContaining(String name, Pageable page);


    @Query(value = "SELECT ns.department_id, COUNT(ns.department_id) FROM employees AS ns GROUP BY ns.department_id ", nativeQuery = true)
    List<Object[]> groupByDepartment();

    @Query(value = "SELECT ns.position_id, COUNT(ns.position_id) FROM employees AS ns GROUP BY ns.position_id ", nativeQuery = true)
    List<Object[]> groupByPosition();

}
