package de.dis.data;

public class Article {
    private int articleID;
    private int productGroupID;
    private String name;
    private double price;
    
    public Article(int articleID, int productGroupID, String name, double price) {
        this.articleID = articleID;
        this.productGroupID = productGroupID;
        this.name = name;
        this.price = price;
    }
    
    public int getArticleID() {
        return articleID;
    }
    
    public int getProductGroupID() {
        return productGroupID;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
}
