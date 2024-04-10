package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Person-Bean
 * 
 * Beispiel-Tabelle:
 * CREATE TABLE person (
 *  first_name varchar(255),
 *  last_name varchar(255),
 *  street varchar(255),
 *  city varchar(255),
 *  postalCode varchar(255)
 */
public class Person {
    private int id = -1;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String postalCode;

    // Getter and Setter methods for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter methods for firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter and Setter methods for lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter and Setter methods for street
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

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

    public static Person load(int id) {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM person WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Person person = new Person();
                person.setId(id);
                person.setFirstName(rs.getString("first_name"));
                person.setLastName(rs.getString("last_name"));
                person.setStreet(rs.getString("street"));
                person.setCity(rs.getString("city"));
                person.setPostalCode(rs.getString("postalCode"));

                rs.close();
                pstmt.close();
                return person;
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
                String insertSQL = "INSERT INTO person(first_name, last_name, street, city, postalCode) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

                pstmt.setString(1, getFirstName());
                pstmt.setString(2, getLastName());
                pstmt.setString(3, getStreet());
                pstmt.setString(4, getCity());
                pstmt.setString(5, getPostalCode());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }

                rs.close();
                pstmt.close();
            } else {
                String updateSQL = "UPDATE person SET first_name = ?, last_name = ?, street = ?, city = ?, postalCode = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                pstmt.setString(1, getFirstName());
                pstmt.setString(2, getLastName());
                pstmt.setString(3, getStreet());
                pstmt.setString(4, getCity());
                pstmt.setString(5, getPostalCode());
                pstmt.setInt(6, getId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
