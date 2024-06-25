package de.dis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dis.data.SalesFact;

import java.util.*;
import java.util.stream.Collectors;

public class DataAnalysisInCode {

    private DataWarehouse dataWarehouse;

    public DataAnalysisInCode(DataWarehouse dataWarehouse) {
        this.dataWarehouse = dataWarehouse;
    }

    public void analysis(String geo, String time, String product) {
        List<SalesFact> facts = dataWarehouse.getSalesFacts();

        Map<String, Map<String, Map<String, Double>>> result = new HashMap<>();

        for (SalesFact fact : facts) {
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

    private String getGeoKey(SalesFact fact, String geo) {
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

    private String getTimeKey(SalesFact fact, String time) {
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

    private String getProductKey(SalesFact fact, String product) {
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
