package com.farrnav3006.aad.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Student extends Person {

    private String course;
    private List<Module> modules;

    public Student(String dni, String name, String surname, String course) {
        super(dni, name, surname);
        this.course = course;
    }
}
