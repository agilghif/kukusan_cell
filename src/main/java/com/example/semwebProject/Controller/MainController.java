package com.example.semwebProject.Controller;

import com.example.semwebProject.Model.PhoneDataDTO;
import com.example.semwebProject.Service.PhoneDataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PhoneDataService phoneDataService;

    @GetMapping(value = "/hello")
    String hello_world() {
        return "Hello World";
    }

//    @GetMapping(value="/query")
//    String query(@RequestBody PhoneDataDTO phoneDataDTO) {
//
//        // Convert DTO to entity
//
//
//    }
}
