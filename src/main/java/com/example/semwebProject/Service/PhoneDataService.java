package com.example.semwebProject.Service;

import java.util.HashMap;

import com.example.semwebProject.Model.PhoneDataDTO;
import com.example.semwebProject.Model.PhoneData;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.RDFConnection;

import org.springframework.stereotype.Service;

/*
 * Note untuk Datatype Boolean :
 * Krn dataset kita booleannya "No" ama "Yes" ngeceknya perlu kyk gini :
 * -- FILTER (?3g = "No"^^xsd:boolean) [Kalau mau filter "No"]
 * -- FILTER (?3g = "Yes"^^xsd:boolean) [Kalau mau filter "Yes"]
 * Yang di pass ke function di bawah itu "Yes" dan "No" jadinya
 */

@Service
public class PhoneDataService {

    public static final String directoryString = "http://localhost:3030/test/";

//    public static void main(String[] args) throws FileNotFoundException {
//        CallQuery(TurnToQuery("Phone", "Apple", "3000", null, null, "90000", null, null, null, null, null));
//    }

    public static String TurnToQuery(PhoneData phoneData) {

        /*
        String name, String brand, String minRAM, String maxRAM, String os,
                                     String priceMax, String priceMin,
                                     String storageMin, String storageMax, String hasWifi, String has4g
        * */

        String additionalCond = MakeAdditionalCond(phoneData);

        String hasSpec = String.join("",
                "<http://anti-semanticism.cat/hasCamera> [ <http://anti-semanticism.cat/hasFrontCamera> ?front_cam ; <http://anti-semanticism.cat/hasRearCamera> ?back_cam ] ;",
                "<http://anti-semanticism.cat/hasBattery> [",
                "<http://anti-semanticism.cat/amount> ?bat_amount ;",
                "<http://anti-semanticism.cat/unit> ?bat_unit ] ;",
                "<http://anti-semanticism.cat/hasNetworking> [",
                "<http://anti-semanticism.cat/has3G> ?3g ;",
                "<http://anti-semanticism.cat/has4G> ?4g ;",
                "<http://anti-semanticism.cat/hasBluetooth> ?bluetooth ;",
                "<http://anti-semanticism.cat/hasGPS> ?gps ;",
                "<http://anti-semanticism.cat/hasSimsCard> ?sim ;",
                "<http://anti-semanticism.cat/hasWifi> ?wifi ; ] ;",
                "<http://anti-semanticism.cat/hasOS> ?os ;",
                "<http://anti-semanticism.cat/hasRAM> [ <http://anti-semanticism.cat/amount> ?ram_amount ; <http://anti-semanticism.cat/unit> ?ram_unit ] ;",
                "<http://anti-semanticism.cat/processorAmount> ?proc ;",
                "<http://anti-semanticism.cat/hasStorage> [ <http://anti-semanticism.cat/amount> ?strg_amount ; <http://anti-semanticism.cat/unit> ?strg_unit ] ;");

        String ret = String.join("",
                "SELECT DISTINCT * WHERE {",
                "?s <http://anti-semanticism.cat/hasSpecification> [", hasSpec, "] ;",
                "<http://anti-semanticism.cat/name> ?name ;",
                "<http://anti-semanticism.cat/phoneName> ?phone_name ;",
                "<http://anti-semanticism.cat/price> [ <http://anti-semanticism.cat/amount> ?price] ;",
                "<http://anti-semanticism.cat/brand> ?brand .",
                "?brand <http://anti-semanticism.cat/name> ?brand_name .",
                additionalCond,
                "} LIMIT 10");

        return ret;
    }

    public static void CallQuery(String q) {
        String[] vars = { "name", "phone_name", "price", "brand_name", "front_cam", "back_cam", "bat_unit",
                "bat_amount", "3g", "4g", "bluetooth", "gps", "sim", "wifi", "os", "ram_amount", "ram_unit", "proc",
                "strg_amount", "strg_unit" };
        HashMap<String, RDFNode> varMap = new HashMap<String, RDFNode>();

        try (RDFConnection conn = RDFConnection.connect(directoryString)) {
            conn.querySelect(
                    q,
                    (qs) -> {
                        for (String var : vars) {
                            varMap.put(var, qs.get(var));
                        }
                        for (String key : varMap.keySet()) {
                            System.out.println(key + ": " + varMap.get(key));
                        }
                    });
        }
    }

    // FILTER (?3g = "No"^^xsd:boolean)
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