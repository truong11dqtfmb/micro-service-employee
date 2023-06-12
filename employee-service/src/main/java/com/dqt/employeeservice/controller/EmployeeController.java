package com.dqt.employeeservice.controller;

import com.dqt.employeeservice.client.DepartmentClient;
import com.dqt.employeeservice.client.PositionClient;
import com.dqt.employeeservice.dto.Department;
import com.dqt.employeeservice.dto.DepartmentGroup;
import com.dqt.employeeservice.dto.EmployeeDTO;
import com.dqt.employeeservice.dto.PositionGroup;
import com.dqt.employeeservice.model.ApiResponse;
import com.dqt.employeeservice.model.Employee;
import com.dqt.employeeservice.repository.EmployeeRepository;
import com.dqt.employeeservice.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentClient departmentClient;

    @Autowired
    private PositionClient positionClient;

    @PostMapping
    public Employee add(@RequestBody Employee employee) {
        LOGGER.info("Employee: Add");
        return employeeService.save(employee);
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable Long id) {
        LOGGER.info("Employee: DeleteById: {}", id);

        Boolean flag = employeeService.delete(id);
        if (flag) {
            return new ApiResponse(true, "Delete Employee Successfully!");
        }
        return new ApiResponse(false, "Delete Employee Failed!");
    }


    @GetMapping("/department/{departmentId}")
    public List<Employee> findByDepartment(@PathVariable Long departmentId) {
        LOGGER.info("Employee:  FindAllByDepartmentID: {}", departmentId);
        return employeeService.findAllByDepartmentId(departmentId);
    }

    @GetMapping("/position/{positionId}")
    public List<Employee> findByPosition(@PathVariable Long positionId) {
        LOGGER.info("Employee:  FindAllByPositionID: {}", positionId);
        return employeeService.findAllByPositionId(positionId);
    }

    @GetMapping
    public List<EmployeeDTO> findAllDTOEmployee(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                                            @RequestParam(value = "keySearch", defaultValue = "", required = false) String keySearch) {
        LOGGER.info("EmployeeDTO: FindAllDTO");


        List<EmployeeDTO> list = this.employeeService.getAllEmployees(pageNumber, pageSize, sortBy, sortDir, keySearch);

        return list;

    }

    @GetMapping("/{id}")
    public EmployeeDTO findByIdDTO(@PathVariable Long id) {
        LOGGER.info("EmployeeDTO: FindById: {}", id);
        EmployeeDTO dto = employeeService.findByIdDTO(id);
        return dto;
    }

    @PutMapping("/{id}")
    public EmployeeDTO updateDTO(@PathVariable Long id, @RequestBody Employee employee) {
        LOGGER.info("Employee: UpdateById: {}", id);
        EmployeeDTO empl = employeeService.updateDTO(employee, id);
        return empl;
    }


    @GetMapping("/report/departments")
    public ResponseEntity<?> thongKeDepartments() throws JsonProcessingException {
        List<Object[]> depGroups = this.employeeRepository.groupByDepartment();

        List<DepartmentGroup> departmentDTOS = new ArrayList<>();

        for (int i = 0; i < depGroups.size(); i++) {
            Map<String, Object> map = new HashMap<>();


            Department dto = departmentClient.findById(Long.valueOf(depGroups.get(i)[0].toString()));
            Long quantity = Long.valueOf(depGroups.get(i)[1].toString());
            String departmentName = dto.getName();

            DepartmentGroup departmentGroup = new DepartmentGroup();
            departmentGroup.setDepartmentName(departmentName);
            departmentGroup.setQuantity(quantity);

            departmentDTOS.add(departmentGroup);
        }

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(departmentDTOS);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping("/report/positions")
    public ResponseEntity<?> thongKePosition() throws JsonProcessingException {
        List<Object[]> posGroups = this.employeeRepository.groupByPosition();

        List<PositionGroup> positionDTOS = new ArrayList<>();

        for (int i = 0; i < posGroups.size(); i++) {
            Map<String, Object> map = new HashMap<>();

            Department dto = departmentClient.findById(Long.valueOf(posGroups.get(i)[0].toString()));
            Long quantity = Long.valueOf(posGroups.get(i)[1].toString());
            String positionName = dto.getName();

            PositionGroup positionGroup = new PositionGroup();
            positionGroup.setpositionName(positionName);
            positionGroup.setQuantity(quantity);

            positionDTOS.add(positionGroup);
        }

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(positionDTOS);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }




}

