package de.dis;


public class Main {

    public static void main(String[] args) {
        DataWarehouse manager = new DataWarehouse();

        // Exctract
        manager.loadSalesDataFromCSV();
        manager.loadDimensionDataFromDatabase();

        // Transform
        manager.transformToSalesFacts();

        // Load
        manager.aggregateSalesByDimensions();
    }
}