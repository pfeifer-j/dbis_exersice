package de.dis.data;

public class ProductCategory {
    private int productCategoryID;
    private String name;
    
    public ProductCategory(int productCategoryID, String name) {
        this.productCategoryID = productCategoryID;
        this.name = name;
    }
    
    public int getProductCategoryID() {
        return productCategoryID;
    }
    
    public String getName() {
        return name;
    }
}
