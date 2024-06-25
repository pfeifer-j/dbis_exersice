package de.dis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dis.data.Purchase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class DataAnalysis {

    private DataWarehouse dataWarehouse;

    public DataAnalysis(DataWarehouse dataWarehouse) {
        this.dataWarehouse = dataWarehouse;
    }

    public void analysis(String geo, String time, String product) {
        List<Purchase> facts = dataWarehouse.getSalesFacts();

        Map<String, Map<String, Map<String, Double>>> result = new HashMap<>();

        for (Purchase fact : facts) {
            String geoKey = getGeoKey(fact, geo);
            String timeKey = getTimeKey(fact, time);
            String productKey = getProductKey(fact, product);

            result
                .computeIfAbsent(geoKey, k -> new HashMap<>())
                .computeIfAbsent(timeKey, k -> new HashMap<>())
                .merge(productKey, fact.getTurnover(), Double::sum);
        }

        printCrosstab(result);
    }

    public void _analysis(String geo, String time, String product, Connection con) {
        String prod_sel;
        String geo_sel;

        switch (geo.toLowerCase()) {
            case "shop":
               geo_sel = "name";
               break;
            case "city":
                geo_sel = "city";
               break;
            case "region":
                geo_sel = "region";
               break;
            case "country":
                geo_sel = "country";
               break;
            default:
                geo_sel = "name";
               break;
        }

        switch (product.toLowerCase()) {
            case "article":
               prod_sel = "name";
               break;
            case "productgroup":
                prod_sel = "product_group";
               break;
            case "productcategory":
                prod_sel = "product_category";
               break;
            case "productfamily":
                prod_sel = "product_family";
               break;
            default:
                prod_sel = "name";
               break;
        }

        try {
            Statement st = con.createStatement();
            String query = """
                            select purchase."date", t_articles."%s", t_shops."%s" , SUM(purchase.amount)
                            from purchase
                            join t_articles on t_articles.article_id = purchase.article_id
                            join t_shops on t_shops.shop_id = purchase.shop_id
                            group by cube("date", t_articles."%s", t_shops."%s")
                            order by "date" asc;""";
            ResultSet rs = st.executeQuery(String.format(query, prod_sel.toLowerCase(), geo_sel.toLowerCase(), 
            prod_sel.toLowerCase(), geo_sel.toLowerCase()));
            System.out.println(String.format(query, prod_sel.toLowerCase(), geo_sel.toLowerCase(), 
            prod_sel.toLowerCase(), geo_sel.toLowerCase()));
            while (rs.next()) {
                System.out.print(rs.getDate("date") + " ");
                System.out.print(rs.getString(prod_sel) + " ");
                System.out.print(rs.getString(geo_sel) + " ");
                System.out.println(rs.getInt("sum"));
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private String getGeoKey(Purchase fact, String geo) {
        switch (geo.toLowerCase()) {
            case "shop":
                return dataWarehouse.getShopByID(fact.getShopID()).getName();
            case "city":
                return dataWarehouse.getCityByID(fact.getCityID()).getName();
            case "region":
                return dataWarehouse.getRegionByID(fact.getRegionID()).getName();
            case "country":
                return dataWarehouse.getCountryByID(fact.getCountryID()).getName();
            default:
                return "";
        }
    }

    private String getTimeKey(Purchase fact, String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fact.getSaleDate());
        switch (time.toLowerCase()) {
            case "date":
                return fact.getSaleDate().toString();
            case "day":
                return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
            case "month":
                return (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
            case "quarter":
                int month = calendar.get(Calendar.MONTH) + 1;
                int quarter = (month - 1) / 3 + 1;
                return "Quarter " + quarter + ", " + calendar.get(Calendar.YEAR);
            case "year":
                return String.valueOf(calendar.get(Calendar.YEAR));
            default:
                return "";
        }
    }

    private String getProductKey(Purchase fact, String product) {
        switch (product.toLowerCase()) {
            case "article":
                return dataWarehouse.getArticleByID(fact.getArticleID()).getName();
            case "productgroup":
                return dataWarehouse.getProductGroupByID(fact.getProductGroupID()).getName();
            case "productfamily":
                return dataWarehouse.getProductFamilyByID(fact.getProductFamilyID()).getName();
            case "productcategory":
                return dataWarehouse.getProductCategoryByID(fact.getProductCategoryID()).getName();
            default:
                return "";
        }
    }

    private void printCrosstab(Map<String, Map<String, Map<String, Double>>> result) {
        Set<String> timeKeys = result.values().stream()
                .flatMap(m1 -> m1.keySet().stream())
                .collect(Collectors.toCollection(TreeSet::new));

        Set<String> productKeys = result.values().stream()
                .flatMap(m1 -> m1.values().stream())
                .flatMap(m2 -> m2.keySet().stream())
                .collect(Collectors.toCollection(TreeSet::new));

        List<String> productKeyList = new ArrayList<>(productKeys);

        System.out.print("|                | Sales |");
        for (String product : productKeyList) {
            System.out.print(" " + product + " |");
        }
        System.out.println(" Total |");

        for (String geoKey : result.keySet()) {
            System.out.println("| **" + geoKey + "** |");

            Map<String, Map<String, Double>> timeMap = result.get(geoKey);

            for (String timeKey : timeKeys) {
                Map<String, Double> productMap = timeMap.getOrDefault(timeKey, new HashMap<>());

                System.out.print("| " + timeKey + " |");
                double totalSales = 0.0;

                for (String product : productKeyList) {
                    double sales = productMap.getOrDefault(product, 0.0);
                    totalSales += sales;
                    System.out.print(" " + (int) sales + " |");
                }

                System.out.println(" " + (int) totalSales + " |");
            }

            System.out.print("| **Total** |");
            double totalGeoSales = 0.0;
            Map<String, Double> totalProductMap = new HashMap<>();

            for (Map<String, Double> productMap : timeMap.values()) {
                for (String product : productKeyList) {
                    totalProductMap.put(product, totalProductMap.getOrDefault(product, 0.0) + productMap.getOrDefault(product, 0.0));
                }
            }

            for (String product : productKeyList) {
                double sales = totalProductMap.getOrDefault(product, 0.0);
                totalGeoSales += sales;
                System.out.print(" " + (int) sales + " |");
            }

            System.out.println(" " + (int) totalGeoSales + " |");
        }

        System.out.print("| **Total** |");
        double grandTotalSales = 0.0;
        Map<String, Double> grandTotalMap = new HashMap<>();

        for (Map<String, Map<String, Double>> timeMap : result.values()) {
            for (Map<String, Double> productMap : timeMap.values()) {
                for (String product : productKeyList) {
                    grandTotalMap.put(product, grandTotalMap.getOrDefault(product, 0.0) + productMap.getOrDefault(product, 0.0));
                }
            }
        }

        for (String product : productKeyList) {
            double sales = grandTotalMap.getOrDefault(product, 0.0);
            grandTotalSales += sales;
            System.out.print(" " + (int) sales + " |");
        }

        System.out.println(" " + (int) grandTotalSales + " |");
    }
}
