package com.farrnav3006.aad.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "students")

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String nif;
    private String name;
    private String email;

    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;

}

