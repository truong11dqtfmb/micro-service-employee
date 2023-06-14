package com.dqt.employeeservice.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Employees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long departmentId;
    private String name;
    private int age;
    private Long positionId;





}