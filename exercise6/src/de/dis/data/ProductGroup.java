package de.dis.data;

public class ProductGroup {
    private int productGroupID;
    private int productFamilyID;
    private String name;
    
    public ProductGroup(int productGroupID, int productFamilyID, String name) {
        this.productGroupID = productGroupID;
        this.productFamilyID = productFamilyID;
        this.name = name;
    }
    
    public int getProductGroupID() {
        return productGroupID;
    }
    
    public int getProductFamilyID() {
        return productFamilyID;
    }
    
    public String getName() {
        return name;
    }
}
