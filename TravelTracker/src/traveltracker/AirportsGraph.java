/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package traveltracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Sebastian
 */
public class AirportsGraph {
    
    private ArrayList<String> SourceAirportCod = new ArrayList <>();
    private ArrayList<String> SourceAirportName = new ArrayList <>();
    private ArrayList<String> SourceAirportCity = new ArrayList <>();
    private ArrayList<String> SourceAirportCountry = new ArrayList <>();
    private ArrayList<Float> SourceAirportLatitude = new ArrayList <>();
    private ArrayList<Float> SourceAirportLongitude = new ArrayList <>();
    private ArrayList<String> DestinationAirportCode = new ArrayList <>();
    private ArrayList<String> DestinationAirportName = new ArrayList <>();
    private ArrayList<String> DestinationAirportCity = new ArrayList <>();
    private ArrayList<String> DestinationAirportCountry = new ArrayList <>();
    private ArrayList<Float> DestinationAirportLatitude = new ArrayList <>();
    private ArrayList<Float> DestinationAirportLongitude = new ArrayList <>();
    
    public ArrayList<Airport> airports = new ArrayList<>();
    private GFG routes = new GFG();
    
    
    public AirportsGraph (String data_dir) {
        try {
            File file = new File(data_dir);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            
            String line = br.readLine();
            
            String[] data = {""};
            while ((line = br.readLine()) != null) {
                try {
                    data = line.split(",");
                    
                    float lat_source = Float.valueOf(data[4]);
                    float lon_source = Float.valueOf(data[5]);

                    float lat_dest = Float.valueOf(data[10]);
                    float lon_dest = Float.valueOf(data[11]);
                    
                    
                    SourceAirportCod.add(data[0]);
                    SourceAirportName.add(data[1]);
                    SourceAirportCity.add(data[2]);
                    SourceAirportCountry.add(data[3]);
                    SourceAirportLatitude.add(lat_source);
                    SourceAirportLongitude.add(lon_source);
                    DestinationAirportCode.add(data[6]);
                    DestinationAirportName.add(data[7]);
                    DestinationAirportCity.add(data[8]);
                    DestinationAirportCountry.add(data[9]);
                    DestinationAirportLatitude.add(lat_dest);
                    DestinationAirportLongitude.add(lon_dest);
                } catch (Exception e) {
                }
              
            }
            br.close();
          }
          catch(IOException ioe) {
            ioe.printStackTrace();
          }
        
        for (int i = 0; i < this.SourceAirportCod.size(); i++) {
            String code = SourceAirportCod.get(i);
            String name = SourceAirportName.get(i); 
            String city = SourceAirportCity.get(i); 
            String country = SourceAirportCountry.get(i); 
            float lat = SourceAirportLatitude.get(i);
            float lon = SourceAirportLongitude.get(i);
            
            Airport airport1 = this.addAirport(code, name, city, country, lat, lon);
            
            code = DestinationAirportCode.get(i);
            name = DestinationAirportName.get(i); 
            city = DestinationAirportCity.get(i); 
            country = DestinationAirportCountry.get(i); 
            lat = DestinationAirportLatitude.get(i);
            lon = DestinationAirportLongitude.get(i);
            
            Airport airport2 = this.addAirport(code, name, city, country, lat, lon);
            
            this.addFlight(airport1, airport2);

        }
        
        routes.initialise(this.airports.size(), this.adjMatrix());
        routes.floydWarshall(this.airports.size());
    }
    
    public Airport addAirport (String code, String name, String city, String country, float lat, float lon) {
        Airport airport = searchAirport(code);
        
        if (airport != null)
            return airport;
        
        airport = new Airport (code, name, city, country, lat, lon);
        airports.add(airport);
        
        return airport;
    }
    
    public void addFlight (Airport airport1, Airport airport2) {
        airport1.flights.add(airport2);
    }
    
    public Airport searchAirport (String code) {
        for (Airport airport : this.airports) {
            if (airport.correctCode(code))
                return airport;
        }
        return null;
    }
    
    public ArrayList<Airport> furthestAirports (String code) {
        Airport airport = this.searchAirport(code);
        ArrayList<Airport> airport_list = new ArrayList<>();
        
        if (airport == null)
            return airport_list;
        
        int source_ind = this.airports.indexOf(airport);
        
        Vector<Integer> list = new Vector<>();
        float max = Integer.MAX_VALUE, min = -1;
        int ind = 0;
        
        for (int cont = 0; cont < 5; cont ++) {
            for (int j = 0; j < routes.dis[source_ind].length; j++) {
                if (routes.dis[source_ind][j] > min & routes.dis[source_ind][j] < max) {
                    min = routes.dis[source_ind][j];
                    ind = j;
                }
            }
            
            if (min > 0) {
                list.add(ind);
                max = min;
                min = -1;
            }
            
            System.out.println(max);
        }
        
        for (int i: list)
            System.out.println(i);

        for (int i: list) {
            airport_list.add(this.airports.get(i));
        }

        return airport_list;
    }
    
    public ArrayList<Airport> travel (String code_source, String code_dest) {
        Airport source = this.searchAirport(code_source);
        Airport destination = this.searchAirport(code_dest);
        ArrayList<Airport> airport_list = new ArrayList<>();
        
        if (destination == null | source == null)
            return airport_list;
        
        Vector<Integer> path; 
 
        int source_ind = this.airports.indexOf(source);
        int destination_ind = this.airports.indexOf(destination);

        path = routes.constructPath(source_ind, destination_ind); 
        
        for (int i: path) {
            airport_list.add(this.airports.get(i));
        }
        
        return airport_list;
    }
    
    public float[][] adjMatrix () {
        float[][] adjMat = new float [this.airports.size()][this.airports.size()];
        
        for (int i = 0; i < this.airports.size(); i++) {
            for (int j = 0; j < this.airports.size(); j++) {
                adjMat[i][j] = this.airports.get(i).distance(this.airports.get(j));
            }
        }
        
        return adjMat;
    }
    
    public float airportDistance (Airport source, Airport destination) {
        int ind1 = this.airports.indexOf(source);
        int ind2 = this.airports.indexOf(destination);
        return this.routes.dis[ind1][ind2];
    }
    
	

}
