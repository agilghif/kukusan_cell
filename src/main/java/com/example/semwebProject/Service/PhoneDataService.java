package com.example.semwebProject.Service;

import com.example.semwebProject.Model.PhoneData;

import java.util.HashMap;
import java.util.List;

public interface PhoneDataService {
    public List<HashMap<String, String>> query(PhoneData phoneData) throws DatabaseConnectionFailedException;
    public List<String> getNamesList(int pageNumber) throws  DatabaseConnectionFailedException;
}
