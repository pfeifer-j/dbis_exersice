package de.dis;

import java.util.Scanner;

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
        DataAnalysisApp dataAnalysisApp = new DataAnalysisApp(dataWarehouse);

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
}
