package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class House extends Estate {
    private int floors;
    private double price;
    private boolean hasGarden;

    public House() {
        super();
    }

    public House(int id, double square, int agent, int floors, double price, boolean hasGarden, String street,
            String streetNumber, String city, String postalCode) {
        super(id, square, agent, street, streetNumber, city, postalCode);
        this.floors = floors;
        this.price = price;
        this.hasGarden = hasGarden;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    public static House loadHouse(int id) {
        House house = null;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM estate JOIN house ON estate.id = house.id WHERE estate.id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                house = new House();
                house.setId(rs.getInt("id"));
                house.setSquare(rs.getDouble("square"));
                house.setAgent(rs.getInt("agent_id"));
                // Set Estate attributes using super
                house.setCity(rs.getString("city"));
                house.setStreet(rs.getString("street"));
                house.setStreetNumber(rs.getString("streetnumber"));
                house.setPostalCode(rs.getString("postalcode"));
                house.setFloors(rs.getInt("floors"));
                house.setPrice(rs.getDouble("price"));
                house.setHasGarden(rs.getBoolean("garden"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return house;
    }

    public void saveHouse() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO estate (square, agent_id, city, street, streetnumber, postalcode) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setDouble(1, getSquare());
                pstmt.setInt(2, getAgent());
                // Set Estate attributes using super
                pstmt.setString(3, getCity());
                pstmt.setString(4, getStreet());
                pstmt.setString(5, getStreetNumber());
                pstmt.setString(6, getPostalCode());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                int id = -1;
                if (rs.next()) {
                    id = rs.getInt(1);
                    this.setId(id);
                }
                rs.close();
                pstmt.close();

                if (id != -1) {
                    String insertHouseSQL = "INSERT INTO house (id, floors, price, garden) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmtHouse = con.prepareStatement(insertHouseSQL);
                    pstmtHouse.setInt(1, id);
                    pstmtHouse.setInt(2, getFloors());
                    pstmtHouse.setDouble(3, getPrice());
                    pstmtHouse.setBoolean(4, isHasGarden());
                    pstmtHouse.executeUpdate();
                    pstmtHouse.close();
                }
            } else {
                String updateSQL = "UPDATE estate SET square = ?, agent_id = ?, city = ?, street = ?, streetnumber = ?, postalcode = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);
                pstmt.setDouble(1, getSquare());
                pstmt.setInt(2, getAgent());
                pstmt.setString(3, getCity());
                pstmt.setString(4, getStreet());
                pstmt.setString(5, getStreetNumber());
                pstmt.setString(6, getPostalCode());
                pstmt.setInt(7, getId());
                pstmt.executeUpdate();
                pstmt.close();

                String updateHouseSQL = "UPDATE house SET floors = ?, price = ?, garden = ? WHERE id = ?";
                PreparedStatement pstmtHouse = con.prepareStatement(updateHouseSQL);
                pstmtHouse.setInt(1, getFloors());
                pstmtHouse.setDouble(2, getPrice());
                pstmtHouse.setBoolean(3, isHasGarden());
                pstmtHouse.setInt(4, getId());
                pstmtHouse.executeUpdate();
                pstmtHouse.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHouse() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            String deleteHouseSQL = "DELETE FROM house WHERE id = ?";
            PreparedStatement pstmtHouse = con.prepareStatement(deleteHouseSQL);
            pstmtHouse.setInt(1, getId());
            pstmtHouse.executeUpdate();
            pstmtHouse.close();

            String deleteEstateSQL = "DELETE FROM estate WHERE id = ?";
            PreparedStatement pstmtEstate = con.prepareStatement(deleteEstateSQL);
            pstmtEstate.setInt(1, getId());
            pstmtEstate.executeUpdate();
            pstmtEstate.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
