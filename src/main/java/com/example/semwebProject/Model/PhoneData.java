package com.example.semwebProject.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneData {
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
