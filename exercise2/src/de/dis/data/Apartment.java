package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Apartment extends Estate {
    private int floor;
    private double rent;
    private int rooms;
    private boolean hasBalcony;
    private boolean hasKitchen;

    public Apartment() {
        super();
    }

    // Constructor
    public Apartment(int id, int addressId, double square, String agent, int floor, double rent, int rooms,
            boolean hasBalcony, boolean hasKitchen) {
        super(id, addressId, square, agent);
        this.floor = floor;
        this.rent = rent;
        this.rooms = rooms;
        this.hasBalcony = hasBalcony;
        this.hasKitchen = hasKitchen;
    }

    // Getter and Setter methods for floor
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    // Getter and Setter methods for rent
    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    // Getter and Setter methods for rooms
    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    // Getter and Setter methods for hasBalcony
    public boolean isHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    // Getter and Setter methods for hasKitchen
    public boolean isHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }

    // Load method for Apartment
    public static Apartment load(int id) {
        Apartment apartment = null;
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM estate JOIN apartment ON estate.id = apartment.id WHERE estate.id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                apartment = new Apartment();
                apartment.setId(rs.getInt("id"));
                apartment.setAddressId(rs.getInt("address_id"));
                apartment.setSquare(rs.getDouble("square"));
                apartment.setAgent(rs.getString("agent"));
                apartment.setFloor(rs.getInt("floor"));
                apartment.setRent(rs.getDouble("rent"));
                apartment.setRooms(rs.getInt("rooms"));
                apartment.setHasBalcony(rs.getBoolean("balcony"));
                apartment.setHasKitchen(rs.getBoolean("kitchen"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartment;
    }

    // Save method for Apartment
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
                    // Insert into the apartment table using the generated estate ID
                    String insertApartmentSQL = "INSERT INTO apartment (id, floor, rent, rooms, balcony, kitchen) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmtApartment = con.prepareStatement(insertApartmentSQL);
                    pstmtApartment.setInt(1, estateId);
                    pstmtApartment.setInt(2, getFloor());
                    pstmtApartment.setDouble(3, getRent());
                    pstmtApartment.setInt(4, getRooms());
                    pstmtApartment.setBoolean(5, isHasBalcony());
                    pstmtApartment.setBoolean(6, isHasKitchen());
                    pstmtApartment.executeUpdate();
                    pstmtApartment.close();
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

                // Update the apartment table
                String updateApartmentSQL = "UPDATE apartment SET floor = ?, rent = ?, rooms = ?, balcony = ?, kitchen = ? WHERE id = ?";
                PreparedStatement pstmtApartment = con.prepareStatement(updateApartmentSQL);
                pstmtApartment.setInt(1, getFloor());
                pstmtApartment.setDouble(2, getRent());
                pstmtApartment.setInt(3, getRooms());
                pstmtApartment.setBoolean(4, isHasBalcony());
                pstmtApartment.setBoolean(5, isHasKitchen());
                pstmtApartment.setInt(6, getId());
                pstmtApartment.executeUpdate();
                pstmtApartment.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete method for Apartment
    public void delete() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            // Delete from the apartment table
            String deleteApartmentSQL = "DELETE FROM apartment WHERE id = ?";
            PreparedStatement pstmtApartment = con.prepareStatement(deleteApartmentSQL);
            pstmtApartment.setInt(1, getId());
            pstmtApartment.executeUpdate();
            pstmtApartment.close();

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
