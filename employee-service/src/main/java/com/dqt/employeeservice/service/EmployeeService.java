package com.dqt.employeeservice.service;

import com.dqt.employeeservice.EmployeeServiceApplication;
import com.dqt.employeeservice.client.DepartmentClient;
import com.dqt.employeeservice.client.PositionClient;
import com.dqt.employeeservice.dto.Department;
import com.dqt.employeeservice.dto.EmployeeDTO;
import com.dqt.employeeservice.dto.Position;
import com.dqt.employeeservice.exception.ResourceNotFoundException;
import com.dqt.employeeservice.kafka.KafkaMethod;
import com.dqt.employeeservice.model.Employee;
import com.dqt.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private String topicNameEmployeeMethod = "topicEmployeeMethod";
    private String topicNameEmployee = "topicEmployee";


    @Autowired
    private DepartmentClient departmentClient;

    @Autowired
    private PositionClient positionClient;


    @Autowired
    private KafkaTemplate<String, Object> template;


    public Employee save(Employee employee) {

        Long departmentId = employee.getDepartmentId();

        Long positionId = employee.getPositionId();

//        List<Department> departments = departmentClient.findAllClient();
        List<Department> departments = this.getDistrictDepartment();

//        List<Position> positions = positionClient.findAllClient();
        List<Position> positions = this.getDistrictPosition();

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
            Employee employ =  employeeRepository.save(employee);

            template.send(topicNameEmployee, employ);
            return employ;
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

//    public Employee update(Employee employee, Long id) {
//
//        Employee emp = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id + ""));
//
//        Long departmentId = employee.getDepartmentId();
//        Long positionId = employee.getPositionId();
//
//        List<Department> departments = departmentClient.findAllClient();
//        List<Position> positions = positionClient.findAllClient();
//
//        Boolean flagNotFound = false;
//
//        for (Department department : departments) {
//            if (department.getId().equals(departmentId)) {
//                flagNotFound = true;
//                break;
//            }
//        }
//
//        for (Position position : positions) {
//            if (position.getId().equals(positionId)) {
//                flagNotFound = true;
//                break;
//            }
//        }
//
//        if (flagNotFound) {
//            emp.setName(employee.getName());
//            emp.setAge(employee.getAge());
//            emp.setPositionId(employee.getPositionId());
//            emp.setDepartmentId(employee.getDepartmentId());
//
//            return employeeRepository.save(emp);
//        }
//        throw new ResourceNotFoundException("Department Or Position", "id", departmentId + " " + positionId);
//    }

    public Employee updateDTO(Employee employee, Long id) {

        Employee emp = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id + ""));

        Long departmentId = employee.getDepartmentId();
        Long positionId = employee.getPositionId();

//        List<Department> departments = departmentClient.findAllClient();
        List<Department> departments = this.getDistrictDepartment();

//        List<Position> positions = positionClient.findAllClient();
        List<Position> positions = this.getDistrictPosition();

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

            KafkaMethod kafkaMethod = new KafkaMethod();
            kafkaMethod.setMethod("update");
            kafkaMethod.setId(employ.getId());
            kafkaMethod.setEmployee(employ);

            template.send(topicNameEmployeeMethod, kafkaMethod);


            return employ;
        }
        throw new ResourceNotFoundException("Department Or Position", "id", departmentId + " " + positionId);
    }


    public Boolean delete(Long id) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id + ""));

        if (employee != null) {
            employeeRepository.delete(employee);

            KafkaMethod kafkaMethod = new KafkaMethod();
            kafkaMethod.setMethod("delete");
            kafkaMethod.setId(id);

            template.send(topicNameEmployeeMethod, kafkaMethod);
            return true;
        }
        return false;
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

    List<Position> listPosition = EmployeeServiceApplication.listPositionKafka;

    List<Department> listDepartment = EmployeeServiceApplication.listDepartmentKafka;

    @KafkaListener(id = "topicPosition-position-id", topics = "topicPosition")
    public void getPosition(Position position) {
        System.out.println(position);
        listPosition.add(position);
    }


    @KafkaListener(id = "topicDepartment-department-id", topics = "topicDepartment")
    public void getDepartment(Department department) {
        System.out.println(department);
        listDepartment.add(department);
    }

    @KafkaListener(id = "topicMethod-department-id", topics = "topicDepartmentMethod")
    public void deleteDepartment(KafkaMethod kafkaMethod) {
        if(kafkaMethod.getMethod().equals("delete")){
            listDepartment.remove(deleteDepartmentFromKafka(kafkaMethod.getId()));
        }
    }

    Department deleteDepartmentFromKafka(Long id){
        return listDepartment.stream().filter(department -> department.getId().equals(id)).findFirst().get();
    }

    @KafkaListener(id = "topicMethod-position-id", topics = "topicPositionMethod")
    public void deletePosition(KafkaMethod kafkaMethod) {
        if(kafkaMethod.getMethod().equals("delete")){
            listDepartment.remove(deletePositionFromKafka(kafkaMethod.getId()));
        }
    }

    Position deletePositionFromKafka(Long id){
        return listPosition.stream().filter(position -> position.getId().equals(id)).findFirst().get();
    }


    public List<Department> getDistrictDepartment(){
        return listDepartment.stream().distinct().collect(Collectors.toList());
    }


    public List<Position> getDistrictPosition(){
        return listPosition.stream().distinct().collect(Collectors.toList());
    }


}
