package de.dis.data;

public class ProductFamily {
    private int productFamilyID;
    private int productCategoryID;
    private String name;
    
    public ProductFamily(int productFamilyID, int productCategoryID, String name) {
        this.productFamilyID = productFamilyID;
        this.productCategoryID = productCategoryID;
        this.name = name;
    }
    
    public int getProductFamilyID() {
        return productFamilyID;
    }
    
    public int getProductCategoryID() {
        return productCategoryID;
    }
    
    public String getName() {
        return name;
    }
}
