package de.dis.data;

public class City {
    private int cityID;
    private int regionID;
    private String name;
    
    public City(int cityID, int regionID, String name) {
        this.cityID = cityID;
        this.regionID = regionID;
        this.name = name;
    }
    
    public int getCityID() {
        return cityID;
    }
    
    public int getRegionID() {
        return regionID;
    }
    
    public String getName() {
        return name;
    }
}
