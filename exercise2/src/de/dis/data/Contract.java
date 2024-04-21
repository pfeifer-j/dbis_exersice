package de.dis.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class Contract {
    private int id = -1;
    private Date date;
    private String place;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public static List<Contract> loadAll() {
        List<Contract> contracts = new ArrayList<>();
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM contract";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Contract contract = new Contract();
                contract.setId(rs.getInt("contract_number"));
                contract.setDate(rs.getDate("date"));
                contract.setPlace(rs.getString("place"));
                contracts.add(contract);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contracts;
    }
}
