package com.farrnav3006.aad.model;

import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter

public class Student {
    private Integer id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Double averageGrade;
}

