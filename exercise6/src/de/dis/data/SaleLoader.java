package de.dis.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SaleLoader {

    /**
     * Loads sales data from a CSV file.
     *
     * @param filename The path to the CSV file.
     * @return List of Sale objects loaded from the CSV.
     */
    public List<Sale> loadSalesFromCSV(String filename) {
        List<Sale> sales = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean headerSkipped = false; // To skip the header line
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header line
                }
                // Split the line by the delimiter ";"
                String[] data = line.split(";", -1); // Split into all parts

                // Check if data array length matches expected length
                if (data.length < 5) {
                    System.err.println("Skipping line with incomplete data: " + line);
                    continue;
                }

                // Extract data fields
                String dateString = data[0];   // Date
                String shop = data[1];         // Shop
                String article = data[2];      // Article
                int sold;
                double revenue;

                // Parse 'Sold' and 'Revenue' fields
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

                // Parse date into java.sql.Date object
                Date date = parseDate(dateString);

                // Create Sale object and add to list
                Sale sale = new Sale(date, shop, article, sold, revenue);
                sales.add(sale);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sales;
    }

    /**
     * Parses date string into java.sql.Date object.
     *
     * @param dateString Date string in format "DD.MM.YYYY".
     * @return java.sql.Date object.
     */
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

    /**
     * Stores sales data into the database.
     *
     * @param sales List of Sale objects to be stored.
     */
    public void storeSalesInDB(List<Sale> sales) {
        // Get database connection
        DbConnectionManager dbManager = DbConnectionManager.getInstance();
        Connection con = dbManager.getConnection();
    
        // SQL query to insert sale data
        String sql = "INSERT INTO sales (Date, ShopID, ArticleID, Sold, Revenue) VALUES (?, ?, ?, ?, ?)";
    
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
    
            int count = 0;
            for (Sale sale : sales) {
                int shopID = fetchShopID(con, sale.getShop());
                if (shopID == -1) {
                    System.err.println("Skipping sale due to shop not found: " + sale);
                    continue;
                }
    
                int articleID = fetchArticleID(con, sale.getArticle());
                if (articleID == -1) {
                    System.err.println("Skipping sale due to article not found: " + sale);
                    continue;
                }
    
                pstmt.setDate(1, sale.getDate());
                pstmt.setInt(2, shopID);
                pstmt.setInt(3, articleID);
                pstmt.setInt(4, sale.getSold());
                pstmt.setDouble(5, sale.getRevenue());
                pstmt.addBatch();
    
                count++;
    
                // Execute batch every 50 entries
                if (count % 50 == 0) {
                    int[] batchResult = pstmt.executeBatch();
                    System.out.println("Successfully inserted " + batchResult.length + " records.");
                    pstmt.clearBatch();
                }
            }
    
            // Execute the remaining batch
            int[] batchResult = pstmt.executeBatch();
            System.out.println("Successfully inserted " + batchResult.length + " records.");
    
            con.commit(); // Commit the transaction
            System.out.println("Successfully inserted " + sales.size() + " records into the database.");
    
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback(); // Rollback the transaction if there's an error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                con.setAutoCommit(true); // Always set auto-commit back to true after handling transactions
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    

    /**
     * Main method to load sales data from CSV and store in database.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SaleLoader loader = new SaleLoader();
        List<Sale> sales = loader.loadSalesFromCSV("ressources\\sales.csv");

        // Store sales data in the database
        loader.storeSalesInDB(sales);
    }

    /**
     * Fetches ShopID from the Shop table based on shopName.
     *
     * @param con      Database connection.
     * @param shopName Name of the shop.
     * @return ShopID if found, otherwise -1.
     * @throws SQLException If SQL error occurs.
     */
    private int fetchShopID(Connection con, String shopName) throws SQLException {
        String sql = "SELECT ShopID FROM Shop WHERE Name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, shopName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ShopID");
            }
        }
        return -1; // Return -1 if shop not found
    }

    /**
     * Fetches ArticleID from the Article table based on articleName.
     *
     * @param con         Database connection.
     * @param articleName Name of the article.
     * @return ArticleID if found, otherwise -1.
     * @throws SQLException If SQL error occurs.
     */
    private int fetchArticleID(Connection con, String articleName) throws SQLException {
        String sql = "SELECT ArticleID FROM Article WHERE Name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, articleName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ArticleID");
            }
        }
        return -1; // Return -1 if article not found
    }
}
