package com.dqt.departmentservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse {
    private Boolean status;
    private String message;
    private Object data;

    public ApiResponse(Boolean status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}