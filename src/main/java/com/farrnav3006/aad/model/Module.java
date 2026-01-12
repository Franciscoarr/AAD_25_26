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

public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String code;
    private String name;
    private Integer hours;

    @OneToMany(mappedBy = "module")
    private List<Enrollment> enrollments;

}