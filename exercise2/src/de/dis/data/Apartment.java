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

    public Apartment(int id, double square, int agent, int floor, double rent, int rooms,
            boolean hasBalcony, boolean hasKitchen, String street, String streetNumber, String city, String postalCode) {
        super(id, square, agent, street, streetNumber, city, postalCode );
        this.floor = floor;
        this.rent = rent;
        this.rooms = rooms;
        this.hasBalcony = hasBalcony;
        this.hasKitchen = hasKitchen;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public boolean isHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    public boolean isHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }

    public static Apartment loadApartment(int id) {
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
                apartment.setSquare(rs.getDouble("square"));
                apartment.setAgent(rs.getInt("agent_id"));
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

    public void saveApartment() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO estate (square, agent_id, city, street, streetnumber, postalcode) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setDouble(1, getSquare());
                pstmt.setInt(2, getAgent());
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
                    String insertApartmentSQL = "INSERT INTO apartment (id, floor, rent, rooms, balcony, kitchen) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmtApartment = con.prepareStatement(insertApartmentSQL);
                    pstmtApartment.setInt(1, id);
                    pstmtApartment.setInt(2, getFloor());
                    pstmtApartment.setDouble(3, getRent());
                    pstmtApartment.setInt(4, getRooms());
                    pstmtApartment.setBoolean(5, isHasBalcony());
                    pstmtApartment.setBoolean(6, isHasKitchen());
                    pstmtApartment.executeUpdate();
                    pstmtApartment.close();
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

    public void deleteApartment() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            String deleteApartmentSQL = "DELETE FROM apartment WHERE id = ?";
            PreparedStatement pstmtApartment = con.prepareStatement(deleteApartmentSQL);
            pstmtApartment.setInt(1, getId());
            pstmtApartment.executeUpdate();
            pstmtApartment.close();

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
