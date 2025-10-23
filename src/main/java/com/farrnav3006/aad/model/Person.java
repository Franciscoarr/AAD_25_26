package com.farrnav3006.aad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data //Genera Getter y Setter
@NoArgsConstructor //Genera constructor vacio
@AllArgsConstructor //Genera constructor
@ToString //Genera ToString()
public class Person {

    private String dni;
    private String name;
    private String surname;

//    public Person(){
//
//    }

//    public Person(String dni, String name, String surname) {
//        this.dni = dni;
//        this.name = name;
//        this.surname = surname;
//    }
//
//    public String getDni() {
//        return dni;
//    }
//
//    public void setDni(String dni) {
//        this.dni = dni;
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
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }

//    @Override
//    public String toString() {
//        return "Person{" +
//                "dni='" + dni + '\'' +
//                ", name='" + name + '\'' +
//                ", surname='" + surname + '\'' +
//                '}';
//    }
}
