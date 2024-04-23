package de.dis.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PurchaseContract extends Contract {
    private int numberOfInstallments;
    private double interestRate;

    
    public PurchaseContract() {
        super();
    }

    public PurchaseContract(int contractNumber, Date date, String place, int numberOfInstallments, double interestRate) {
        super();
        this.numberOfInstallments = numberOfInstallments;
        this.interestRate = interestRate;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void savePurchaseContract() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                // Insert new purchase contract
                String insertSQL = "INSERT INTO contract (date, place) VALUES (?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setDate(1, getDate());
                pstmt.setString(2, getPlace());
                pstmt.executeUpdate();
    
                ResultSet rs = pstmt.getGeneratedKeys();
                int id = -1;
                if (rs.next()) {
                    id = rs.getInt(1);
                    setId(id);
                }
                rs.close();
                pstmt.close();
    
                if (id != -1) {
                    // Insert into purchase_contract table
                    String insertPurchase = "INSERT INTO purchase_contract (number_of_installments, interest_rate, id) VALUES (?, ?, ?)";
                    PreparedStatement pstmtPurchase = con.prepareStatement(insertPurchase);
                    pstmtPurchase.setInt(1, numberOfInstallments);
                    pstmtPurchase.setDouble(2, interestRate);
                    pstmtPurchase.setInt(3, getId());
                    pstmtPurchase.executeUpdate();
                    pstmtPurchase.close();
                }
            } else {
                // Update existing purchase contract
                String updateSQL = "UPDATE contract SET date = ?, place = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);
                pstmt.setDate(1, getDate());
                pstmt.setString(2, getPlace());
                pstmt.setInt(3, getId());
                pstmt.executeUpdate();
                pstmt.close();
    
                String updatePurchase = "UPDATE purchase_contract SET number_of_installments = ?, interest_rate = ? WHERE id = ?";
                PreparedStatement pstmtPurchase = con.prepareStatement(updatePurchase);
                pstmtPurchase.setInt(1, numberOfInstallments);
                pstmtPurchase.setDouble(2, interestRate);
                pstmtPurchase.setInt(3, getId());
                pstmtPurchase.executeUpdate();
                pstmtPurchase.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static PurchaseContract loadPurchaseContract(int contractNumber) {
        PurchaseContract contract = null;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM contract JOIN purchase_contract ON contract.id = purchase_contract.id WHERE contract.id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, contractNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                contract = new PurchaseContract();
                contract.setId(rs.getInt("id"));
                contract.setDate(rs.getDate("date"));
                contract.setPlace(rs.getString("place"));
                contract.setNumberOfInstallments(rs.getInt("number_of_installments"));
                contract.setInterestRate(rs.getDouble("interest_rate"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contract;
    }

    public void sells(int sellerId, int houseId){
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO sells (seller_id, house_id, contract_number) VALUES (?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, sellerId);
                pstmt.setInt(2, houseId);
                pstmt.setInt(2, getId());
                pstmt.executeUpdate();

            } else {
                String insertSQL = "UPDATE sells SET seller_id = ?, house_id = ? WHERE contract_number = ?";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, sellerId);
                pstmt.setInt(2, houseId);
                pstmt.setInt(2, getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rents(int tenantId, int apartmentId){
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            //if (getId() == -1) {
                String insertSQL = "INSERT INTO rents (tenant_id, apartment_id, contract_number) VALUES (?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, tenantId);
                pstmt.setInt(2, apartmentId);
                pstmt.setInt(3, getId());
                pstmt.executeUpdate();
                pstmt.close();

            /*} else {
                String insertSQL = "UPDATE rents SET tenant_id = ?, apartment_id = ? WHERE contract_number = ?";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, tenantId);
                pstmt.setInt(2, apartmentId);
                pstmt.setInt(3, getId());
                pstmt.executeUpdate();
                pstmt.close();
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String loadOverview() {
        List<String> overviews = new ArrayList<>();
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT sells.contract_number, sells.seller_id, sells.house_id, " +
                    "contract.date, contract.place, purchase_contract.interest_rate, " +
                    "purchase_contract.number_of_installments " +
                    "FROM sells " +
                    "INNER JOIN contract ON sells.contract_number = contract.contract_number " +
                    "INNER JOIN purchase_contract ON sells.contract_number = purchase_contract.id";
    
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                StringBuilder overview = new StringBuilder();
                overview.append("Contract Number: ").append(rs.getInt("contract_number")).append("\n");
                overview.append("Seller ID: ").append(rs.getInt("seller_id")).append("\n");
                overview.append("House ID: ").append(rs.getInt("house_id")).append("\n");
                overview.append("Date: ").append(rs.getDate("date")).append("\n");
                overview.append("Place: ").append(rs.getString("place")).append("\n");
                overview.append("Interest Rate: ").append(rs.getDouble("interest_rate")).append("\n");
                overview.append("Number of Installments: ").append(rs.getInt("number_of_installments")).append("\n");
    
                overviews.add(overview.toString());
            }
    
            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.join("\n", overviews);
    }
    
}
