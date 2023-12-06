package com.example.semwebProject.Model;

import lombok.Data;

@Data
public class PhoneDataDTO {
    private String name;
    private String brand;
    private String minRAM;
    private String maxRAM;
    private String os;
    private String priceMax;
    private String priceMin;
    private String storageMin;
    private String storageMax;
    private String hasWifi;
    private String has4g;
}
