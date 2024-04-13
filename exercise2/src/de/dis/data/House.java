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

    // Constructor
    public House(int id, int addressId, double square, String agent, int floors, double price, boolean hasGarden) {
        super(id, addressId, square, agent);
        this.floors = floors;
        this.price = price;
        this.hasGarden = hasGarden;
    }

    // Getter and Setter methods for floors
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    // Getter and Setter methods for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter and Setter methods for hasGarden
    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    // Load method for House
    public static House load(int id) {
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
                house.setAddressId(rs.getInt("address_id"));
                house.setSquare(rs.getDouble("square"));
                house.setAgent(rs.getString("agent"));
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

    // Save method for House
    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                // If the ID is not set, insert a new record
                String insertSQL = "INSERT INTO estate (address_id, square, agent) VALUES (?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, getAddressId());
                pstmt.setDouble(2, getSquare());
                pstmt.setString(3, getAgent());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                int estateId = -1;
                if (rs.next()) {
                    estateId = rs.getInt(1);
                }
                rs.close();
                pstmt.close();

                if (estateId != -1) {
                    // Insert into the house table using the generated estate ID
                    String insertHouseSQL = "INSERT INTO house (id, floors, price, garden) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmtHouse = con.prepareStatement(insertHouseSQL);
                    pstmtHouse.setInt(1, estateId);
                    pstmtHouse.setInt(2, getFloors());
                    pstmtHouse.setDouble(3, getPrice());
                    pstmtHouse.setBoolean(4, isHasGarden());
                    pstmtHouse.executeUpdate();
                    pstmtHouse.close();
                }
            } else {
                // If the ID is set, update the existing record
                String updateSQL = "UPDATE estate SET address_id = ?, square = ?, agent = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);
                pstmt.setInt(1, getAddressId());
                pstmt.setDouble(2, getSquare());
                pstmt.setString(3, getAgent());
                pstmt.setInt(4, getId());
                pstmt.executeUpdate();
                pstmt.close();

                // Update the house table
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

    // Delete method for House
    public void delete() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            // Delete from the house table
            String deleteHouseSQL = "DELETE FROM house WHERE id = ?";
            PreparedStatement pstmtHouse = con.prepareStatement(deleteHouseSQL);
            pstmtHouse.setInt(1, getId());
            pstmtHouse.executeUpdate();
            pstmtHouse.close();

            // Delete from the estate table
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
