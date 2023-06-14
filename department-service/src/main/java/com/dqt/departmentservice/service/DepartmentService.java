package com.dqt.departmentservice.service;

import com.dqt.departmentservice.client.EmployeeClient;
import com.dqt.departmentservice.dto.Employee;
import com.dqt.departmentservice.exception.ResourceNotFoundException;
import com.dqt.departmentservice.model.Department;
import com.dqt.departmentservice.dto.DepartmentDTO;
import com.dqt.departmentservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeClient employeeClient;

    public Department save(Department department) {

        return departmentRepository.save(department);
    }

    public Department findById(Long departmentId) {

        Department department = this.departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId + ""));

        return department;
    }

    public List<Department> findAll() {

        return departmentRepository.findAll();
    }

    public DepartmentDTO convertToDto(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    public List<DepartmentDTO> findAllDTO() {
        List<Department> list = departmentRepository.findAll();

        List<DepartmentDTO> listDTO = list.stream().map(department -> this.convertToDto(department)).collect(Collectors.toList());
//        List<DepartmentDTO> listDTO = list.stream().map(this::convertToDto).collect(Collectors.toList());

        listDTO.forEach(department -> department.setEmployees(employeeClient.findByDepartment(department.getId())));

        return listDTO;
    }

    public DepartmentDTO findByIdDTO(Long departmentId) {
        Department department = this.departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId + ""));
        DepartmentDTO dto = this.convertToDto(department);
        dto.setEmployees(employeeClient.findByDepartment(dto.getId()));
        return dto;
    }

    public Department update(Department department, Long departmentId) {
        Department dep = this.departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId + ""));
        dep.setName(department.getName());
        return departmentRepository.save(dep);
    }

    public DepartmentDTO updateDTO(Department department, Long departmentId) {
        Department dep = this.departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId + ""));
        dep.setName(department.getName());
        Department depart = departmentRepository.save(dep);
        DepartmentDTO dto = this.convertToDto(depart);
        dto.setEmployees(employeeClient.findByDepartment(dto.getId()));
        return dto;
    }


    public Boolean delete(Long departmentId) {
        Department dep = this.departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId + ""));

        Long depId = dep.getId();

        List<Employee> list = employeeClient.findAllClient();

        Boolean flagFound = false;

        for (Employee employee : list) {
            if (employee.getDepartmentId().equals(depId)) {
                flagFound = true;
                break;
            }
        }

        if (flagFound) {
            return false;
        }
        departmentRepository.delete(dep);

        return true;
    }

    public Page<Department> getAllDepartments(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String keySearch) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Department> departmentPage = null;

        if (keySearch.equals("") || keySearch == "") {
            departmentPage = this.departmentRepository.findAll(pageable);
        } else {
            departmentPage = this.departmentRepository.findByNameContaining(keySearch, pageable);
        }

//        List<Department> deparmentList = departmentPage.getContent();

        return departmentPage;


    }
}
