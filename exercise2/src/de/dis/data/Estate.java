package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Estate {
    private int id = -1;
    private double square;
    private int agent;
    private String street;
    private String streetNumber;
    private String city;
    private String postalCode;

    public Estate() {
    }

    public Estate(int id, double square, int agent, String street, String streetNumber, String city, String postalCode) {
        this.id = id;
        this.square = square;
        this.agent = agent;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public int getAgent() {
        return agent;
    }

    public void setAgent(int agent) {
        this.agent = agent;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Estate{" +
                "id=" + id +
                ", square=" + square +
                ", agent='" + agent + '\'' +
                '}';
    }

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
                estate.setSquare(rs.getDouble("square"));
                estate.setAgent(rs.getInt("agent_id"));
                estate.setStreet(rs.getString("street"));
                estate.setStreetNumber(rs.getString("streetnumber"));
                estate.setCity(rs.getString("city"));
                estate.setPostalCode(rs.getString("postalcode"));
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
