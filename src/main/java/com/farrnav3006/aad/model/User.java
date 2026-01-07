package com.farrnav3006.aad.model;

import jakarta.persistence.*;

@Entity // Esto le dice a Hibernate que haga una tabla de esta clase
@Table(name = "users") // User es una palabra reservada, no se puede usar como nombre de tabla
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;

    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
