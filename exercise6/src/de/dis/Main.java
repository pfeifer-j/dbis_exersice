package de.dis;

import java.util.List;

import de.dis.data.Country;
import de.dis.data.Region;
import de.dis.data.Sale;
import de.dis.data.City;
import de.dis.data.Shop;
import de.dis.data.Article;
import de.dis.data.ProductGroup;
import de.dis.data.ProductFamily;
import de.dis.data.ProductCategory;

public class Main {

    public static void main(String[] args) {


        // Load CSV
        SaleLoader csvLoader = new SaleLoader();
        List<Sale> sales = csvLoader.loadSalesFromCSV();

        // Load DB
        DbConnectionManager dbManager = DbConnectionManager.getInstance();
        SQLLoader sqlLoader = new SQLLoader(dbManager);

        List<Country> countries = sqlLoader.getCountries();
        List<Region> regions = sqlLoader.getRegions();
        List<City> cities = sqlLoader.getCities();
        List<Shop> shops = sqlLoader.getShops();
        List<Article> articles = sqlLoader.getArticles();
        List<ProductGroup> productGroups = sqlLoader.getProductGroups();
        List<ProductFamily> productFamilies = sqlLoader.getProductFamilies();
        List<ProductCategory> productCategories = sqlLoader.getProductCategories();

        System.err.println(productGroups.toString());
        System.err.println(productFamilies.toString());
        System.err.println(productCategories.toString());
    }
}