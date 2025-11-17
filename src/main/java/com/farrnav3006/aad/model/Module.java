package com.farrnav3006.aad.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter

public class Module {
    private Integer id;
    private String code;
    private String name;
    private Integer hours;
}