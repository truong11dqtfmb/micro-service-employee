package com.dqt.positionservice.controller;

import com.dqt.positionservice.client.EmployeeClient;
import com.dqt.positionservice.dto.PositionDTO;
import com.dqt.positionservice.model.ApiResponse;
import com.dqt.positionservice.model.Position;
import com.dqt.positionservice.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/position")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class PositionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionController.class);

    @Autowired
    private PositionService positionService;

    @Autowired
    private EmployeeClient employeeClient;


    @PostMapping
    public Position add(@RequestBody Position position) {
        LOGGER.info("Position: Add");
        return positionService.save(position);
    }
//    @GetMapping
//    public List<Position> findAll() {
//        LOGGER.info("Position: FindAll");
//        return positionService.findAll();
//    }

    @GetMapping
    public List<Position> findAll(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                                    @RequestParam(value = "keySearch", defaultValue = "", required = false) String keySearch) {
        LOGGER.info("Position: FindAll");


        List<Position> list = this.positionService.getAllDepartments(pageNumber, pageSize, sortBy, sortDir, keySearch);

        return list;
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
    public ApiResponse delete(@PathVariable Long id) {
        LOGGER.info("Position: DeleteById: {}", id);

        Boolean flag = positionService.delete(id);
        if (flag) {
            return new ApiResponse(true, "Delete Position successfully!");
        }
        return new ApiResponse(false, "You can not delete Position id: {} because contain constraint" + id);

    }

    @GetMapping("/dto")
    public List<PositionDTO> findAllDTO() {
        LOGGER.info("PositionDTO:  FindAllDTO");
        List<PositionDTO> positionDTOS = positionService.findAllDTO();
        return positionDTOS;
    }

    @GetMapping("/dto/{id}")
    public PositionDTO findByIdDTO(@PathVariable Long id) {
        LOGGER.info("PositionDTO: FindById: {}", id);
        PositionDTO dto = positionService.findByIdDTO(id);
        return dto;
    }

    @PutMapping("/dto/{id}")
    public PositionDTO updateDTO(@PathVariable Long id, @RequestBody Position position) {
        LOGGER.info("Position: UpdateById: {}", id);
        return positionService.updateDTO(position, id);
    }


}
