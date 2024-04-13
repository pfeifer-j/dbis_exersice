package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Estate {
    private int id;
    private int addressId;
    private double square;
    private String agent;

    public Estate() {
        // Default constructor
    }

    public Estate(int id, int addressId, double square, String agent) {
        this.id = id;
        this.addressId = addressId;
        this.square = square;
        this.agent = agent;
    }

    // Getter and Setter methods for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter methods for addressId
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    // Getter and Setter methods for square
    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    // Getter and Setter methods for agent
    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "Estate{" +
                "id=" + id +
                ", addressId=" + addressId +
                ", square=" + square +
                ", agent='" + agent + '\'' +
                '}';
    }

    // Load one
    public static Estate load(int id) {
        Estate estate = null;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM estate WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                estate = new Estate();
                estate.setId(rs.getInt("id"));
                estate.setAddressId(rs.getInt("address_id"));
                estate.setSquare(rs.getDouble("square"));
                estate.setAgent(rs.getString("agent"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estate;
    }

    // Save method
    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO estate (address_id, square, agent) VALUES (?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

                pstmt.setInt(1, getAddressId());
                pstmt.setDouble(2, getSquare());
                pstmt.setString(3, getAgent());

                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }

                rs.close();
                pstmt.close();
            } else {
                String updateSQL = "UPDATE estate SET address_id = ?, square = ?, agent = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                pstmt.setInt(1, getAddressId());
                pstmt.setDouble(2, getSquare());
                pstmt.setString(3, getAgent());
                pstmt.setInt(4, getId());

                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load all method
    public static List<Estate> loadAll() {
        List<Estate> estates = new ArrayList<>();
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM estate";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Estate estate = new Estate();
                estate.setId(rs.getInt("id"));
                estate.setAddressId(rs.getInt("address_id"));
                estate.setSquare(rs.getDouble("square"));
                estate.setAgent(rs.getString("agent"));
                estates.add(estate);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estates;
    }
}