package de.dis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.dis.data.Sale;

public class SaleLoader {

    final String PATH = "ressources/sales.csv";

    public List<Sale> loadSalesFromCSV() {
        List<Sale> sales = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                String[] data = line.split(";", -1);

                if (data.length < 5) {
                    System.err.println("Skipping line with incomplete data: " + line);
                    continue;
                }

                String dateString = data[0];
                String shop = data[1];
                String article = data[2];
                int sold;
                double revenue;

                try {
                    sold = Integer.parseInt(data[3].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line due to invalid 'Sold' value: " + line);
                    continue;
                }

                try {
                    revenue = Double.parseDouble(data[4].replace(",", ".").trim());
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line due to invalid 'Revenue' value: " + line);
                    continue;
                }
                Date date = parseDate(dateString);

                Sale sale = new Sale(date, shop, article, sold, revenue);
                sales.add(sale);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sales;
    }
    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            java.util.Date parsedDate = dateFormat.parse(dateString);
            return new Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
