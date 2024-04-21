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

    public TenancyContract(int contractNumber, Date date, String place, Date startDate, double duration,
            double additionalCosts) {
        super();
        this.startDate = startDate;
        this.duration = duration;
        this.additionalCosts = additionalCosts;
    }

    public TenancyContract() {
        super();
    }

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

    public void saveTenancyContract() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {

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
                    String insertTenancy = "INSERT INTO tenancy_contract (start_date, duration, additional_costs, id) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmtTenancy = con.prepareStatement(insertTenancy, Statement.RETURN_GENERATED_KEYS);
                    pstmtTenancy.setDate(1, startDate);
                    pstmtTenancy.setDouble(2, duration);
                    pstmtTenancy.setDouble(3, additionalCosts);
                    pstmtTenancy.setInt(4, getId());
                    pstmtTenancy.executeUpdate();
                    pstmtTenancy.close();
                }
            } else {
                String insertSQL = "UPDATE contract SET date = ?, place = ? WHERE contract_number = ?";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setDate(1, getDate());
                pstmt.setString(2, getPlace());
                pstmt.setInt(2, getId());
                pstmt.executeUpdate();

                String updateSQL = "UPDATE tenancy_contract SET start_date = ?, duration = ?, additional_costs = ? WHERE id = ?";
                PreparedStatement pstmtTenancy = con.prepareStatement(updateSQL);
                pstmtTenancy.setDate(1, startDate);
                pstmtTenancy.setDouble(2, duration);
                pstmtTenancy.setDouble(3, additionalCosts);
                pstmtTenancy.setInt(4, getId());
                pstmtTenancy.executeUpdate();
                pstmtTenancy.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TenancyContract loadTenancyContract(int contractNumber) {
        TenancyContract contract = null;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM contract JOIN tenancy_contract ON contract.contract_number = tenancy_contract.id WHERE contract.contract_number = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, contractNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                contract = new TenancyContract();
                contract.setId(rs.getInt("contract_number"));
                contract.setStartDate(rs.getDate("date"));
                contract.setPlace(rs.getString("place"));
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
