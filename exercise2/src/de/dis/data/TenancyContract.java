package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class TenancyContract extends Contract {
    private Date startDate;
    private double duration;
    private double additionalCosts;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getAdditionalCosts() {
        return additionalCosts;
    }

    public void setAdditionalCosts(double additionalCosts) {
        this.additionalCosts = additionalCosts;
    }

    @Override
    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO tenancy_contract (start_date, duration, additional_costs, number) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setDate(1, startDate);
                pstmt.setDouble(2, duration);
                pstmt.setDouble(3, additionalCosts);
                pstmt.setInt(4, getId());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }
                rs.close();
                pstmt.close();
            } else {
                String updateSQL = "UPDATE tenancy_contract SET start_date = ?, duration = ?, additional_costs = ? WHERE number = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);
                pstmt.setDate(1, startDate);
                pstmt.setDouble(2, duration);
                pstmt.setDouble(3, additionalCosts);
                pstmt.setInt(4, getId());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TenancyContract load(int contractNumber) {
        TenancyContract contract = null;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM tenancy_contract WHERE number = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, contractNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                contract = new TenancyContract();
                contract.setId(contractNumber);
                contract.setStartDate(rs.getDate("start_date"));
                contract.setDuration(rs.getDouble("duration"));
                contract.setAdditionalCosts(rs.getDouble("additional_costs"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contract;
    }

}
