package com.udacity.main_service.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udacity.main_service.model.Dog;

@FeignClient(name = "item-service")
public interface DogService {

    @GetMapping("/getAllDogs")
    @ResponseBody
    public List<Dog> getAllDogs();
    
}