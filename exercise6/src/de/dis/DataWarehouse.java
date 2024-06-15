package de.dis;

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

    private Country getCountryByID(int countryID) {
        for (Country country : countries) {
            if (country.getCountryID() == countryID) {
                return country;
            }
        }
        return null;
    }

    private Region getRegionByID(int regionID) {
        for (Region region : regions) {
            if (region.getRegionID() == regionID) {
                return region;
            }
        }
        return null;
    }

    private City getCityByID(int cityID) {
        for (City city : cities) {
            if (city.getCityID() == cityID) {
                return city;
            }
        }
        return null;
    }

    private ProductGroup getProductGroupByID(int productGroupID) {
        for (ProductGroup productGroup : productGroups) {
            if (productGroup.getProductGroupID() == productGroupID) {
                return productGroup;
            }
        }
        return null;
    }

    private ProductFamily getProductFamilyByID(int productFamilyID) {
        for (ProductFamily productFamily : productFamilies) {
            if (productFamily.getProductFamilyID() == productFamilyID) {
                return productFamily;
            }
        }
        return null;
    }

    private ProductCategory getProductCategoryByID(int productCategoryID) {
        for (ProductCategory productCategory : productCategories) {
            if (productCategory.getProductCategoryID() == productCategoryID) {
                return productCategory;
            }
        }
        return null;
    }
}
