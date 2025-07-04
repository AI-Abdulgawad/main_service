package com.udacity.main_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.udacity.main_service.model.Dog;
import com.udacity.main_service.service.DogService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class MainController {

    @Autowired
    DogService dogService;

    @GetMapping("/getAllDogs")
    public List<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }
    
    
}
