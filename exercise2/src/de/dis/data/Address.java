package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Address-Bean
 * 
 * CREATE TABLE address (
 * id INT PRIMARY KEY,
 * city VARCHAR(255),
 * postalcode VARCHAR(255),
 * street VARCHAR(255),
 * streetnumber VARCHAR(255)
 * );
 */
public class Address {
    private int id = -1;
    private String city;
    private String postalcode;
    private String street;
    private String streetnumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetnumber() {
        return streetnumber;
    }

    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }

    public static Address load(int id) {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM address WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Address address = new Address();
                address.setId(id);
                address.setCity(rs.getString("city"));
                address.setPostalcode(rs.getString("postalcode"));
                address.setStreet(rs.getString("street"));
                address.setStreetnumber(rs.getString("streetnumber"));

                rs.close();
                pstmt.close();
                return address;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            if (getId() == -1) {
                String insertSQL = "INSERT INTO address(city, postalcode, street, streetnumber) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

                pstmt.setString(1, getCity());
                pstmt.setString(2, getPostalcode());
                pstmt.setString(3, getStreet());
                pstmt.setString(4, getStreetnumber());

                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }

                rs.close();
                pstmt.close();
            } else {
                String updateSQL = "UPDATE address SET city = ?, postalcode = ?, street = ?, streetnumber = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                pstmt.setString(1, getCity());
                pstmt.setString(2, getPostalcode());
                pstmt.setString(3, getStreet());
                pstmt.setString(4, getStreetnumber());
                pstmt.setInt(5, getId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}