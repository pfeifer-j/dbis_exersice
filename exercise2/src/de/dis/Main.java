package de.dis;

import de.dis.data.Makler;
import de.dis.data.Estate;
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
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Eigentums-Verwaltung", MENU_ESTATE);
		mainMenu.addEntry("Vertrags-Verwaltung", MENU_CONTRACT);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
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
		//Menüoptionen
		final int CREATE_MAKLER = 0;
		final int UPDATE_MAKLER = 1;
		final int DELETE_MAKLER = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", CREATE_MAKLER);
		maklerMenu.addEntry("Makler bearbeiten", UPDATE_MAKLER);
		maklerMenu.addEntry("Makler löschen", DELETE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
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
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
	}

	/**
	 * Bearbeitet einen bestehenden Makler.
	 */
	private static void updateMakler() {
		// todo
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
		if(!loginMakler()){
			return;
		}

		//Menüoptionen
		final int NEW_ESTATE = 0;
		final int UPDATE_ESTATE = 1;
		final int DELETE_ESTATE = 2;
		final int BACK = 3;
		
		// Eigentumsverwaltungsmenü
		Menu estateMenu = new Menu("Eigentums-Verwaltung");
		estateMenu.addEntry("Neues Eigentum", NEW_ESTATE);
		estateMenu.addEntry("Eigentum ändern", UPDATE_ESTATE);
		estateMenu.addEntry("Eigentum löschen", DELETE_ESTATE);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
				case NEW_ESTATE:
					newEstate();
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
	private static boolean loginMakler(){
		// todo
		return true;
	}

	/**
	 * Legt neues Eigentum an.
	 */
	private static void newEstate() {
		Estate estate = new Estate();
		// todo
	}

	/**
	 * Updated ein bestehendes Eigentum.
	 */
	private static void updateEstate() {
		// todo
	}

	/**
	 * Löscht ein bestehendes Eigentum.
	 */
	private static void deleteEstate() {
		// todo
	}

	/**
	 * Zeigt die Vertragsverwaltung
	 */
	private static void showContractMenu() {
		//Menüoptionen
		final int NEW_CONTRACT = 0;
		final int ADD_Person = 1;
		final int LIST_CONTRACTS = 2;
		final int BACK = 3;
		
		Menu contractMenu = new Menu("Vertrags-Verwaltung");
		contractMenu.addEntry("Neuer Vertrag", NEW_CONTRACT);
		contractMenu.addEntry("Person hinzufügen", ADD_Person);
		contractMenu.addEntry("Alle Verträge anzeigen", LIST_CONTRACTS);
		contractMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = contractMenu.show();
			
			switch(response) {
				case NEW_CONTRACT:
					newContract();
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
	private static void newContract() {
		Contract contract = new Contract();

		// todo
	}

	/**
	 * Fügt eine Person zu einem Vertrag hinzu.
	 */
	private static void addPerson() {
		Person person = new Person();
		// todo
	}

	/**
	 * Listet alle Verträge auf.
	 */
	private static void listContracts() {
		// todo
	}
}
