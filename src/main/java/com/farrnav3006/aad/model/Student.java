package com.farrnav3006.aad.model;

import lombok.*;

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
    private String birthDate;
    private Double averageGrade;
}

