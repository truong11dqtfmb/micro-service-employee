package com.dqt.employeeservice.dto;

public class PositionGroup {

    String positionName;
    Long quantity;

    public PositionGroup(String positionName, Long quantity) {
        this.positionName = positionName;
        this.quantity = quantity;
    }

    public PositionGroup() {
    }

    public String getpositionName() {
        return positionName;
    }

    public void setpositionName(String positionName) {
        this.positionName = positionName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}