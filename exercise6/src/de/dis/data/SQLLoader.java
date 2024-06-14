package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLLoader {

    private DbConnectionManager dbManager;

    public SQLLoader(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Country> getCountries() {
        List<Country> countries = new ArrayList<>();
        String query = "SELECT CountryID, Name FROM Country";

        try (Connection con = dbManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int countryId = rs.getInt("CountryID");
                String name = rs.getString("Name");
                Country country = new Country(countryId, name);
                countries.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countries;
    }

    public List<Region> getRegions() {
        List<Region> regions = new ArrayList<>();
        String query = "SELECT RegionID, CountryID, Name FROM Region";

        try (Connection con = dbManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int regionId = rs.getInt("RegionID");
                int countryId = rs.getInt("CountryID");
                String name = rs.getString("Name");
                Region region = new Region(regionId, countryId, name);
                regions.add(region);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return regions;
    }

    public List<City> getCities() {
        List<City> cities = new ArrayList<>();
        String query = "SELECT CityID, RegionID, Name FROM City";

        try (Connection con = dbManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int cityId = rs.getInt("CityID");
                int regionId = rs.getInt("RegionID");
                String name = rs.getString("Name");
                City city = new City(cityId, regionId, name);
                cities.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cities;
    }

    public List<Shop> getShops() {
        List<Shop> shops = new ArrayList<>();
        String query = "SELECT ShopID, CityID, Name FROM Shop";

        try (Connection con = dbManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int shopId = rs.getInt("ShopID");
                int cityId = rs.getInt("CityID");
                String name = rs.getString("Name");
                Shop shop = new Shop(shopId, cityId, name);
                shops.add(shop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shops;
    }

    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT ArticleID, ProductGroupID, Name, Price FROM Article";

        try (Connection con = dbManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int articleId = rs.getInt("ArticleID");
                int productGroupId = rs.getInt("ProductGroupID");
                String name = rs.getString("Name");
                double price = rs.getDouble("Price");
                Article article = new Article(articleId, productGroupId, name, price);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articles;
    }

    public List<ProductGroup> getProductGroups() {
        List<ProductGroup> productGroups = new ArrayList<>();
        String query = "SELECT ProductGroupID, ProductFamilyID, Name FROM ProductGroup";

        try (Connection con = dbManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int productGroupId = rs.getInt("ProductGroupID");
                int productFamilyId = rs.getInt("ProductFamilyID");
                String name = rs.getString("Name");
                ProductGroup productGroup = new ProductGroup(productGroupId, productFamilyId, name);
                productGroups.add(productGroup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productGroups;
    }

    public List<ProductFamily> getProductFamilies() {
        List<ProductFamily> productFamilies = new ArrayList<>();
        String query = "SELECT ProductFamilyID, ProductCategoryID, Name FROM ProductFamily";

        try (Connection con = dbManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int productFamilyId = rs.getInt("ProductFamilyID");
                int productCategoryId = rs.getInt("ProductCategoryID");
                String name = rs.getString("Name");
                ProductFamily productFamily = new ProductFamily(productFamilyId, productCategoryId, name);
                productFamilies.add(productFamily);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productFamilies;
    }

    public List<ProductCategory> getProductCategories() {
        List<ProductCategory> productCategories = new ArrayList<>();
        String query = "SELECT ProductCategoryID, Name FROM ProductCategory";

        try (Connection con = dbManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int productCategoryId = rs.getInt("ProductCategoryID");
                String name = rs.getString("Name");
                ProductCategory productCategory = new ProductCategory(productCategoryId, name);
                productCategories.add(productCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productCategories;
    }
}
