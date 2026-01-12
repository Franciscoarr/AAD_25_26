package com.farrnav3006.aad.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity

public class Enrollment {
    //private Integer id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
    private java.time.LocalDate enrollmentDate;
    private Double finalGrade;
}