package de.dis.data.done;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.dis.data.DbConnectionManager;

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

    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO estate (square, agent_id, street, street_number, city, postal_code) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

                pstmt.setDouble(1, getSquare());
                pstmt.setInt(2, getAgent());
                pstmt.setString(3, getStreet());
                pstmt.setString(4, getStreetNumber());
                pstmt.setString(5, getCity());
                pstmt.setString(6, getPostalCode());

                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.setId(rs.getInt(1));
                }

                rs.close();
                pstmt.close();
            } else {
                String updateSQL = "UPDATE estate SET square = ?, agent_id = ?, street = ?, street_number = ?, city = ?, postal_code = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                pstmt.setDouble(1, getSquare());
                pstmt.setInt(2, getAgent());
                pstmt.setString(3, getStreet());
                pstmt.setString(4, getStreetNumber());
                pstmt.setString(5, getCity());
                pstmt.setString(6, getPostalCode());
                pstmt.setInt(7, getId());

                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                estate.setStreetNumber(rs.getString("street_number"));
                estate.setCity(rs.getString("city"));
                estate.setPostalCode(rs.getString("postal_code"));
                estates.add(estate);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estates;
    }

    public static Estate load(int id) {
        Estate estate = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM estate WHERE id = ?";
            pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int estateId = rs.getInt("id");
                boolean isApartment = isApartment(estateId);
                boolean isHouse = isHouse(estateId);

                if (isApartment) {
                    estate = Apartment.load(id);
                } else if (isHouse) {
                    estate = House.load(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return estate;
    }

    private static boolean isApartment(int estateId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isApartment = false;

        try {
            con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM apartment WHERE estate_id = ?";
            pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, estateId);
            rs = pstmt.executeQuery();
            isApartment = rs.next();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isApartment;
    }

    private static boolean isHouse(int estateId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isHouse = false;

        try {
            con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM house WHERE estate_id = ?";
            pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, estateId);
            rs = pstmt.executeQuery();
            isHouse = rs.next();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isHouse;
    }
}
