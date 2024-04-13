package de.dis;

import de.dis.data.Makler;
import de.dis.data.Estate;
import de.dis.data.House;

import java.util.List;

import de.dis.data.Address;
import de.dis.data.Apartment;
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
					if (loginMaklerMenu()) {
						showAccountMenu();
					}
					break;
				case MENU_ESTATE:
					if (loginEstateMenu()) {
						showEstateMenu();
					}
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
	 * Legt einen neuen Makler an
	 */
	private static void createMakler() {
		Makler makler = new Makler();

		makler.setName(FormUtil.readString("Name"));
		makler.setAddress(FormUtil.readString("Adresse"));
		makler.setLogin(FormUtil.readString("Login"));
		makler.setPassword(FormUtil.readString("Passwort"));
		makler.save();

		System.out.println("Makler mit der ID " + makler.getId() + " wurde erzeugt.");
	}

	/**
	 * Bearbeitet einen bestehenden Makler
	 */
	private static void updateMakler() {
		// Lade alle Makler
		List<Makler> maklers = Makler.loadAll();
		System.out.println("Alle Makler:");
		for (Makler makler : maklers) {
			System.out.println(makler.getId() + ": " + makler.getName());
		}

		// Finde Makler
		int maklerIdToUpdate = FormUtil.readInt("Makler ID: ");

		Makler maklerToUpdate = null;
		for (Makler makler : maklers) {
			if (makler.getId() == maklerIdToUpdate) {
				maklerToUpdate = makler;
				break;
			}
		}
		if (maklerToUpdate == null) {
			System.out.println("Makler mit ID " + maklerIdToUpdate + " nicht gefunden.");
			return;
		}

		// Zeige Makler
		System.out.println("Current information of the Makler:");
		System.out.println("ID: " + maklerToUpdate.getId());
		System.out.println("Name: " + maklerToUpdate.getName());
		System.out.println("Address: " + maklerToUpdate.getAddress()); // todo
		System.out.println("Login: " + maklerToUpdate.getLogin());

		String newName = FormUtil.readString("Name: ");
		if (!newName.isEmpty()) {
			maklerToUpdate.setName(newName);
		}
		String newAddress = FormUtil.readString("Addresse: ");
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
		listMaklers();
		Makler makler = new Makler();
		makler.setId(FormUtil.readInt("ID"));
		makler.delete();
		listMaklers();
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

	/**
	 * Legt neue Immobilie an.
	 */
	private static void createEstate() {
		// Prompt the user to choose between creating a house or an apartment
		System.out.println("Welche Art von Immobilie möchten Sie erstellen?");
		System.out.println("1. Haus");
		System.out.println("2. Wohnung");
		int estateTypeChoice = FormUtil.readInt("Ihre Wahl");

		Estate estate;
		switch (estateTypeChoice) {
			case 1:
				House house = new House();
				house.setFloors(FormUtil.readInt("Etagenanzahl"));
				house.setPrice(FormUtil.readDouble("Preis"));
				house.setHasGarden(FormUtil.readBoolean("Hat Garten (true/false)"));
				estate = house;
				break;
			case 2:
				Apartment apartment = new Apartment();
				apartment.setFloor(FormUtil.readInt("Stockwerk"));
				apartment.setRent(FormUtil.readDouble("Miete"));
				apartment.setRooms(FormUtil.readInt("Zimmer"));
				apartment.setHasBalcony(FormUtil.readBoolean("Hat Balkon (true/false)"));
				apartment.setHasKitchen(FormUtil.readBoolean("Hat Küche (true/false)"));
				estate = apartment;
				break;
			default:
				System.out.println("Ungültige Auswahl.");
				return;
		}

		// Proceed with setting the common details
		System.out.println("Neue Immobilie anlegen:");
		Address address = new Address();
		address.setCity(FormUtil.readString("Stadt"));
		address.setPostalcode(FormUtil.readString("Postleitzahl"));
		address.setStreet(FormUtil.readString("Straße"));
		address.setStreetnumber(FormUtil.readString("Hausnummer"));
		address.save();

		estate.setAddressId(address.getId());
		estate.setSquare(FormUtil.readDouble("Grundstücksfläche"));
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

		// Load the estate
		Estate estate = Estate.load(idToUpdate);

		// Prompt the user for estate details
		System.out.println("Ändern der Immobilie mit ID " + estate.getId());

		// Prompt the user for address details
		Address address = new Address();
		address.setCity(FormUtil.readString("Stadt"));
		address.setPostalcode(FormUtil.readString("Postleitzahl"));
		address.setStreet(FormUtil.readString("Straße"));
		address.setStreetnumber(FormUtil.readString("Hausnummer"));
		address.save();

		// Update the estate in the database
		estate.setAddressId(address.getId());
		estate.save();

		listEstates();
	}

	/**
	 * Löscht eine bestehende Immobilie.
	 */
	private static void deleteEstate() {
		listEstates();
		int idToDelete = FormUtil.readInt("ID of estate to delete");

		// Check if the ID exists
		if (!isExistingEstateId(idToDelete)) {
			System.out.println("Estate with ID " + idToDelete + " does not exist.");
			return;
		}

		// Load the estate
		Estate estate = Estate.load(idToDelete);

		// Check if the estate is a house or an apartment
		if (estate instanceof House) {
			// If it's a house, call the delete method for houses
			((House) estate).delete();
		} else if (estate instanceof Apartment) {
			// If it's an apartment, call the delete method for apartments
			((Apartment) estate).delete();
		} else {
			// For general estates, just call the delete method
			System.out.println("Immobilie mit ID " + idToDelete + " konnte nicht gelöscht werden.");
		}

		System.out.println("Immobilie mit ID " + idToDelete + " wurde gelöscht.");
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
	 * Listet alle Makler auf.
	 */
	private static void listMaklers() {
		List<Makler> maklers = Makler.loadAll();
		if (maklers != null && !maklers.isEmpty()) {
			for (Makler makler : maklers) {
				System.out.println("Makler ID: " + makler.getId());
				System.out.println("Name: " + makler.getLogin());
				System.out.println("------------------------");
			}
		} else {
			System.out.println("Keine Makler vorhanden.");
		}
	}

	/**
	 * Listet alle Immobilien auf. todoy
	 */
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

	private static boolean loginEstateMenu() {
		String login = FormUtil.readString("Login");
		String password = FormUtil.readString("Passwort");

		Makler makler = Makler.loadByLogin(login);

		return makler.getPassword().equals(password);
	}

	/*
	 * Login für Makler.
	 */
	private static boolean loginMaklerMenu() {
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
}