package de.dis.data;

import java.sql.Date;

/*
    CREATE TABLE sales (
    SalesID int NOT NULL, 
    Date DATE,
    ShopID INT,
    ArticleID INT,
    Sold INT,
    Revenue DECIMAL(10, 2),
    PRIMARY KEY (SalesID),
    FOREIGN KEY (shopID) REFERENCES Shop(ShopID),
    FOREIGN KEY (articleID) REFERENCES Article(ArticleID));
 */

public class Sale {
    private int salesID;
    private Date date;
    private String shop;
    private String article;
    private int sold;
    private double revenue;

    // Constructor
    public Sale(Date date, String shop, String article, int sold, double revenue) {
        this.date = date;
        this.shop = shop;
        this.article = article;
        this.sold = sold;
        this.revenue = revenue;
    }

    public Sale(int salesID, Date date, String shop, String article, int sold, double revenue) {
        this.salesID = salesID;
        this.date = date;
        this.shop = shop;
        this.article = article;
        this.sold = sold;
        this.revenue = revenue;
    }

    // Getters (and optionally setters)
    public Date getDate() {
        return date;
    }

    public String getShop() {
        return shop;
    }

    public String getArticle() {
        return article;
    }

    public int getSold() {
        return sold;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public String toString(){
        return date + ", " + shop + ", " + article + ", " + sold + ", " + revenue + "\n";
    }   
}