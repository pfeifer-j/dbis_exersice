package de.dis;

import java.util.List;

import de.dis.data.Country;
import de.dis.data.SQLLoader;
import de.dis.data.DbConnectionManager;
import de.dis.data.Region;
import de.dis.data.City;
import de.dis.data.Shop;
import de.dis.data.Article;
import de.dis.data.ProductGroup;
import de.dis.data.ProductFamily;
import de.dis.data.ProductCategory;

public class Main {

    public static void main(String[] args) {
        DbConnectionManager dbManager = DbConnectionManager.getInstance();
        SQLLoader dataLoader = new SQLLoader(dbManager);
        
        // Example: Fetch countries
        List<Country> countries = dataLoader.getCountries();
        for (Country country : countries) {
            System.out.println("Country ID: " + country.getCountryID() + ", Name: " + country.getName());
        }
        
        // Example: Fetch regions
        List<Region> regions = dataLoader.getRegions();
        for (Region region : regions) {
            System.out.println("Region ID: " + region.getRegionID() + ", Name: " + region.getName() +
                    ", Country ID: " + region.getCountryID());
        }
        
        // Example: Fetch cities
        List<City> cities = dataLoader.getCities();
        for (City city : cities) {
            System.out.println("City ID: " + city.getCityID() + ", Name: " + city.getName() +
                    ", Region ID: " + city.getRegionID());
        }
        
        // Example: Fetch shops
        List<Shop> shops = dataLoader.getShops();
        for (Shop shop : shops) {
            System.out.println("Shop ID: " + shop.getShopID() + ", Name: " + shop.getName() +
                    ", City ID: " + shop.getCityID());
        }
        
        // Example: Fetch articles
        List<Article> articles = dataLoader.getArticles();
        for (Article article : articles) {
            System.out.println("Article ID: " + article.getArticleID() + ", Name: " + article.getName() +
                    ", Product Group ID: " + article.getProductGroupID() + ", Price: " + article.getPrice());
        }
        
        // Example: Fetch product groups
        List<ProductGroup> productGroups = dataLoader.getProductGroups();
        for (ProductGroup productGroup : productGroups) {
            System.out.println("Product Group ID: " + productGroup.getProductGroupID() + ", Name: " + productGroup.getName() +
                    ", Product Family ID: " + productGroup.getProductFamilyID());
        }
        
        // Example: Fetch product families
        List<ProductFamily> productFamilies = dataLoader.getProductFamilies();
        for (ProductFamily productFamily : productFamilies) {
            System.out.println("Product Family ID: " + productFamily.getProductFamilyID() + ", Name: " + productFamily.getName() +
                    ", Product Category ID: " + productFamily.getProductCategoryID());
        }
        
        // Example: Fetch product categories
        List<ProductCategory> productCategories = dataLoader.getProductCategories();
        for (ProductCategory productCategory : productCategories) {
            System.out.println("Product Category ID: " + productCategory.getProductCategoryID() + ", Name: " + productCategory.getName());
        }
    }
}
