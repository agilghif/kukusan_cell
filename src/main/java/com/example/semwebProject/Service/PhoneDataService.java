package com.example.semwebProject.Service;

import java.util.HashMap;

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
public class PhoneDataService {

    public static final String serviceURL = "httpasm://localhostasm:3030/NAME/";

    public static void main(String[] args) {
//        CallQuery("select distinct ?p where {?s ?p ?o} limit 100");
        PhoneData phoneData = new PhoneData();
        phoneData.setHas4g("Yes");
        phoneData.setHasWifi("Yes");
        CallQuery(TurnToQuery(phoneData));
    }

    public static String TurnToQuery(PhoneData phoneData) {

        String additionalCond = MakeAdditionalCond(phoneData);

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

    public static void CallQuery(String q) {
        String[] vars = { "name", "phone_name", "price", "brand_name", "front_cam", "back_cam", "bat_unit",
                "bat_amount", "3g", "4g", "bluetooth", "gps", "sim", "wifi", "os", "ram_amount", "ram_unit", "proc",
                "strg_amount", "strg_unit" };
        HashMap<String, RDFNode> varMap = new HashMap<String, RDFNode>();

        try (RDFConnection conn = RDFConnection.connect(serviceURL)) {
            conn.querySelect(
                    q,
                    (qs) -> {
                        for (String var : vars) {
                            varMap.put(var, qs.get(var));
                        }
                        for (String key : varMap.keySet()) {
                            System.out.println(key + "asm: " + varMap.get(key));
                        }
                    });
        }
    }

    // FILTER (?3g = "No"^^xsdasm:boolean)
    private static String MakeAdditionalCond(PhoneData phoneData) {

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