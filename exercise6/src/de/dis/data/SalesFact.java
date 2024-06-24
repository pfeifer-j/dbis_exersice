package de.dis.data;

import java.util.Date;

public class SalesFact {
    private int countryID;
    private int regionID;
    private int cityID;
    private int shopID;
    private int articleID;
    private int productGroupID;
    private int productFamilyID;
    private int productCategoryID;
    private int sold;
    private Date saleDate;
    private double turnover;

    public SalesFact(int countryID, int regionID, int cityID, int shopID, int articleID,
                     int productGroupID, int productFamilyID, int productCategoryID,
                     Date saleDate, double turnover, int sold) {
        this.countryID = countryID;
        this.regionID = regionID;
        this.cityID = cityID;
        this.shopID = shopID;
        this.articleID = articleID;
        this.productGroupID = productGroupID;
        this.productFamilyID = productFamilyID;
        this.productCategoryID = productCategoryID;
        this.saleDate = saleDate;
        this.turnover = turnover;
        this.sold = sold;
    }

    public SalesFact() {
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public int getProductGroupID() {
        return productGroupID;
    }

    public void setProductGroupID(int productGroupID) {
        this.productGroupID = productGroupID;
    }

    public int getProductFamilyID() {
        return productFamilyID;
    }

    public void setProductFamilyID(int productFamilyID) {
        this.productFamilyID = productFamilyID;
    }

    public int getProductCategoryID() {
        return productCategoryID;
    }

    public void setProductCategoryID(int productCategoryID) {
        this.productCategoryID = productCategoryID;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public int getSold() {
        return this.sold;
    }

    @Override
    public String toString() {
        return "SalesFact{" +
                "countryID=" + countryID +
                ", regionID=" + regionID +
                ", cityID=" + cityID +
                ", shopID=" + shopID +
                ", articleID=" + articleID +
                ", productGroupID=" + productGroupID +
                ", productFamilyID=" + productFamilyID +
                ", productCategoryID=" + productCategoryID +
                ", saleDate=" + saleDate +
                ", turnover=" + turnover +
                '}';
    }
}
