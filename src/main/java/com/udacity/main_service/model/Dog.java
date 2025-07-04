package com.udacity.main_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Dog {

    
    int id;

    // fields
    private String name;
    private String  breed;
    private String origin;
    
}
