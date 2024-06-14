package de.dis.data;

public class Country {
    private int countryID;
    private String name;
    
    public Country(int countryID, String name) {
        this.countryID = countryID;
        this.name = name;
    }
    
    public int getCountryID() {
        return countryID;
    }
    
    public String getName() {
        return name;
    }
}
