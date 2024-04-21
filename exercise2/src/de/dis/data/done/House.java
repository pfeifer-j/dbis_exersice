package de.dis.data.done;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis.data.DbConnectionManager;

public class House extends Estate {
    private int floors;
    private double price;
    private boolean hasGarden;

    public House() {
        super();
    }

    public House(int id, double square, int agent, int floors, double price, boolean hasGarden, String street, String streetNumber, String city, String postalCode) {
        super(id, square, agent, street, streetNumber, city, postalCode );
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
                house.setSquare(rs.getDouble("square"));
                house.setAgent(rs.getInt("agent_id"));
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

    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO estate (square, agent_id) VALUES (?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setDouble(1, getSquare());
                pstmt.setInt(2, getAgent());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                int estateId = -1;
                if (rs.next()) {
                    estateId = rs.getInt(1);
                    this.setId(estateId);
                }
                rs.close();
                pstmt.close();

                if (estateId != -1) {
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
                String updateSQL = "UPDATE estate SET square = ?, agent_id = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);
                pstmt.setDouble(1, getSquare());
                pstmt.setInt(2, getAgent());
                pstmt.setInt(3, getId());
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

    public void delete() {
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
