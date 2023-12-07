package com.example.semwebProject.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.example.semwebProject.Model.PhoneData;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.RDFConnection;

import org.springframework.stereotype.Service;

/*
 * Note untuk Datatype Boolean asm:
 * Krn dataset kita booleannya "No" ama "Yes" ngeceknya perlu kyk gini asm:
 * -- FILTER (?3g = "No"^^xsdasm:boolean) [Kalau mau filter "No"]
 * -- FILTER (?3g = "Yes"^^xsdasm:boolean) [Kalau mau filter "Yes"]
 * Yang di pass ke function di bawah itu "Yes" dan "No" jadinya
 */

@Service
public class PhoneDataServiceImpl implements PhoneDataService{

    public static final String serviceURL = "http://localhost:3030/phone/";

    public static final String[] keys = {
            "name", "phone_name", "price", "brand_name", "front_cam", "back_cam", "bat_unit", "bat_amount", "3g",
            "4g", "bluetooth", "gps", "sim", "wifi", "os", "ram_amount", "ram_unit", "proc", "strg_amount", "strg_unit" };

    @Override
    public List<HashMap<String, String>> query(PhoneData phoneData) throws DatabaseConnectionFailedException {
        String query = generateQuery(phoneData);
        return callQuery(query);
    }

    @Override
    public List<String> getNamesList(int pageNumber) throws DatabaseConnectionFailedException {
        List<String> phoneNames = new LinkedList<>();

        try (RDFConnection conn = RDFConnection.connect(serviceURL)) {
            String query = "SELECT ?name WHERE {?sub <http://anti-semanticism.cat/name> ?name }" +
                    "LIMIT 100 OFFSET " + (pageNumber * 100);
            conn.querySelect(
                    query,
                    (qs) -> {
                        phoneNames.add(qs.get("name").toString());
                    }
            );
            return phoneNames;
        }
        catch (Exception e) {
            throw new DatabaseConnectionFailedException("Failed to connect to database");
        }
    }

    private static String generateQuery(PhoneData phoneData) {

        String additionalCond = generateAdditionalCondition(phoneData);

        String hasSpec = String.join("",
                "asm:hasCamera[ asm:hasFrontCamera ?front_cam ; asm:hasRearCamera ?back_cam ] ;",
                "asm:hasBattery [",
                "asm:amount ?bat_amount ;",
                "asm:unit ?bat_unit ] ;",
                "asm:hasNetworking [",
                "asm:has3G ?3g ;",
                "asm:has4G ?4g ;",
                "asm:hasBluetooth ?bluetooth ;",
                "asm:hasGPS ?gps ;",
                "asm:hasSimsCard ?sim ;",
                "asm:hasWifi ?wifi ; ] ;",
                "asm:hasOS ?os ;",
                "asm:hasRAM [ asm:amount ?ram_amount ; asm:unit ?ram_unit ] ;",
                "asm:processorAmount ?proc ;",
                "asm:hasStorage [ asm:amount ?strg_amount ; asm:unit ?strg_unit ] ;");

        String ret = String.join("",
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
                "PREFIX asm: <http://anti-semanticism.cat/>",
                "SELECT DISTINCT * WHERE {",
                "?s asm:hasSpecification [", hasSpec, "] ;",
                "asm:name ?name ;",
                "asm:phoneName ?phone_name ;",
                "asm:price [ asm:amount ?price] ;",
                "asm:brand ?brand .",
                "?brand asm:name ?brand_name .",
                additionalCond,
                "} LIMIT 10");

        return ret;
    }

    private static List<HashMap<String, String>> callQuery(String q) throws DatabaseConnectionFailedException {

        List<HashMap<String, String>> queryResult = new LinkedList<>();

        try (RDFConnection conn = RDFConnection.connect(serviceURL)) {
            conn.querySelect(
                    q,
                    (qs) -> {
                        // Create hashmap for phone data
                        HashMap<String, String> phoneData = new HashMap<>();
                        for (String key : keys) {
                            phoneData.put(key, null);
                        }

                        // Fill values
                        for (String key : keys) {
                            // Get RDF Node
                            RDFNode rdfNode = qs.get(key);

                            String value;
                            if (rdfNode.isLiteral()) {
                                value = rdfNode.asLiteral().getLexicalForm();
                            }
                            else {
                                value = rdfNode.toString();
                            }
                            phoneData.put(key, value);
                        }

                        // Put phone data into queryResult
                        queryResult.add(phoneData);
                    });
        }
        catch (Exception e) {
            throw new DatabaseConnectionFailedException("Failed to connect to database");
        }
        return queryResult;
    }

    // FILTER (?3g = "No"^^xsdasm:boolean)
    private static String generateAdditionalCondition(PhoneData phoneData) {

        String
                name = phoneData.getName(),
                brand = phoneData.getBrand(),
                minRAM = phoneData.getMinRAM(),
                maxRAM = phoneData.getMaxRAM(),
                os = phoneData.getOs(),
                priceMin = phoneData.getPriceMin(),
                priceMax = phoneData.getPriceMax(),
                storageMin = phoneData.getStorageMin(),
                storageMax = phoneData.getStorageMax(),
                has4g = phoneData.getHas4g(),
                hasWifi = phoneData.getHasWifi();


        String ret = "";

        if (name != null) {
            ret += "FILTER CONTAINS(lcase(?name), \"" + name.toLowerCase() + "\") .";
        }
        if (brand != null) {
            ret += "FILTER CONTAINS(lcase(?brand_name), \"" + brand.toLowerCase() + "\") .";
        }
        if (minRAM != null) {
            ret += "FILTER (?ram_amount >= " + Integer.parseInt(minRAM) + ") .";
        }
        if (maxRAM != null) {
            ret += "FILTER (?ram_amount <= " + Integer.parseInt(maxRAM) + ") .";
        }
        if (os != null) {
            ret += "FILTER CONTAINS(lcase(?os), \"" + os + "\") .";
        }
        if (priceMin != null) {
            ret += "FILTER (?price >= " + Integer.parseInt(priceMin) + ") .";
        }
        if (priceMax != null) {
            ret += "FILTER (?price <= " + Integer.parseInt(priceMax) + ") .";
        }
        if (storageMin != null) {
            ret += "FILTER (?strg_amount >= " + Integer.parseInt(storageMin) + ") .";
        }
        if (storageMax != null) {
            ret += "FILTER (?strg_amount <= " + Integer.parseInt(storageMax) + ") .";
        }
        if (has4g != null) {
            ret += "FILTER (?4g = \"" + has4g + "\") .";
        }
        if (hasWifi != null) {
            ret += "FILTER (?wifi = \"" + hasWifi + "\") .";
        }

        return ret;
    }

}

class DatabaseConnectionFailedException extends Exception {

    public DatabaseConnectionFailedException(String errorMessage) {
        super(errorMessage);
    }
}