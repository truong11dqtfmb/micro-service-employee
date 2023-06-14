package com.dqt.departmentservice.controller;

import com.dqt.departmentservice.client.EmployeeClient;
import com.dqt.departmentservice.dto.DepartmentDTO;
import com.dqt.departmentservice.dto.PageDepartmentDTO;
import com.dqt.departmentservice.dto.Pageable;
import com.dqt.departmentservice.model.ApiResponse;
import com.dqt.departmentservice.model.Department;
import com.dqt.departmentservice.repository.DepartmentRepository;
import com.dqt.departmentservice.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeClient employeeClient;

    @Autowired
    private DepartmentRepository departmentRepository;


    @PostMapping
    public Department add(@RequestBody Department department) {
        LOGGER.info("Department: Add");
        return departmentService.save(department);
    }

//    @GetMapping
//    public List<Department> findAll(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
//                                    @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
//                                    @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
//                                    @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
//        LOGGER.info("Department: FindAll");
//
//
//        List<Department> postDTOList = this.departmentService.getAllDepartments(pageNumber, pageSize, sortBy, sortDir);
//
//        return postDTOList;
//    }

    @GetMapping
    public PageDepartmentDTO findAll(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                                    @RequestParam(value = "keySearch", defaultValue = "", required = false) String keySearch) {
        LOGGER.info("Department: FindAll");


        Page<Department> page = this.departmentService.getAllDepartments(pageNumber, pageSize, sortBy, sortDir, keySearch);

        List<Department> list = page.getContent();

        long totalPages = page.getTotalPages();
        long totalElements = page.getTotalElements();
        long size = page.getSize();
        long currentPage = pageNumber;


        return new PageDepartmentDTO(list, new Pageable(totalPages,totalElements,size,currentPage));

    }

    @GetMapping("/client")
    public List<Department> findAllClient(){
        return this.departmentRepository.findAll();
    }



    @GetMapping("/{id}")
    public Department findById(@PathVariable Long id) {
        LOGGER.info("Department: FindById: {}", id);
        return departmentService.findById(id);
    }

    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @RequestBody Department department) {
        LOGGER.info("Department: UpdateById: {}", id);
        return departmentService.update(department, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        LOGGER.info("Department: DeleteById: {}", id);

        Boolean flag = departmentService.delete(id);
        if (flag) {
            return new ResponseEntity<>(new ApiResponse(true, "Delete Department successfully!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(false, "You can not delete Department id: {} because contain constraint" + id),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/dto")
    public List<DepartmentDTO> findAllDTO() {
        LOGGER.info("DepartmentDTO:  FindAllDTO");
        List<DepartmentDTO> departmentDTOS = departmentService.findAllDTO();
        return departmentDTOS;
    }

    @GetMapping("/dto/{id}")
    public DepartmentDTO findByIdDTO(@PathVariable Long id) {
        LOGGER.info("DepartmentDTO: FindById: {}", id);
        DepartmentDTO dto = departmentService.findByIdDTO(id);
        return dto;
    }

    @PutMapping("/dto/{id}")
    public DepartmentDTO updateDTO(@PathVariable Long id, @RequestBody Department department) {
        LOGGER.info("Department: UpdateById: {}", id);
        return departmentService.updateDTO(department, id);
    }


}
