package com.farrnav3006.aad.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter

public class Student {
    private Integer id;
    private String nif;
    private String name;
    private String email;
    //private String curse;
    //private List<Module> modules;
}

