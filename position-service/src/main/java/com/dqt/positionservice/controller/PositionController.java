package com.dqt.positionservice.controller;

import com.dqt.positionservice.PositionServiceApplication;
import com.dqt.positionservice.client.EmployeeClient;
import com.dqt.positionservice.dto.Employee;
import com.dqt.positionservice.dto.PagePositionDTO;
import com.dqt.positionservice.dto.Pageable;
import com.dqt.positionservice.dto.PositionDTO;
import com.dqt.positionservice.kafka.KafkaMethod;
import com.dqt.positionservice.model.ApiResponse;
import com.dqt.positionservice.model.Position;
import com.dqt.positionservice.repository.PositionRepository;
import com.dqt.positionservice.service.PositionService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/position")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class PositionController {

    private String topicNamePosition = "topicPosition";
    private String topicNameDepartment = "topicDepartment";
    private String topicNameEmployee = "topicEmployee";

    List<Employee> list = PositionServiceApplication.listEmployeeKafka;


    @Autowired
    private KafkaTemplate<String, Object> template;

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionController.class);



    @Autowired
    private PositionService positionService;

    @Autowired
    private EmployeeClient employeeClient;

    @Autowired
    private PositionRepository positionRepository;


    @PostMapping
    public Position add(@RequestBody Position position) {
        LOGGER.info("Position: Add");
        return positionService.save(position);
    }

    @GetMapping
    public PagePositionDTO findAll(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                  @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                  @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                                  @RequestParam(value = "keySearch", defaultValue = "", required = false) String keySearch) {
        LOGGER.info("Position: FindAll");


        Page<Position> page = this.positionService.getAllDepartments(pageNumber, pageSize, sortBy, sortDir, keySearch);

        long totalPages = page.getTotalPages();
        long totalElements = page.getTotalElements();
        long size = page.getSize();
        long currentPage = pageNumber;

        List<Position> dtoList = page.getContent();


        return new PagePositionDTO(dtoList, new Pageable(totalPages,totalElements,size,currentPage));


    }
    @GetMapping("/client")
    public List<Position> findAllClient(){
        return this.positionService.findAll();
    }



    @GetMapping("/{id}")
    public Position findById(@PathVariable Long id) {
        LOGGER.info("Position: FindById: {}", id);
        return positionService.findById(id);
    }

    @PutMapping("/{id}")
    public Position update(@PathVariable Long id, @RequestBody Position position) {
        LOGGER.info("Position: UpdateById: {}", id);
        return positionService.update(position, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        LOGGER.info("Position: DeleteById: {}", id);

        Boolean flag = positionService.delete(id);
        if (flag) {
            return new ResponseEntity<>(new ApiResponse(true, "Delete Position successfully!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(false, "You can not delete Position id: {} because contain constraint" + id),HttpStatus.BAD_REQUEST);

    }

//    @GetMapping("/dto")
//    public List<PositionDTO> findAllDTO() {
//        LOGGER.info("PositionDTO:  FindAllDTO");
//        List<PositionDTO> positionDTOS = positionService.findAllDTO();
//        return positionDTOS;
//    }
//
//    @GetMapping("/dto/{id}")
//    public PositionDTO findByIdDTO(@PathVariable Long id) {
//        LOGGER.info("PositionDTO: FindById: {}", id);
//        PositionDTO dto = positionService.findByIdDTO(id);
//        return dto;
//    }
//
//    @PutMapping("/dto/{id}")
//    public PositionDTO updateDTO(@PathVariable Long id, @RequestBody Position position) {
//        LOGGER.info("Position: UpdateById: {}", id);
//        return positionService.updateDTO(position, id);
//    }

    @Async
    @GetMapping("/init")
    public void initKafka(){
        List<Position> list = positionRepository.findAll();
        list.forEach(pos -> {
            template.send(topicNamePosition, pos);
//            template.send(topicNameDepartment,emp);
            System.out.println("Init Producer: " + pos.getId());
        });
    }

    @GetMapping("/getEmployeeKafka")
    public List<Employee> employeeList(){

        return list;
    }





}
