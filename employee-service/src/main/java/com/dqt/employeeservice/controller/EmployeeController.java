package com.dqt.employeeservice.controller;

import com.dqt.employeeservice.EmployeeServiceApplication;
import com.dqt.employeeservice.client.DepartmentClient;
import com.dqt.employeeservice.client.PositionClient;
import com.dqt.employeeservice.dto.*;
import com.dqt.employeeservice.kafka.KafkaMethod;
import com.dqt.employeeservice.model.ApiResponse;
import com.dqt.employeeservice.model.Employee;
import com.dqt.employeeservice.repository.EmployeeRepository;
import com.dqt.employeeservice.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class EmployeeController {
    private String topicNamePosition = "topicPosition";
    private String topicNameDepartment = "topicDepartment";
    private String topicNameEmployee = "topicEmployee";

    @Autowired
    private KafkaTemplate<String, Object> template;


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
    public PageEmployeeDTO findAllDTOEmployee(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                                            @RequestParam(value = "keySearch", defaultValue = "", required = false) String keySearch) {
        LOGGER.info("Employee: FindAllDTO");



        Page<Employee> page = this.employeeService.getAllEmployees(pageNumber, pageSize, sortBy, sortDir, keySearch);

        List<Employee> employeeList = page.getContent();

        List<EmployeeDTO> dtoList = employeeList.stream().map(this::convertToDTO).collect(Collectors.toList());

        long totalPages = page.getTotalPages();
        long totalElements = page.getTotalElements();
        long size = page.getSize();
        long currentPage = pageNumber;


        return new PageEmployeeDTO(dtoList, new Pageable(totalPages,totalElements,size,currentPage));

    }

    @GetMapping("/client")
    public List<Employee> findAllClient(){
        LOGGER.info("Client Employee: FindAllDTO");

        List<Employee> employeeList = employeeService.findAll();

        return employeeList;
    }

    @GetMapping("/{id}")
    public Employee findByIdDTO(@PathVariable Long id) {
        LOGGER.info("EmployeeDTO: FindById: {}", id);
        Employee dto = employeeService.findById(id);
        return dto;
    }

    @PutMapping("/{id}")
    public Employee updateDTO(@PathVariable Long id, @RequestBody Employee employee) {
        LOGGER.info("Employee: UpdateById: {}", id);
        Employee empl = employeeService.updateDTO(employee, id);
        return empl;
    }


    @GetMapping("/report/departments")
    public ResponseEntity<?> thongKeDepartments() throws JsonProcessingException {
        List<Object[]> depGroups = this.employeeRepository.groupByDepartment();

        List<DepartmentGroup> departmentDTOS = new ArrayList<>();

        for (int i = 0; i < depGroups.size(); i++) {
            Map<String, Object> map = new HashMap<>();


//            Department dto = departmentClient.findById(Long.valueOf(depGroups.get(i)[0].toString()));
            Department dto = this.deleteDepartmentFromKafka(Long.valueOf(depGroups.get(i)[0].toString()));
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

//            Position dto = positionClient.findById(Long.valueOf(posGroups.get(i)[0].toString()));
            Position dto = this.deletePositionFromKafka(Long.valueOf(posGroups.get(i)[0].toString()));
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

    public EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setAge(employee.getAge());

//        Department department = departmentClient.findById(employee.getDepartmentId());
        Department department = this.deleteDepartmentFromKafka(employee.getDepartmentId());
//        Position position = positionClient.findById(employee.getPositionId());
        Position position = this.deletePositionFromKafka(employee.getPositionId());

        dto.setPosition(position);
        dto.setDepartment(department);
        return dto;
    }

    @Async
    @GetMapping("/init")
    public void initKafka(){
        List<Employee> list = employeeRepository.findAll();
        list.forEach(emp -> {
            template.send(topicNameEmployee, emp);
//            template.send(topicNameDepartment,emp);
            System.out.println("Init Producer: " + emp.getId());
        });
    }

    List<Position> listPosition = EmployeeServiceApplication.listPositionKafka;
//
//    @KafkaListener(id = "topicPosition-position-id", topics = "topicPosition")
//    public void getPosition(Position position) {
//        System.out.println(position);
//        listPosition.add(position);
//    }
//
    List<Department> listDepartment = EmployeeServiceApplication.listDepartmentKafka;
//
//    @KafkaListener(id = "topicDepartment-department-id", topics = "topicDepartment")
//    public void getDepartment(Department department) {
//        System.out.println(department);
//        listDepartment.add(department);
//    }
//
//    @KafkaListener(id = "topicMethod-department-id", topics = "topicDepartmentMethod")
//    public void deleteDepartment(KafkaMethod kafkaMethod) {
//        if(kafkaMethod.getMethod().equals("delete")){
//            listDepartment.remove(deleteDepartmentFromKafka(kafkaMethod.getId()));
//        }
//    }
//
    Department deleteDepartmentFromKafka(Long id){
        return listDepartment.stream().filter(department -> department.getId().equals(id)).findFirst().get();
    }
//
//    @KafkaListener(id = "topicMethod-position-id", topics = "topicPositionMethod")
//    public void deletePosition(KafkaMethod kafkaMethod) {
//        if(kafkaMethod.getMethod().equals("delete")){
//            listDepartment.remove(deletePositionFromKafka(kafkaMethod.getId()));
//        }
//    }
//
    Position deletePositionFromKafka(Long id){
        return listPosition.stream().filter(position -> position.getId().equals(id)).findFirst().get();
    }


    public List<Department> getDistrictDepartment(){
        return listDepartment.stream().distinct().collect(Collectors.toList());
    }


    public List<Position> getDistrictPosition(){
        return listPosition.stream().distinct().collect(Collectors.toList());
    }

    @GetMapping("/getDepartmentKafka")
    public List<Department> departments(){
        return listDepartment;
    }

    @GetMapping("/getPositionKafka")
    public List<Position> positions(){
        return listPosition;
    }




}

