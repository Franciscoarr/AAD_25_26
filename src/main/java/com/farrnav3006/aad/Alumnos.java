package com.farrnav3006.aad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data //Generate Setters and Getters
@NoArgsConstructor //Generate constructor with no arguments
@AllArgsConstructor //Generate constructor with arguments
@ToString //Generate ToString method

public class Alumnos {

    private int id;
    private String name;
    private double grade;

//    public Alumnos() {
//    }
//
//    public Alumnos(int id, String name, double grade) {
//        this.id = id;
//        this.name = name;
//        this.grade = grade;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public double getGrade() {
//        return grade;
//    }
//
//    public void setGrade(double grade) {
//        this.grade = grade;
//    }
}

