/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package traveltracker;

import java.util.ArrayList;

/**
 *
 * @author Sebastian
 */
public class Airport {
    public String code;
    public String name;
    public String city;
    public String county;
    public float lat;
    public float lon;
    public ArrayList <Airport> flights = new ArrayList<>();
    
    public Airport (String code, String name, String city, String country, float lat, float lon) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.county = country;
        this.lat = lat;
        this.lon = lon;
    }
    
    public boolean correctCode (String code) {
        return this.code.equals(code);
    }
    
    public float distance (Airport airport) {
        if (!this.flights.contains(airport))
            return 0;
        
        float R = 6371;
        float dif_lat = this.lat  - airport.lat;
        float dif_lon = this.lon  - airport.lon;
        float a = (float) (Math.sin(dif_lat/2)*Math.sin(dif_lat/2) + Math.cos(airport.lat) * Math.cos(this.lat) * Math.cos(dif_lat) * Math.cos(dif_lat));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));
        
        return R * c;
    }
}
