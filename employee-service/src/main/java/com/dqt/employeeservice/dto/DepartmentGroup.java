package com.dqt.employeeservice.dto;

public class DepartmentGroup {

    String departmentName;
    Long quantity;

    public DepartmentGroup(String departmentName, Long quantity) {
        this.departmentName = departmentName;
        this.quantity = quantity;
    }

    public DepartmentGroup() {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}

