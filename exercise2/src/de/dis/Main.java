package de.dis;

import java.util.List;

import de.dis.data.Agent;
import de.dis.data.Apartment;
import de.dis.data.Contract;
import de.dis.data.Estate;
import de.dis.data.House;
import de.dis.data.Person;
import de.dis.data.PurchaseContract;
import de.dis.data.TenancyContract;

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
		agent.setAddress(FormUtil.readString("Address"));

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
		Agent agentToUpdate = Agent.loadById(agentIdToUpdate);

		System.out.println("Current information of the Agent:");
		System.out.println("ID: " + agentToUpdate.getId());
		System.out.println("Name: " + agentToUpdate.getName());
		System.out.println("Login: " + agentToUpdate.getLogin());
		System.out.println("Address: " + agentToUpdate.getAddress());

		String newName = FormUtil.readString("Enter new name: ");
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
			agentToUpdate.setAddress(address);
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
					updateEstate(agent.getId());
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

		switch (estateTypeChoice) {
			case 1:
				House house = new House();
				house.setFloors(FormUtil.readInt("Number of Floors"));
				house.setPrice(FormUtil.readDouble("Price"));
				house.setHasGarden(FormUtil.readBoolean("Has Garden (true/false)"));
				house.setSquare(FormUtil.readDouble("Square in m^2"));
				house.setCity(FormUtil.readString("City"));
				house.setPostalCode(FormUtil.readString("Postal Code"));
				house.setStreet(FormUtil.readString("Street"));
				house.setStreetNumber(FormUtil.readString("Street Number"));
				house.setAgent(agentId);
				house.saveHouse();
				System.out.println("House with ID " + house.getId() + " has been created.");

				break;
			case 2:
				Apartment apartment = new Apartment();
				apartment.setFloor(FormUtil.readInt("Floor"));
				apartment.setRent(FormUtil.readDouble("Rent"));
				apartment.setRooms(FormUtil.readInt("Rooms"));
				apartment.setHasBalcony(FormUtil.readBoolean("Has Balcony (true/false)"));
				apartment.setHasKitchen(FormUtil.readBoolean("Has Kitchen (true/false)"));
				apartment.setSquare(FormUtil.readDouble("Square in m^2"));
				apartment.setCity(FormUtil.readString("City"));
				apartment.setPostalCode(FormUtil.readString("Postal Code"));
				apartment.setStreet(FormUtil.readString("Street"));
				apartment.setStreetNumber(FormUtil.readString("Street Number"));
				apartment.setAgent(agentId);
				apartment.saveApartment();
				System.out.println("Apartment with ID " + apartment.getId() + " has been created.");
				break;
			default:
				System.out.println("Invalid choice.");
				return;
		}
	}

	private static void updateEstate(int agentId) {
		listEstates();
		;
		System.out.println("Which type of estate do you want to update?");
		System.out.println("1. House");
		System.out.println("2. Apartment");
		int estateTypeChoice = FormUtil.readInt("Your choice");
		int idToUpdate = -1;
		switch (estateTypeChoice) {
			case 1:
				idToUpdate = FormUtil.readInt("ID of house");
				House house = House.loadHouse(idToUpdate);
				house.setFloors(FormUtil.readInt("Number of Floors"));
				house.setPrice(FormUtil.readDouble("Price"));
				house.setHasGarden(FormUtil.readBoolean("Has Garden (true/false)"));
				house.setSquare(FormUtil.readDouble("Square in m^2"));
				house.setCity(FormUtil.readString("City"));
				house.setPostalCode(FormUtil.readString("Postal Code"));
				house.setStreet(FormUtil.readString("Street"));
				house.setStreetNumber(FormUtil.readString("Street Number"));
				house.setAgent(agentId);
				house.saveHouse();
				System.out.println("House with ID " + house.getId() + " has been updated.");

				break;
			case 2:
				idToUpdate = FormUtil.readInt("ID of apartment");
				Apartment apartment = Apartment.loadApartment(idToUpdate);
				apartment.setFloor(FormUtil.readInt("Floor"));
				apartment.setRent(FormUtil.readDouble("Rent"));
				apartment.setRooms(FormUtil.readInt("Rooms"));
				apartment.setHasBalcony(FormUtil.readBoolean("Has Balcony (true/false)"));
				apartment.setHasKitchen(FormUtil.readBoolean("Has Kitchen (true/false)"));
				apartment.setSquare(FormUtil.readDouble("Square in m^2"));
				apartment.setCity(FormUtil.readString("City"));
				apartment.setPostalCode(FormUtil.readString("Postal Code"));
				apartment.setStreet(FormUtil.readString("Street"));
				apartment.setStreetNumber(FormUtil.readString("Street Number"));
				apartment.setAgent(agentId);
				apartment.saveApartment();
				System.out.println("Apartment with ID " + apartment.getId() + " has been updated.");
				break;
			default:
				System.out.println("Invalid choice.");
				return;
		}
		listEstates();
	}

	private static void deleteEstate() {
		listEstates();
		;
		System.out.println("Which type of estate do you want to delete?");
		System.out.println("1. House");
		System.out.println("2. Apartment");
		int estateTypeChoice = FormUtil.readInt("Your choice");
		int idToUpdate = -1;
		switch (estateTypeChoice) {
			case 1:
				idToUpdate = FormUtil.readInt("ID of house");
				House house = House.loadHouse(idToUpdate);
				house.deleteHouse();
				System.out.println("House with ID " + house.getId() + " has been updated.");

				break;
			case 2:
				idToUpdate = FormUtil.readInt("ID of apartment");
				Apartment apartment = Apartment.loadApartment(idToUpdate);
				apartment.deleteApartment();
				System.out.println("Apartment with ID " + apartment.getId() + " has been updated.");
				break;
			default:
				System.out.println("Invalid choice.");
				return;
		}
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
		System.out.println("Which type of contract do you want to create?");
		System.out.println("1. Tenancy Contract");
		System.out.println("2. Purchase Contract");
		int contractChoice = FormUtil.readInt("Your choice");

		switch (contractChoice) {
			case 1:
				TenancyContract tenancyContract = new TenancyContract();
				tenancyContract.setPlace(FormUtil.readString("Place"));
				tenancyContract.setDate(FormUtil.readDate("Date (yyyy-mm-dd)"));
				tenancyContract.setStartDate(FormUtil.readDate("Start Date"));
				tenancyContract.setDuration(FormUtil.readDouble("Duration in years (yyyy-mm-dd)"));
				tenancyContract.setAdditionalCosts(FormUtil.readInt("Additional Costs"));
				tenancyContract.saveTenancyContract();
				System.out.println("Teneancy Contract with ID " + tenancyContract.getId() + " has been created.");
				break;
			case 2:
				PurchaseContract purchaseContract = new PurchaseContract();
				purchaseContract.setDate(FormUtil.readDate("Date (yyyy-mm-dd)"));
				purchaseContract.setPlace(FormUtil.readString("Place"));
				purchaseContract.setInterestRate(FormUtil.readDouble("Interest Rate (%)"));
				purchaseContract.setNumberOfInstallments(FormUtil.readInt("Installments Number"));

				purchaseContract.savePurchaseContract();
				System.out.println("Purchase Contract with ID " + purchaseContract.getId() + " has been created.");
				break;
			default:
				System.out.println("Invalid choice.");
				return;
		}
	}

	private static void addPerson() {
		Person person = new Person();

		System.out.println("Add New Person:");
		person.setFirstName(FormUtil.readString("First Name"));
		person.setLastName(FormUtil.readString("Last Name"));
		person.setAddress(FormUtil.readString("Address"));

		person.save();

		System.out.println("Person with ID " + person.getId() + " has been added.");
	}

	private static void listContracts() {
		List<Contract> contracts = Contract.loadAll();
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
				System.out.println("Contract ID: " + contract.getId());
				System.out.println("Date (yyyy-mm-dd): " + contract.getDate());
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
				System.out.println("Agent ID: " + estate.getAgent());
				System.out.println("Postalcode: " + estate.getPostalCode());
				System.out.println("City: " + estate.getCity());
				System.out.println("Street: " + estate.getStreet());
				System.out.println("Streetnumber: " + estate.getStreetNumber());
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
