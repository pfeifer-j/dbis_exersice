package de.dis;

public class Main {

    public static void main(String[] args) {
        DataWarehouse dataWarehouse = new DataWarehouse();

        // Exctract
        dataWarehouse.loadSalesDataFromCSV();
        dataWarehouse.loadDimensionDataFromDatabase();

        // Transform
        dataWarehouse.transformToSalesFacts();

        // Load
        DataAnalysisApp dataAnalysisApp = new DataAnalysisApp(dataWarehouse);

        String geo = "country";    // Example: "country", "region", "city", "shop"
        String time = "month";     // Example: "date", "day", "month", "quarter", "year"
        String product = "productcategory"; // Example: "article", "productgroup", "productfamily", "productcategory"
        
        System.out.println("Performing analysis for geo: " + geo + ", time: " + time + ", product: " + product);
        dataAnalysisApp.analysis(geo, time, product);
    }
}
