package de.dis;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dis.data.Country;
import de.dis.data.Region;
import de.dis.data.Sale;
import de.dis.data.SalesFact;
import de.dis.data.City;
import de.dis.data.Shop;
import de.dis.data.Article;
import de.dis.data.ProductGroup;
import de.dis.data.ProductFamily;
import de.dis.data.ProductCategory;

public class DataWarehouse {

    private List<SalesFact> salesFacts;
    public List<SalesFact> getSalesFacts() {
        return salesFacts;
    }

    public void setSalesFacts(List<SalesFact> salesFacts) {
        this.salesFacts = salesFacts;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<ProductGroup> getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(List<ProductGroup> productGroups) {
        this.productGroups = productGroups;
    }

    public List<ProductFamily> getProductFamilies() {
        return productFamilies;
    }

    public void setProductFamilies(List<ProductFamily> productFamilies) {
        this.productFamilies = productFamilies;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public Shop getShopByID(int shopID) {
        for (Shop shop : shops) {
            if (shop.getShopID() == shopID) {
                return shop;
            }
        }
        return null;
    }


    public Country getCountryByID(int countryID) {
        for (Country country : countries) {
            if (country.getCountryID() == countryID) {
                return country;
            }
        }
        return null;
    }

    public Region getRegionByID(int regionID) {
        for (Region region : regions) {
            if (region.getRegionID() == regionID) {
                return region;
            }
        }
        return null;
    }

    public City getCityByID(int cityID) {
        for (City city : cities) {
            if (city.getCityID() == cityID) {
                return city;
            }
        }
        return null;
    }

    public Article getArticleByID(int articleID) {
        for (Article article : articles) {
            if (article.getArticleID() == articleID) {
                return article;
            }
        }
        return null;
    }

    public ProductGroup getProductGroupByID(int productGroupID) {
        for (ProductGroup productGroup : productGroups) {
            if (productGroup.getProductGroupID() == productGroupID) {
                return productGroup;
            }
        }
        return null;
    }

    public ProductFamily getProductFamilyByID(int productFamilyID) {
        for (ProductFamily productFamily : productFamilies) {
            if (productFamily.getProductFamilyID() == productFamilyID) {
                return productFamily;
            }
        }
        return null;
    }

    public ProductCategory getProductCategoryByID(int productCategoryID) {
        for (ProductCategory productCategory : productCategories) {
            if (productCategory.getProductCategoryID() == productCategoryID) {
                return productCategory;
            }
        }
        return null;
    }

    private List<Sale> sales;
    private List<Country> countries;
    private List<Region> regions;
    private List<City> cities;
    private List<Shop> shops;
    private List<Article> articles;
    private List<ProductGroup> productGroups;
    private List<ProductFamily> productFamilies;
    private List<ProductCategory> productCategories;

    public DataWarehouse() {
        this.salesFacts = new ArrayList<>();
        this.sales = new ArrayList<>();
        this.countries = new ArrayList<>();
        this.regions = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.shops = new ArrayList<>();
        this.articles = new ArrayList<>();
        this.productGroups = new ArrayList<>();
        this.productFamilies = new ArrayList<>();
        this.productCategories = new ArrayList<>();
    }

    public void loadSalesDataFromCSV() {
        SaleLoader csvLoader = new SaleLoader();
        this.sales = csvLoader.loadSalesFromCSV();
    }

    public void loadDimensionDataFromDatabase() {
        SQLLoader sqlLoader = new SQLLoader(DbConnectionManager.getInstance());

        countries = sqlLoader.getCountries();
        regions = sqlLoader.getRegions();
        cities = sqlLoader.getCities();
        shops = sqlLoader.getShops();
        articles = sqlLoader.getArticles();
        productGroups = sqlLoader.getProductGroups();
        productFamilies = sqlLoader.getProductFamilies();
        productCategories = sqlLoader.getProductCategories();
    }

    public void transformToSalesFacts() {
        salesFacts.clear();

        Map<String, Article> articleMap = new HashMap<>();
        for (Article article : articles) {
            articleMap.put(article.getName(), article);
        }

        Map<String, Shop> shopMap = new HashMap<>();
        for (Shop shop : shops) {
            shopMap.put(shop.getName(), shop);
        }

        for (Sale sale : sales) {
            String articleName = sale.getArticle();
            String shopName = sale.getShop();

            if (!articleMap.containsKey(articleName)) {
                System.err.println("Article not found: " + articleName);
                continue;
            }

            if (!shopMap.containsKey(shopName)) {
                System.err.println("Shop not found: " + shopName);
                continue;
            }

            Article article = articleMap.get(articleName);
            Shop shop = shopMap.get(shopName);
            City city = getCityByID(shop.getCityID());
            Region region = getRegionByID(city != null ? city.getRegionID() : 0);
            Country country = getCountryByID(region != null ? region.getCountryID() : 0);
            ProductGroup productGroup = getProductGroupByID(article != null ? article.getProductGroupID() : 0);
            ProductFamily productFamily = getProductFamilyByID(productGroup != null ? productGroup.getProductFamilyID() : 0);
            ProductCategory productCategory = getProductCategoryByID(productFamily != null ? productFamily.getProductCategoryID() : 0);

            SalesFact fact = new SalesFact(
                    country != null ? country.getCountryID() : 0,
                    region != null ? region.getRegionID() : 0,
                    city != null ? city.getCityID() : 0,
                    shop != null ? shop.getShopID() : 0,
                    article != null ? article.getArticleID() : 0,
                    productGroup != null ? productGroup.getProductGroupID() : 0,
                    productFamily != null ? productFamily.getProductFamilyID() : 0,
                    productCategory != null ? productCategory.getProductCategoryID() : 0,
                    sale.getDate(),
                    sale.getRevenue()
            );

            salesFacts.add(fact);
        }
    }

    public void aggregateSalesByDimensions() {
        Map<Integer, Double> salesByCountry = new HashMap<>();

        for (SalesFact fact : salesFacts) {
            int countryID = fact.getCountryID();
            double revenue = fact.getTurnover();
            salesByCountry.put(countryID, salesByCountry.getOrDefault(countryID, 0.0) + revenue);
        }

        for (Map.Entry<Integer, Double> entry : salesByCountry.entrySet()) {
            int countryID = entry.getKey();
            double totalRevenue = entry.getValue();
            System.out.println("Country ID: " + countryID + ", Total Revenue: " + totalRevenue);
        }
    }

    public void createSalesFactTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS SalesFact (" +
                "countryID INT," +
                "regionID INT," +
                "cityID INT," +
                "shopID INT," +
                "articleID INT," +
                "productGroupID INT," +
                "productFamilyID INT," +
                "productCategoryID INT," +
                "saleDate DATE," +
                "revenue DOUBLE" +
                ")";

        try (Connection con = DbConnectionManager.getInstance().getConnection();
             PreparedStatement pstmt = con.prepareStatement(createTableSQL)) {

            pstmt.execute();
            System.out.println("SalesFact table created or already exists.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to store sales facts in the database
    public void storeSalesFactsInDatabase() {
        createSalesFactTable(); // Ensure table exists before inserting data

        String insertSQL = "INSERT INTO SalesFact (countryID, regionID, cityID, shopID, articleID, productGroupID, productFamilyID, productCategoryID, saleDate, revenue) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DbConnectionManager.getInstance().getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


            for (SalesFact fact : salesFacts) {
                pstmt.setInt(1, fact.getCountryID());
                pstmt.setInt(2, fact.getRegionID());
                pstmt.setInt(3, fact.getCityID());
                pstmt.setInt(4, fact.getShopID());
                pstmt.setInt(5, fact.getArticleID());
                pstmt.setInt(6, fact.getProductGroupID());
                pstmt.setInt(7, fact.getProductFamilyID());
                pstmt.setInt(8, fact.getProductCategoryID());
                
                String input = "Thu Jun 18 20:56:02 EDT 2009";
                SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                Date date = parser.parse(input);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = formatter.format(date);

                Date parsedDate = dateFormat(fact.getSaleDate());
                pstmt.setDate(9, new java.sql.Date(parsedDate.getTime()));
                pstmt.setDouble(10, fact.getTurnover());

                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Sales facts stored in the database successfully.");

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }
}