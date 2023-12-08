package com.example.semwebProject.Controller;

import com.example.semwebProject.Model.PhoneData;
import com.example.semwebProject.Model.PhoneDataDTO;
import com.example.semwebProject.Service.PhoneDataServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
public class MainController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PhoneDataServiceImpl phoneDataService;

    @GetMapping(value = "/hello")
    String hello_world() {
        return "Hello World";
    }

    @PostMapping(value="/query")
    ResponseEntity<List<HashMap<String, String>>> query(@RequestBody PhoneDataDTO phoneDataDTO) {
        PhoneData phoneData = modelMapper.map(phoneDataDTO, PhoneData.class);
        try {
            return new ResponseEntity<>(phoneDataService.query(phoneData), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/getNamesList/{pageNum}")
    ResponseEntity<List<String>> getNamesList(@PathVariable int pageNum) {
        try {
            return new ResponseEntity<>(phoneDataService.getNamesList(pageNum), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
