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
    
}
