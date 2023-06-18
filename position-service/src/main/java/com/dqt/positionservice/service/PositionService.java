package com.dqt.positionservice.service;

import com.dqt.positionservice.PositionServiceApplication;
import com.dqt.positionservice.client.EmployeeClient;
import com.dqt.positionservice.dto.Employee;
import com.dqt.positionservice.dto.PositionDTO;
import com.dqt.positionservice.exception.ResourceNotFoundException;
import com.dqt.positionservice.kafka.KafkaMethod;
import com.dqt.positionservice.model.Position;
import com.dqt.positionservice.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PositionService {

    @Autowired
    private KafkaTemplate<String, Object> template;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EmployeeClient employeeClient;


    private String topicNamePosition = "topicPositionMethod";

    List<Employee> list = PositionServiceApplication.listEmployeeKafka;

    @KafkaListener(id = "topicEmployee-position-id", topics = "topicEmployee")
    public void getEmployee(Employee employee) {
        System.out.println(employee);
        list.add(employee);
    }

    @KafkaListener(id = "topicMethod-employee-id", topics = "topicEmployeeMethod")
    public void deleteDepartment(KafkaMethod kafkaMethod) {
        if(kafkaMethod.getMethod().equals("update")){
            Employee employeeeee = kafkaMethod.getEmployee();
            for(Employee employee : list){
                if(employee.getId().equals(kafkaMethod.getId())){
                   employee.setAge(employeeeee.getAge());
                    employee.setName(employeeeee.getName());
                    employee.setDepartmentId(employeeeee.getDepartmentId());
                    employee.setPositionId(employeeeee.getPositionId());
                }

            }
        }else if(kafkaMethod.getMethod().equals("delete")){
            list.remove(deleteEmployeeFromKafka(kafkaMethod.getId()));
        }
    }

    Employee deleteEmployeeFromKafka(Long id){
        return list.stream().filter(department -> department.getId().equals(id)).findFirst().get();
    }

    Employee updateEmployeeFromKafka(Long id,Employee employee){
        Employee emp = list.stream().filter(department -> department.getId().equals(id)).findFirst().get();

        emp.setAge(employee.getAge());
        emp.setName(employee.getName());
        emp.setPositionId(employee.getPositionId());
        emp.setDepartmentId(employee.getDepartmentId());
        return emp;
    }


    public List<Employee> getDistrict(){
        return list.stream().distinct().collect(Collectors.toList());
    }


    public Position save(Position position){

        return positionRepository.save(position);
    }

    public Position findById(Long positionId){
        System.out.println("FROM DB");
        Position position = this.positionRepository.findById(positionId).orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId + ""));

        return position;
    }

    public List<Position> findAll(){

        return positionRepository.findAll();
    }

    public Position update(Position position, Long positionId){
        Position dep = this.positionRepository.findById(positionId).orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId + ""));
        dep.setName(position.getName());
        return positionRepository.save(dep);
    }

    public PositionDTO convertToDto(Position position){
        PositionDTO dto = new PositionDTO();
        dto.setId(position.getId());
        dto.setName(position.getName());
        return dto;
    }

//    public List<PositionDTO> findAllDTO(){
//        List<Position> list = positionRepository.findAll();
//
//        List<PositionDTO> listDTO = list.stream().map(position -> this.convertToDto(position)).collect(Collectors.toList());
//
//        listDTO.forEach(position -> position.setEmployees(employeeClient.findByPosition(position.getId())));
//
//        return listDTO;
//    }
//
//    public PositionDTO findByIdDTO(Long positionId){
//        Position position = this.positionRepository.findById(positionId).orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId + ""));
//        PositionDTO dto = this.convertToDto(position);
//        dto.setEmployees(employeeClient.findByPosition(dto.getId()));
//        return dto;
//    }
//
//
//
//    public PositionDTO updateDTO(Position position, Long positionId){
//        Position dep = this.positionRepository.findById(positionId).orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId + ""));
//        dep.setName(position.getName());
//        Position depart = positionRepository.save(dep);
//        PositionDTO dto = this.convertToDto(depart);
//        dto.setEmployees(employeeClient.findByPosition(dto.getId()));
//        return dto;
//    }

    public Boolean delete(Long positionId){
        Position dep = this.positionRepository.findById(positionId).orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId + ""));

        Long depId = dep.getId();

//        List<Employee> list = employeeClient.findAllClient();
        List<Employee> list = this.getDistrict();

        Boolean flagFound = false;

        for (Employee employee: list) {
            if(employee.getPositionId().equals(depId)){
                flagFound = true;
                break;
            }
        }

        if(flagFound){
            return false;
        }

        KafkaMethod kafkaMethod = new KafkaMethod();
        kafkaMethod.setMethod("delete");
        kafkaMethod.setId(dep.getId());

        template.send(topicNamePosition, kafkaMethod);
        positionRepository.delete(dep);

        return true;
    }


    public Page<Position> getAllDepartments(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String keySearch) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Position> positionPage = null;

        if (keySearch.equals("") || keySearch == "") {
            positionPage = this.positionRepository.findAll(pageable);
        } else {
            positionPage = this.positionRepository.findByNameContaining(keySearch, pageable);
        }

//        List<Position> positionList = positionPage.getContent();

        return positionPage;


    }
}
