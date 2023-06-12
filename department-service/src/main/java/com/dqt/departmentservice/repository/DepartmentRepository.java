package com.dqt.departmentservice.repository;

import com.dqt.departmentservice.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Page<Department> findByNameContaining(String name, Pageable page);

}
