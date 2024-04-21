package de.dis;

import java.util.List;

import de.dis.data.Contract;
import de.dis.data.done.Agent;
import de.dis.data.done.Apartment;
import de.dis.data.done.Estate;
import de.dis.data.done.House;
import de.dis.data.done.Person;

/**
 * Main class
 */
public class Main {
	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		showMainMenu();
	}

	/**
	 * Displays the main menu
	 */
	private static void showMainMenu() {
		final int MENU_AGENT = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;

		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("Agent Management", MENU_AGENT);
		mainMenu.addEntry("Estate Management", MENU_ESTATE);
		mainMenu.addEntry("Contract Management", MENU_CONTRACT);
		mainMenu.addEntry("Quit", QUIT);

		while (true) {
			int response = mainMenu.show();

			switch (response) {
				case MENU_AGENT:
					if (loginAgentMenu()) {
						showAccountMenu();
					}
					break;
				case MENU_ESTATE:
					showEstateMenu();
					break;
				case MENU_CONTRACT:
					showContractMenu();
					break;
				case QUIT:
					return;
			}
		}
	}

	/**
	 * Displays the agent management menu
	 */
	private static void showAccountMenu() {
		final int CREATE_AGENT = 0;
		final int UPDATE_AGENT = 1;
		final int DELETE_AGENT = 2;
		final int BACK = 3;

		Menu agentMenu = new Menu("Agent Management");
		agentMenu.addEntry("New Agent", CREATE_AGENT);
		agentMenu.addEntry("Edit Agent", UPDATE_AGENT);
		agentMenu.addEntry("Delete Agent", DELETE_AGENT);
		agentMenu.addEntry("Back to Main Menu", BACK);

		while (true) {
			int response = agentMenu.show();

			switch (response) {
				case CREATE_AGENT:
					createAgent();
					break;
				case UPDATE_AGENT:
					updateAgent();
					break;
				case DELETE_AGENT:
					deleteAgent();
					break;
				case BACK:
					return;
			}
		}
	}

	private static void createAgent() {
		Agent agent = new Agent();
		agent.setName(FormUtil.readString("Name"));
		agent.setLogin(FormUtil.readString("Login"));
		agent.setPassword(FormUtil.readString("Password"));
		agent.setOldAddress(FormUtil.readString("Address"));

		agent.save();

		System.out.println("Agent with ID " + agent.getId() + " has been created.");
	}

	private static void updateAgent() {
		List<Agent> agents = Agent.loadAll();
		System.out.println("All Agents:");
		for (Agent agent : agents) {
			System.out.println(agent.getId() + ": " + agent.getName());
		}

		int agentIdToUpdate = FormUtil.readInt("Agent ID: ");

		Agent agentToUpdate = null;
		for (Agent agent : agents) {
			if (agent.getId() == agentIdToUpdate) {
				agentToUpdate = agent;
				break;
			}
		}
		if (agentToUpdate == null) {
			System.out.println("Agent with ID " + agentIdToUpdate + " not found.");
			return;
		}

		System.out.println("Current information of the Agent:");
		System.out.println("ID: " + agentToUpdate.getId());
		System.out.println("Name: " + agentToUpdate.getName());
		System.out.println("Login: " + agentToUpdate.getLogin());
		System.out.println("Address: " + agentToUpdate.getOldAddress());

		String newName = FormUtil.readString("Name: ");
		if (!newName.isEmpty()) {
			agentToUpdate.setName(newName);
		}
		String newLogin = FormUtil.readString("Enter new login: ");
		if (!newLogin.isEmpty()) {
			agentToUpdate.setLogin(newLogin);
		}
		String newPassword = FormUtil.readString("Enter new password: ");
		if (!newPassword.isEmpty()) {
			agentToUpdate.setPassword(newPassword);
		}

		String address = FormUtil.readString("Enter new address: ");
		if (!address.isEmpty()) {
			agentToUpdate.setOldAddress(address);
		}

		agentToUpdate.save();

		System.out.println("Agent with ID " + agentToUpdate.getId() + " has been updated.");
	}

	private static void deleteAgent() {
		listAgents();
		Agent agent = Agent.loadById(FormUtil.readInt("ID"));
		agent.delete();
		listAgents();
	}

	private static void showEstateMenu() {
		String login = FormUtil.readString("Login");
		String password = FormUtil.readString("Password");

		Agent agent = Agent.loadByLogin(login);

		if (!agent.getPassword().equals(password)) {
			return;
		}

		final int NEW_ESTATE = 0;
		final int LIST_ESTATES = 1;
		final int UPDATE_ESTATE = 2;
		final int DELETE_ESTATE = 3;
		final int BACK = 4;

		Menu estateMenu = new Menu("Estate Management");
		estateMenu.addEntry("All Estates", LIST_ESTATES);
		estateMenu.addEntry("New Estate", NEW_ESTATE);
		estateMenu.addEntry("Edit Estate", UPDATE_ESTATE);
		estateMenu.addEntry("Delete Estate", DELETE_ESTATE);
		estateMenu.addEntry("Back to Main Menu", BACK);

		while (true) {
			int response = estateMenu.show();

			switch (response) {
				case LIST_ESTATES:
					listEstates();
					break;
				case NEW_ESTATE:
					createEstate(agent.getId());
					break;
				case UPDATE_ESTATE:
					updateEstate();
					break;
				case DELETE_ESTATE:
					deleteEstate();
					break;
				case BACK:
					return;
			}
		}
	}

	private static void createEstate(int agentId) {
		System.out.println("Which type of estate do you want to create?");
		System.out.println("1. House");
		System.out.println("2. Apartment");
		int estateTypeChoice = FormUtil.readInt("Your choice");

		Estate estate;
		switch (estateTypeChoice) {
			case 1:
				House house = new House();
				house.setFloors(FormUtil.readInt("Number of Floors"));
				house.setPrice(FormUtil.readDouble("Price"));
				house.setHasGarden(FormUtil.readBoolean("Has Garden (true/false)"));
				estate = house;
				break;
			case 2:
				Apartment apartment = new Apartment();
				apartment.setFloor(FormUtil.readInt("Floor"));
				apartment.setRent(FormUtil.readDouble("Rent"));
				apartment.setRooms(FormUtil.readInt("Rooms"));
				apartment.setHasBalcony(FormUtil.readBoolean("Has Balcony (true/false)"));
				apartment.setHasKitchen(FormUtil.readBoolean("Has Kitchen (true/false)"));
				estate = apartment;
				break;
			default:
				System.out.println("Invalid choice.");
				return;
		}

		System.out.println("Create new estate:");
		estate.setSquare(FormUtil.readDouble("Square"));
		estate.setCity(FormUtil.readString("City"));
		estate.setPostalCode(FormUtil.readString("Postal Code"));
		estate.setStreet(FormUtil.readString("Street"));
		estate.setStreetNumber(FormUtil.readString("Street Number"));

		estate.setAgent(agentId);
		estate.save();

		System.out.println("Estate with ID " + estate.getId() + " has been created.");
	}

	private static void updateEstate() {
		listEstates();
		int idToUpdate = FormUtil.readInt("ID of estate");

		if (!isExistingEstateId(idToUpdate)) {
			System.out.println("Estate with ID " + idToUpdate + " does not exist.");
			return;
		}

		Estate estate = Estate.load(idToUpdate);

		System.out.println("Update Estate with ID " + estate.getId());
		estate.setCity(FormUtil.readString("City"));
		estate.setPostalCode(FormUtil.readString("Postal Code"));
		estate.setStreet(FormUtil.readString("Street"));
		estate.setStreetNumber(FormUtil.readString("Street Number"));

		estate.save();

		listEstates();
	}

	private static void deleteEstate() {
		listEstates();
		int idToDelete = FormUtil.readInt("ID of estate to delete");

		if (!isExistingEstateId(idToDelete)) {
			System.out.println("Estate with ID " + idToDelete + " does not exist.");
			return;
		}

		Estate estate = Estate.load(idToDelete);

		if (estate instanceof House) {
			((House) estate).delete();
		} else if (estate instanceof Apartment) {
			((Apartment) estate).delete();
		} else {
			System.out.println("Estate with ID " + idToDelete + " could not be deleted.");
		}

		System.out.println("Estate with ID " + idToDelete + " has been deleted.");
		listEstates();
	}

	private static void showContractMenu() {
		final int NEW_CONTRACT = 0;
		final int ADD_PERSON = 1;
		final int LIST_CONTRACTS = 2;
		final int BACK = 3;

		Menu contractMenu = new Menu("Contract Management");
		contractMenu.addEntry("New Contract", NEW_CONTRACT);
		contractMenu.addEntry("Add Person", ADD_PERSON);
		contractMenu.addEntry("List All Contracts", LIST_CONTRACTS);
		contractMenu.addEntry("Back to Main Menu", BACK);

		while (true) {
			int response = contractMenu.show();

			switch (response) {
				case NEW_CONTRACT:
					createContract();
					break;
				case ADD_PERSON:
					addPerson();
					break;
				case LIST_CONTRACTS:
					listContracts();
					break;
				case BACK:
					return;
			}
		}
	}

	private static void createContract() {
		Contract contract = new Contract();

		System.out.println("Create New Contract:");
		contract.setDate(FormUtil.readDate("Date (yyyy-MM-dd)"));
		contract.setPlace(FormUtil.readString("Place"));

		contract.save();

		System.out.println("Contract with ID " + contract.getId() + " has been created.");
	}

	private static void addPerson() {
		Person person = new Person();

		System.out.println("Add New Person:");
		person.setFirstName(FormUtil.readString("First Name: "));
		person.setLastName(FormUtil.readString("Last Name: "));
		person.setAddress(FormUtil.readString("Address: "));

		person.save();

		System.out.println("Person with ID " + person.getId() + " has been added.");
	}

	private static void listContracts() {
		List<Contract> contracts = Contract.loadAll();
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
				System.out.println("Contract ID: " + contract.getId());
				System.out.println("Date: " + contract.getDate());
				System.out.println("Place: " + contract.getPlace());
				System.out.println("------------------------");
			}
		} else {
			System.out.println("No contracts found.");
		}
	}

	private static void listAgents() {
		List<Agent> agents = Agent.loadAll();
		if (agents != null && !agents.isEmpty()) {
			for (Agent agent : agents) {
				System.out.println("Agent ID: " + agent.getId());
				System.out.println("Name: " + agent.getLogin());
				System.out.println("------------------------");
			}
		} else {
			System.out.println("No agents available.");
		}
	}

	private static void listEstates() {
		List<Estate> estates = Estate.loadAll();
		if (estates != null && !estates.isEmpty()) {
			for (Estate estate : estates) {
				System.out.println("Estate ID: " + estate.getId());
				System.out.println("------------------------");
			}
		} else {
			System.out.println("No estates found.");
		}
	}

	private static boolean loginAgentMenu() {
		String securePwd = "0000";
		String loginPwd = FormUtil.readString("Login Password");

		if (securePwd.equals(loginPwd)) {
			System.out.println("Successful login");
			return true;
		} else {
			System.out.println("Wrong password");
			return false;
		}
	}
}
