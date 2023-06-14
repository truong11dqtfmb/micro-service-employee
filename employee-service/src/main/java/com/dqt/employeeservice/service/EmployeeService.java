package com.dqt.employeeservice.service;

import com.dqt.employeeservice.client.DepartmentClient;
import com.dqt.employeeservice.client.PositionClient;
import com.dqt.employeeservice.dto.Department;
import com.dqt.employeeservice.dto.EmployeeDTO;
import com.dqt.employeeservice.dto.Position;
import com.dqt.employeeservice.exception.ResourceNotFoundException;
import com.dqt.employeeservice.model.Employee;
import com.dqt.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentClient departmentClient;

    @Autowired
    private PositionClient positionClient;

    public Employee save(Employee employee) {

        Long departmentId = employee.getDepartmentId();

        Long positionId = employee.getPositionId();

        List<Department> departments = departmentClient.findAllClient();

        List<Position> positions = positionClient.findAllClient();

        Boolean flagNotFound = false;

        for (Department department : departments) {
            if (department.getId().equals(departmentId)) {
                flagNotFound = true;
                break;
            }
        }

        for (Position position : positions) {
            if (position.getId().equals(positionId)) {
                flagNotFound = true;
                break;
            }
        }

        if (flagNotFound) {
            return employeeRepository.save(employee);
        }
        throw new ResourceNotFoundException("Department Or Position", "id", departmentId + " " + positionId);
    }

    public List<Employee> findAll() {

        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id + ""));

        return employee;
    }

    public List<Employee> findAllByDepartmentId(Long id) {
        return employeeRepository.findAllByDepartmentId(id);
    }

    public List<Employee> findAllByPositionId(Long id) {
        return employeeRepository.findAllByPositionId(id);
    }

    public List<EmployeeDTO> findAllDTO() {
        List<Employee> list = employeeRepository.findAll();

        List<EmployeeDTO> dtoList = list.stream().map(employee -> this.convertToDTO(employee)).collect(Collectors.toList());

        return dtoList;
    }




    public EmployeeDTO findByIdDTO(Long id) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id + ""));

        EmployeeDTO dto = this.convertToDTO(employee);

        return dto;
    }

    public Employee update(Employee employee, Long id) {

        Employee emp = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id + ""));

        Long departmentId = employee.getDepartmentId();
        Long positionId = employee.getPositionId();

        List<Department> departments = departmentClient.findAllClient();
        List<Position> positions = positionClient.findAllClient();

        Boolean flagNotFound = false;

        for (Department department : departments) {
            if (department.getId().equals(departmentId)) {
                flagNotFound = true;
                break;
            }
        }

        for (Position position : positions) {
            if (position.getId().equals(positionId)) {
                flagNotFound = true;
                break;
            }
        }

        if (flagNotFound) {
            emp.setName(employee.getName());
            emp.setAge(employee.getAge());
            emp.setPositionId(employee.getPositionId());
            emp.setDepartmentId(employee.getDepartmentId());

            return employeeRepository.save(emp);
        }
        throw new ResourceNotFoundException("Department Or Position", "id", departmentId + " " + positionId);
    }

    public Employee updateDTO(Employee employee, Long id) {

        Employee emp = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id + ""));

        Long departmentId = employee.getDepartmentId();
        Long positionId = employee.getPositionId();

        List<Department> departments = departmentClient.findAllClient();

        List<Position> positions = positionClient.findAllClient();

        Boolean flagNotFound = false;

        for (Department department : departments) {
            if (department.getId().equals(departmentId)) {
                flagNotFound = true;
                break;
            }
        }

        for (Position position : positions) {
            if (position.getId().equals(positionId)) {
                flagNotFound = true;
                break;
            }
        }

        if (flagNotFound) {
            emp.setName(employee.getName());
            emp.setAge(employee.getAge());
            emp.setPositionId(employee.getPositionId());
            emp.setDepartmentId(employee.getDepartmentId());

            Employee employ = employeeRepository.save(emp);

//            EmployeeDTO dto = this.convertToDTO(employ);

//            return dto;

            return employ;
        }
        throw new ResourceNotFoundException("Department Or Position", "id", departmentId + " " + positionId);
    }


    public Boolean delete(Long id) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id + ""));

        if (employee != null) {
            employeeRepository.delete(employee);
            return true;
        }
        return false;
    }


    public EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setAge(employee.getAge());

        Department department = departmentClient.findById(employee.getDepartmentId());
        Position position = positionClient.findById(employee.getPositionId());

        dto.setPosition(position);
        dto.setDepartment(department);
        return dto;
    }


    public Page<Employee> getAllEmployees(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String keySearch) {

        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Employee> employeePage = null;

        if (keySearch.equals("") || keySearch == "") {
            employeePage = this.employeeRepository.findAll(pageable);
        } else {
            employeePage = this.employeeRepository.findByNameContaining(keySearch, pageable);
        }

//        List<Employee> employeeList = employeePage.getContent();


//        List<EmployeeDTO> dtoList = employeeList.stream().map(employee -> this.convertToDTO(employee)).collect(Collectors.toList());

        return employeePage;
    }
}
