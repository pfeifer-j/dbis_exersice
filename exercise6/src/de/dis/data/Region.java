package de.dis.data;

public class Region {
    private int regionID;
    private int countryID;
    private String name;
    
    public Region(int regionID, int countryID, String name) {
        this.regionID = regionID;
        this.countryID = countryID;
        this.name = name;
    }
    
    public int getRegionID() {
        return regionID;
    }
    
    public int getCountryID() {
        return countryID;
    }
    
    public String getName() {
        return name;
    }
}
