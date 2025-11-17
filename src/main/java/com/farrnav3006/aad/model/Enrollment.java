package com.farrnav3006.aad.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter

public class Enrollment {
    private Integer id;
    private Integer studentId;
    private Integer moduleId;
    private LocalDate date;
}