package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Real Estate Agent Bean
 * 
 * Example Table:
 * CREATE TABLE estate_agent (
 * name VARCHAR(255),
 * address VARCHAR(255),
 * login VARCHAR(255) PRIMARY KEY,
 * password VARCHAR(255)
 * );
 */
public class Agent {
	private int id = -1;
	private String name;
	// private Address address;
	private String address;
	private String login;
	private String password="";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String oldAddress){
		this.address = oldAddress;
	}

	/*
	 public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	*/

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Loads a real estate agent from the database
	 * 
	 * @param id ID of the real estate agent to load
	 * @return RealEstateAgent instance
	 */
	public static Agent loadById(int id) {
		Agent ts = new Agent();

		try {
			// Get connection
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Create query
			String selectSQL = "SELECT * FROM estate_agent WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Execute query
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				ts.setId(id);
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setLogin(rs.getString("login"));
				ts.setPassword(rs.getString("password"));

				rs.close();
				pstmt.close();
				return ts;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads a real estate agent from the database
	 * 
	 * @param id ID of the real estate agent to load
	 * @return RealEstateAgent instance
	 */
	public static Agent loadByLogin(String login) {
		Agent ts = new Agent();
		try {
			// Get connection
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Create query
			String selectSQL = "SELECT * FROM estate_agent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, login);

			// Execute query
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				ts.setId(rs.getInt("id"));
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setLogin(rs.getString("login"));
				ts.setPassword(rs.getString("password"));

				rs.close();
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ts;

	}

	/**
	 * Loads all agents.
	 * @return list of all agents.
	 */
	public static List<Agent> loadAll() {
		List<Agent> realEstateAgents = new ArrayList<>();
		try {
			Connection con = DbConnectionManager.getInstance().getConnection();
			String selectSQL = "SELECT * FROM estate_agent";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Agent realEstateAgent = new Agent();
				realEstateAgent.setId(rs.getInt("id"));
				realEstateAgent.setLogin(rs.getString("login"));
				realEstateAgent.setName(rs.getString("name"));
				realEstateAgents.add(realEstateAgent);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return realEstateAgents;
	}

	/**
	 * Saves the agent in the database.
	 */
	public void save() {
		try {
			// Get connection
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Check if the real estate agent is new or existing
			if (getId() == -1) {
				// If the real estate agent is new, insert it into the estate_agent table
				String insertAgentSQL = "INSERT INTO estate_agent(name, address, login, password) VALUES (?, ?, ?, ?)";
				PreparedStatement agentPstmt = con.prepareStatement(insertAgentSQL, Statement.RETURN_GENERATED_KEYS);
				agentPstmt.setString(1, getName());
				agentPstmt.setString(2, getAddress());
				agentPstmt.setString(3, getLogin());
				agentPstmt.setString(4, getPassword());
				agentPstmt.executeUpdate();

				// Get the generated agent_id
				ResultSet rs = agentPstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}
				rs.close();
				agentPstmt.close();
			} else {
				// If the real estate agent exists, update it
				String updateAgentSQL = "UPDATE estate_agent SET name = ?, address = ?, login = ?, password = ? WHERE id = ?";
				PreparedStatement agentUpdatePstmt = con.prepareStatement(updateAgentSQL);
				agentUpdatePstmt.setString(1, getName());
				agentUpdatePstmt.setString(2, getAddress());
				agentUpdatePstmt.setString(3, getLogin());
				agentUpdatePstmt.setString(4, getPassword());
				agentUpdatePstmt.setInt(5, getId());
				agentUpdatePstmt.executeUpdate();
				agentUpdatePstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the address of an agent
	 * Not used atm.
	 */
	/*
	public void saveAddress() {
		try {
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Check if the address needs to be saved or updated
			if (getAddress().getId() == -1) {
				// If the address is new, insert it into the address table
				String insertAddressSQL = "INSERT INTO address(city, postalcode, street, streetnumber) VALUES (?, ?, ?, ?)";
				PreparedStatement addressPstmt = con.prepareStatement(insertAddressSQL,
						Statement.RETURN_GENERATED_KEYS);
				addressPstmt.setString(1, getAddress().getCity());
				addressPstmt.setString(2, getAddress().getPostalcode());
				addressPstmt.setString(3, getAddress().getStreet());
				addressPstmt.setString(4, getAddress().getStreetnumber());
				addressPstmt.executeUpdate();

				// Get the generated address_id
				ResultSet rs = addressPstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}
				rs.close();
				addressPstmt.close();
			} else if (getAddress().getId() != -1) {
				// If the address exists, update it
				String updateAddressSQL = "UPDATE address SET city = ?, postalcode = ?, street = ?, streetnumber = ? WHERE id = ?";
				PreparedStatement addressUpdatePstmt = con.prepareStatement(updateAddressSQL);
				addressUpdatePstmt.setString(1, getAddress().getCity());
				addressUpdatePstmt.setString(2, getAddress().getPostalcode());
				addressUpdatePstmt.setString(3, getAddress().getStreet());
				addressUpdatePstmt.setString(4, getAddress().getStreetnumber());
				addressUpdatePstmt.setInt(5, getAddress().getId());
				addressUpdatePstmt.executeUpdate();
				addressUpdatePstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/

	// Delete a agent.
	public void delete() {
		if (getId() == -1) {
			System.out.println("Cannot delete estate with invalid ID.");
			return;
		}

		Connection con = DbConnectionManager.getInstance().getConnection();

		/*
		try {
			String deleteSQL = "DELETE FROM address WHERE owner_id = ?";
			PreparedStatement pstmt = con.prepareStatement(deleteSQL);
			pstmt.setInt(1, getId());
			int rowsAffected = pstmt.executeUpdate();
			pstmt.close();

			if (rowsAffected > 0) {
				System.out.println("Address with ID " + getId() + " has been deleted.");
			} else {
				System.out.println("No address with ID " + getId() + " found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 */
		try {
			String deleteSQL = "DELETE FROM estate_agent WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(deleteSQL);
			pstmt.setInt(1, getId());
			int rowsAffected = pstmt.executeUpdate();
			pstmt.close();

			if (rowsAffected > 0) {
				System.out.println("Real estate agent with ID " + getId() + " has been deleted.");
			} else {
				System.out.println("No real estate agent with ID " + getId() + " found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}