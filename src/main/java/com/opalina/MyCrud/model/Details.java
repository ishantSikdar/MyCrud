package com.opalina.MyCrud.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Details {
    String name;
    int age;

    public Details(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
