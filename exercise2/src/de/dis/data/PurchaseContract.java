package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PurchaseContract extends Contract {
    private int numberOfInstallments;
    private double interestRate;

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

    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO purchase_contract (number_of_installments, interest_rate, number) VALUES (?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, numberOfInstallments);
                pstmt.setDouble(2, interestRate);
                pstmt.setInt(3, getId());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }
                rs.close();
                pstmt.close();
            } else {
                String updateSQL = "UPDATE purchase_contract SET number_of_installments = ?, interest_rate = ? WHERE number = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);
                pstmt.setInt(1, numberOfInstallments);
                pstmt.setDouble(2, interestRate);
                pstmt.setInt(3, getId());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PurchaseContract load(int contractNumber) {
        PurchaseContract contract = null;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM purchase_contract WHERE number = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, contractNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                contract = new PurchaseContract();
                contract.setId(contractNumber);
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
