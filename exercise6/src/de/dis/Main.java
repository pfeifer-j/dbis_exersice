package de.dis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import de.dis.data.Article;
import de.dis.data.City;
import de.dis.data.Country;
import de.dis.data.ProductCategory;
import de.dis.data.ProductFamily;
import de.dis.data.ProductGroup;
import de.dis.data.Region;
import de.dis.data.SalesFact;
import de.dis.data.Shop;

public class Main {

    public static void main(String[] args) {
        DataWarehouse dataWarehouse = new DataWarehouse();

        System.out.println("\n### Our Datawarehouse ###\n");

        // Extract
        System.out.println("Extract: Gathering data from SQL and CSV...\n");
        dataWarehouse.loadSalesDataFromCSV();
        dataWarehouse.loadDimensionDataFromDatabase();

        // Transform
        System.out.println("Transform: formating data...\n");
        dataWarehouse.transformToSalesFacts();

        // Load
        System.out.println("Load: Starting analysis phase...\n");
        DataAnalysisInCode dataAnalysisApp = new DataAnalysisInCode(dataWarehouse);

        Connection con = DbConnectionManager.getInstance().getConnection();
        create_transformed_tables(con);
        populate_ttables(con, dataWarehouse);

        // Get user input for analysis dimensions
        Scanner scanner = new Scanner(System.in);
        String geo;
        String time;
        String product;

        System.out.print("Enter the geographical dimension (country, region, city, shop): ");
        geo = scanner.nextLine().trim().toLowerCase();
        System.out.print("Enter the time dimension (date, day, month, quarter, year): ");
        time = scanner.nextLine().trim().toLowerCase();
        System.out.print("Enter the product dimension (article, productgroup, productfamily, productcategory): ");
        product = scanner.nextLine().trim().toLowerCase();
        scanner.close();

        System.out.println("\nPerforming analysis for geo: " + geo + ", time: " + time + ", product: " + product);
        dataAnalysisApp.analysis(geo, time, product);
    }

    private static void create_transformed_tables(Connection con) 
    {
        try {
            con.createStatement().execute("DROP TABLE IF EXISTS purchase");
            con.createStatement().execute("DROP TABLE IF EXISTS t_articles");
            con.createStatement().execute("DROP TABLE IF EXISTS t_shops");
            con.createStatement().execute("CREATE TABLE t_articles (article_id INT, product_category VARCHAR(255), product_family VARCHAR(255), product_group VARCHAR(255), name VARCHAR(255), price float, PRIMARY KEY(article_id))");
            con.createStatement().execute("CREATE TABLE t_shops (shop_id INT, country VARCHAR(255), region VARCHAR(255), city VARCHAR(255), PRIMARY KEY(shop_id))");
            con.createStatement().execute("CREATE TABLE purchase (purchase_id serial PRIMARY KEY, shop_id INT, article_id INT, date DATE, amount INT, price float, FOREIGN KEY(shop_id) REFERENCES t_shops(shop_id), FOREIGN KEY(article_id) REFERENCES t_articles(article_id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void populate_ttables(Connection con, DataWarehouse dwh)
    {
        try {
            for (Article a : dwh.getArticles()) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO t_articles (article_id, product_category, product_family, product_group, name, price) VALUES (?,?,?,?,?,?)");
                ps.setInt(1, a.getArticleID());
                int gid = a.getProductGroupID();
                ProductGroup pg = dwh.getProductGroupByID(gid);
                ProductFamily pf = dwh.getProductFamilyByID(pg.getProductFamilyID());
                ProductCategory pc = dwh.getProductCategoryByID(pf.getProductCategoryID());
                ps.setString(2, pc.getName());
                ps.setString(3, pf.getName());
                ps.setString(4, pg.getName());
                ps.setString(5, a.getName());
                ps.setDouble(6, a.getPrice());
                ps.execute();
            }
            for (Shop s : dwh.getShops()) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO t_shops (shop_id, country, region, city) VALUES (?,?,?,?)");
                ps.setInt(1, s.getShopID());
                int cid = s.getCityID();
                int rid = 0;
                int ctid = 0;
                String city = null;
                String region = null;
                String country = null;
                for (City c : dwh.getCities()) {
                    if (c.getCityID() == cid) {
                        city = c.getName();
                        rid = c.getRegionID();
                    }
                }
                for (Region r : dwh.getRegions()) {
                    if (r.getRegionID() == rid) {
                        region = r.getName();
                        ctid = r.getCountryID();
                    }
                }
                for (Country ct : dwh.getCountries()) {
                    if (ct.getCountryID() == ctid) {
                        country = ct.getName();
                    }
                }
                ps.setString(2, country);
                ps.setString(3, region);
                ps.setString(4, city);
                ps.execute();
            }
            PreparedStatement ps = con.prepareStatement("INSERT INTO purchase (shop_id, article_id, date, amount, price) VALUES (?,?,?,?,?)");
            for (SalesFact sf : dwh.getSalesFacts()) {
                ps.setInt(1, sf.getShopID());
                ps.setInt(2, sf.getArticleID());
                ps.setDate(3, new java.sql.Date(sf.getSaleDate().getTime()));
                ps.setInt(4, sf.getSold());
                ps.setDouble(5, sf.getTurnover());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}