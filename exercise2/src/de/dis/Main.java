package de.dis;

import de.dis.data.Makler;
import de.dis.data.Estate;

import java.util.List;

import de.dis.data.Contract;
import de.dis.data.Person;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}

	/**
	 * Zeigt das Hauptmenü
	 */
	private static void showMainMenu() {
		// Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;

		// Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Immobilien-Verwaltung", MENU_ESTATE);
		mainMenu.addEntry("Vertrags-Verwaltung", MENU_CONTRACT);
		mainMenu.addEntry("Beenden", QUIT);

		// Verarbeite Eingabe
		while (true) {
			int response = mainMenu.show();

			switch (response) {
				case MENU_MAKLER:
					showAccountMenu();
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
	 * Zeigt die Maklerverwaltung
	 */
	private static void showAccountMenu() {
		if (!loginMakler()) {
			return;
		}

		// Menüoptionen
		final int CREATE_MAKLER = 0;
		final int UPDATE_MAKLER = 1;
		final int DELETE_MAKLER = 2;
		final int BACK = 3;

		// Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", CREATE_MAKLER);
		maklerMenu.addEntry("Makler bearbeiten", UPDATE_MAKLER);
		maklerMenu.addEntry("Makler löschen", DELETE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = maklerMenu.show();

			switch (response) {
				case CREATE_MAKLER:
					createMakler();
					break;
				case UPDATE_MAKLER:
					updateMakler();
					break;
				case DELETE_MAKLER:
					deleteMakler();
					break;
				case BACK:
					return;
			}
		}
	}

	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	private static void createMakler() {
		Makler m = new Makler();

		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();

		System.out.println("Makler mit der ID " + m.getId() + " wurde erzeugt.");
	}

	/**
	 * Bearbeitet einen bestehenden Makler.
	 */
	private static void updateMakler() {
		// Fetch all Maklers from the database
		List<Makler> maklers = Makler.fetchAllMaklers();

		// Display all Maklers to the user along with their IDs
		System.out.println("Available Maklers:");
		for (Makler makler : maklers) {
			System.out.println(makler.getId() + ": " + makler.getName());
		}

		// Prompt the user to enter the ID of the Makler to update
		int maklerIdToUpdate = FormUtil.readInt("Enter the ID of the Makler to update: ");

		// Find the Makler with the specified ID
		Makler maklerToUpdate = null;
		for (Makler makler : maklers) {
			if (makler.getId() == maklerIdToUpdate) {
				maklerToUpdate = makler;
				break;
			}
		}
		if (maklerToUpdate == null) {
			System.out.println("Makler with ID " + maklerIdToUpdate + " not found.");
			return;
		}

		// Display the current information of the Makler
		System.out.println("Current information of the Makler:");
		System.out.println("ID: " + maklerToUpdate.getId());
		System.out.println("Name: " + maklerToUpdate.getName());
		System.out.println("Address: " + maklerToUpdate.getAddress());
		System.out.println("Login: " + maklerToUpdate.getLogin());

		// Prompt the user to update Makler information
		String newName = FormUtil.readString("Enter new name (press Enter to keep the current name): ");
		if (!newName.isEmpty()) {
			maklerToUpdate.setName(newName);
		}
		String newAddress = FormUtil.readString("Enter new address (press Enter to keep the current address): ");
		if (!newAddress.isEmpty()) {
			maklerToUpdate.setAddress(newAddress);
		}
		String newLogin = FormUtil.readString("Enter new login (press Enter to keep the current login): ");
		if (!newLogin.isEmpty()) {
			maklerToUpdate.setLogin(newLogin);
		}
		String newPassword = FormUtil.readString("Enter new password (press Enter to keep the current password): ");
		if (!newPassword.isEmpty()) {
			maklerToUpdate.setPassword(newPassword);
		}

		// Update the Makler in the database
		maklerToUpdate.save();

		System.out.println("Makler with ID " + maklerToUpdate.getId() + " has been updated.");
	}

	/**
	 * Löscht einen bestehenden Makler.
	 */
	private static void deleteMakler() {
		// todo
	}

	/**
	 * Zeigt die Estate-Verwaltung
	 */
	private static void showEstateMenu() {

		// Menüoptionen
		final int NEW_ESTATE = 0;
		final int LIST_ESTATES = 1;
		final int UPDATE_ESTATE = 2;
		final int DELETE_ESTATE = 3;
		final int BACK = 4;

		// Immobilienverwaltungsmenü
		Menu estateMenu = new Menu("Immobilien-Verwaltung");
		estateMenu.addEntry("Alle Immobilien", LIST_ESTATES);
		estateMenu.addEntry("Neue Immobilie", NEW_ESTATE);
		estateMenu.addEntry("Immobilie ändern", UPDATE_ESTATE);
		estateMenu.addEntry("Immobilie löschen", DELETE_ESTATE);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = estateMenu.show();

			switch (response) {
				case LIST_ESTATES:
					listEstates();
					break;
				case NEW_ESTATE:
					createEstate();
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

	/*
	 * Login für Makler.
	 */
	private static boolean loginMakler() {
		// Hardcoded Password
		String securePwd = "000";
		String loginPwd = FormUtil.readString("Login-Password");

		if (securePwd.equals(loginPwd)) {
			System.out.println("Successfull login");
			return true;
		} else {
			System.out.println("Wrong password");
			return false;
		}
	}

	/**
	 * Legt neue Immobilie an.
	 */
	private static void createEstate() {
		Estate estate = new Estate();

		// Prompt the user for estate details
		System.out.println("Neue Immobilie anlegen:");
		estate.setCity(FormUtil.readString("Stadt"));
		estate.setPostalCode(FormUtil.readString("Postleitzahl"));
		estate.setStreet(FormUtil.readString("Straße"));
		estate.setStreetNumber(FormUtil.readInt("Hausnummer"));
		estate.setFloor(FormUtil.readInt("Stockwerk"));
		estate.setPrice(FormUtil.readDouble("Preis"));
		estate.setRent(FormUtil.readDouble("Miete"));
		estate.setRooms(FormUtil.readInt("Zimmer"));
		estate.setHasBalcony(FormUtil.readBoolean("Hat Balkon (true/false)"));
		estate.setHasKitchen(FormUtil.readBoolean("Hat Küche (true/false)"));
		estate.setFloors(FormUtil.readInt("Etagenanzahl"));
		estate.setHasGarden(FormUtil.readBoolean("Hat Garten (true/false)"));
		estate.setSquareArea(FormUtil.readInt("Grundstücksfläche"));

		// Save the estate to the database
		estate.save();

		System.out.println("Immobilie mit der ID " + estate.getId() + " wurde erstellt.");
	}

	/**
	 * Updated eine bestehende Immobilie.
	 */
	private static void updateEstate() {
		listEstates();
		int idToUpdate = FormUtil.readInt("ID of estate");
	
		// Check if the ID exists
		if (!isExistingEstateId(idToUpdate)) {
			System.out.println("Estate with ID " + idToUpdate + " does not exist.");
			return;
		}
	
		// Proceed with updating the estate
		Estate estate = new Estate();
		estate.setId(idToUpdate);
	
		// Prompt the user for estate details
		estate.setCity(FormUtil.readString("Stadt"));
		estate.setPostalCode(FormUtil.readString("Postleitzahl"));
		estate.setStreet(FormUtil.readString("Straße"));
		estate.setStreetNumber(FormUtil.readInt("Hausnummer"));
		estate.setFloor(FormUtil.readInt("Stockwerk"));
		estate.setPrice(FormUtil.readDouble("Preis"));
		estate.setRent(FormUtil.readDouble("Miete"));
		estate.setRooms(FormUtil.readInt("Zimmer"));
		estate.setHasBalcony(FormUtil.readBoolean("Hat Balkon (true/false)"));
		estate.setHasKitchen(FormUtil.readBoolean("Hat Küche (true/false)"));
		estate.setFloors(FormUtil.readInt("Etagenanzahl"));
		estate.setHasGarden(FormUtil.readBoolean("Hat Garten (true/false)"));
		estate.setSquareArea(FormUtil.readInt("Grundstücksfläche"));
		estate.save();
	
		listEstates();
	}
	
	// Check if the estate ID exists in the database
	private static boolean isExistingEstateId(int id) {
		List<Estate> estates = Estate.loadAll();
		for (Estate estate : estates) {
			if (estate.getId() == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Löscht eine bestehende Immobilie.
	 */
	private static void deleteEstate() {
		listEstates();
		Estate estate = new Estate();
		estate.setId(FormUtil.readInt("ID"));
		estate.delete();
		listEstates();
	}

	/**
	 * Zeigt die Vertragsverwaltung
	 */
	private static void showContractMenu() {
		// Menüoptionen
		final int NEW_CONTRACT = 0;
		final int ADD_Person = 1;
		final int LIST_CONTRACTS = 2;
		final int BACK = 3;

		Menu contractMenu = new Menu("Vertrags-Verwaltung");
		contractMenu.addEntry("Neuer Vertrag", NEW_CONTRACT);
		contractMenu.addEntry("Person hinzufügen", ADD_Person);
		contractMenu.addEntry("Alle Verträge anzeigen", LIST_CONTRACTS);
		contractMenu.addEntry("Zurück zum Hauptmenü", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = contractMenu.show();

			switch (response) {
				case NEW_CONTRACT:
					createContract();
					break;
				case ADD_Person:
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

	/**
	 * Legt einen neuen Vertrag an.
	 */
	private static void createContract() {
		Contract contract = new Contract();

		// Prompt the user for contract details
		System.out.println("Neuen Vertrag anlegen:");
		contract.setDate(FormUtil.readDate("Datum (yyyy-MM-dd)"));
		contract.setPlace(FormUtil.readString("Ort"));

		// Save the contract to the database
		contract.save();

		System.out.println("Vertrag mit der ID " + contract.getId() + " wurde erstellt.");
	}

	/**
	 * Fügt eine Person zu einem Vertrag hinzu.
	 */
	private static void addPerson() {
		Person person = new Person();

		// Prompt the user for person details using FormUtil
		System.out.println("Neue Person hinzufügen:");
		person.setCity(FormUtil.readString("Stadt: "));
		person.setFirstName(FormUtil.readString("Vorname: "));
		person.setLastName(FormUtil.readString("Nachname: "));
		person.setStreet(FormUtil.readString("Straße: "));
		person.setPostalCode(FormUtil.readString("Postleitzahl: "));

		// Save the person to the database
		person.save();

		System.out.println("Person mit der ID " + person.getId() + " wurde hinzugefügt.");
	}

	/**
	 * Listet alle Verträge auf.
	 */
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

	/**
	 * Listet alle Immobilien auf.
	 */
	private static void listEstates() {
		List<Estate> estates = Estate.loadAll();
		if (estates != null && !estates.isEmpty()) {
			for (Estate estate : estates) {
				System.out.println("Estate ID: " + estate.getId());
				System.out.println("City: " + estate.getCity());
				System.out.println("Postal Code: " + estate.getPostalCode());
				System.out.println("Street: " + estate.getStreet());
				System.out.println("Street Number: " + estate.getStreetNumber());
				System.out.println("Floor: " + estate.getFloor());
				System.out.println("Price: " + estate.getPrice());
				System.out.println("Rooms: " + estate.getRooms());
				System.out.println("Has Balcony: " + estate.isHasBalcony());
				System.out.println("Has Kitchen: " + estate.isHasKitchen());
				System.out.println("Floors: " + estate.getFloors());
				System.out.println("Has Garden: " + estate.isHasGarden());
				System.out.println("Square Area: " + estate.getSquareArea());
				System.out.println("------------------------");
			}
		} else {
			System.out.println("No estates found.");
		}
	}
}