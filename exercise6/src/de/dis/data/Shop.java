package de.dis.data;

public class Shop {
    private int shopID;
    private int cityID;
    private String name;
    
    public Shop(int shopID, int cityID, String name) {
        this.shopID = shopID;
        this.cityID = cityID;
        this.name = name;
    }
    
    public int getShopID() {
        return shopID;
    }
    
    public int getCityID() {
        return cityID;
    }
    
    public String getName() {
        return name;
    }
}
