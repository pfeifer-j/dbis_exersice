package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Estate-Bean
 * 
 * Beispiel-Tabelle:
 * CREATE TABLE estate (^^
 * city varchar(255), 
 * postalCode varchar(255), 
 * street varchar(255), 
 * streetNumber int, 
 * floor int,
 * price decimal(10, 2),
 * rooms int, 
 * has_balcony bool, //todo
 * has_kitchen bool,
 * floors int,
 * price decimal(10, 2),
 * has_garden bool,
 * squareArea int, 
 * id serial primary key);
 */
public class Estate {
	private int id = -1;
    private String city;
    private String postalCode;
    private String street;
    private int streetNumber;
    private int floor;
    private double price;
    private int rooms;
    private boolean hasBalcony;
    private boolean hasKitchen;
    private int floors;
    private boolean hasGarden;
    private int squareArea;
	
	// Getter and Setter methods for city
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Getter and Setter methods for postalCode
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    // Getter and Setter methods for street
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    // Getter and Setter methods for streetNumber
    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    // Getter and Setter methods for floor
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    // Getter and Setter methods for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    // Getter and Setter methods for floors
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    // Getter and Setter methods for hasGarden
    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    // Getter and Setter methods for squareArea
    public int getSquareArea() {
        return squareArea;
    }

    public void setSquareArea(int squareArea) {
        this.squareArea = squareArea;
    }

    // Getter and Setter methods for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
	
	/**
	 * Lädt einen Makler aus der Datenbank
	 * @param id ID des zu ladenden Maklers
	 * @return Makler-Instanz
	 */
    // Load method
    public static Estate load(int id) {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM estate WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Estate estate = new Estate();
                estate.setId(id);
                estate.setCity(rs.getString("city"));
                estate.setPostalCode(rs.getString("postalCode"));
                estate.setStreet(rs.getString("street"));
                estate.setStreetNumber(rs.getInt("streetNumber"));
                estate.setFloor(rs.getInt("floor"));
                estate.setPrice(rs.getDouble("price"));
                estate.setRooms(rs.getInt("rooms"));
                estate.setHasBalcony(rs.getBoolean("has_balcony"));
                estate.setHasKitchen(rs.getBoolean("has_kitchen"));
                estate.setFloors(rs.getInt("floors"));
                estate.setHasGarden(rs.getBoolean("has_garden"));
                estate.setSquareArea(rs.getInt("squareArea"));

                rs.close();
                pstmt.close();
                return estate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Save method
    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO estate(city, postalCode, street, streetNumber, floor, price, rooms, has_balcony, has_kitchen, floors, has_garden, squareArea) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

                pstmt.setString(1, getCity());
                pstmt.setString(2, getPostalCode());
                pstmt.setString(3, getStreet());
                pstmt.setInt(4, getStreetNumber());
                pstmt.setInt(5, getFloor());
                pstmt.setDouble(6, getPrice());
                pstmt.setInt(7, getRooms());
                pstmt.setBoolean(8, isHasBalcony());
                pstmt.setBoolean(9, isHasKitchen());
                pstmt.setInt(10, getFloors());
                pstmt.setBoolean(11, isHasGarden());
                pstmt.setInt(12, getSquareArea());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }

                rs.close();
                pstmt.close();
            } else {
                String updateSQL = "UPDATE estate SET city = ?, postalCode = ?, street = ?, streetNumber = ?, floor = ?, price = ?, rooms = ?, has_balcony = ?, has_kitchen = ?, floors = ?, has_garden = ?, squareArea = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                pstmt.setString(1, getCity());
                pstmt.setString(2, getPostalCode());
                pstmt.setString(3, getStreet());
                pstmt.setInt(4, getStreetNumber());
                pstmt.setInt(5, getFloor());
                pstmt.setDouble(6, getPrice());
                pstmt.setInt(7, getRooms());
                pstmt.setBoolean(8, isHasBalcony());
                pstmt.setBoolean(9, isHasKitchen());
                pstmt.setInt(10, getFloors());
                pstmt.setBoolean(11, isHasGarden());
                pstmt.setInt(12, getSquareArea());
                pstmt.setInt(13, getId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}