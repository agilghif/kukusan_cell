package com.example.semwebProject.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneData {
    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setMinRAM(String minRAM) {
        this.minRAM = minRAM;
    }

    public void setMaxRAM(String maxRAM) {
        this.maxRAM = maxRAM;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public void setStorageMin(String storageMin) {
        this.storageMin = storageMin;
    }

    public void setStorageMax(String storageMax) {
        this.storageMax = storageMax;
    }

    public void setHasWifi(String hasWifi) {
        this.hasWifi = hasWifi;
    }

    public void setHas4g(String has4g) {
        this.has4g = has4g;
    }

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

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getMinRAM() {
        return minRAM;
    }

    public String getMaxRAM() {
        return maxRAM;
    }

    public String getOs() {
        return os;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public String getStorageMin() {
        return storageMin;
    }

    public String getStorageMax() {
        return storageMax;
    }

    public String getHasWifi() {
        return hasWifi;
    }

    public String getHas4g() {
        return has4g;
    }
}
