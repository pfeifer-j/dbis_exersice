package de.dis.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    
}
