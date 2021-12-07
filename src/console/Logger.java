/**
 * 
 */
package console;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import hopital.Hopital;
import hopital.IPersonne;
import hopital.expections.ConsoleExeption;
import hopital.patient.Patient;
import hopital.personnels.Administrator;
import hopital.personnels.Medecin;
import hopital.personnels.Personnel;

/**
 * @author Utilisateur
 *
 */
public abstract class Logger {
	
	private static final String help = "-help";
	private static final String listPatients = "-list-patients";
	private static final String listMedecins = "-list-medecins";
	private static final String listAdmins = "-list-admins";
	private static final String listPatientsMedecin = "-list-patients-medecin";
	private static final String stop = "-stop";
	
	private static final String titleMedecin = "Medecins Hopital";
	private static final String titleAdmin = "Admins Hopital";
	private static final String titlePatient = "Patients Hopital";
	
	public static void logCommand() {
		boolean run = true;
		Scanner sc = new Scanner(System.in);
		String command;
		int id;
		System.out.println("+-----------------------------------+");
		System.out.println("|              Hopital              |");
		System.out.println("+-----------------------------------+");
		System.out.println("");
		System.out.println("Rentrez "+help+" pour voir la liste des commandes");
		while(run) {
			try {
				command = sc.nextLine();
				switch (command) {
				case help:
					commandeConsole();
					break;
				case listPatients:
					loggerListObject(Hopital.getPatients(), titlePatient);
					break;
				case listMedecins:
					loggerListObject(Hopital.getMedecins(), titleMedecin);
					break;
				case listAdmins:
					loggerListObject(Hopital.getAdmins(), titleAdmin);
					break;
				case listPatientsMedecin:
					System.out.println("Rentrer id du medecin pour ca liste de patient");
					id = sc.nextInt();
					affichagePatientsMedecin(id);
					break;
				case stop:
					System.exit(0);
					break;
				default:
					throw new ConsoleExeption(command+" n'est pas une commande reconnue");
				}
			} catch (ConsoleExeption e) {
				e.printStackTrace();
			} catch (InputMismatchException e) {
				System.out.println("Mauvaise saisie");
			}
		}
	}
	
	public static <E extends IPersonne> void loggerListObject(ArrayList<E> objects, String title) {
		/*
		 * Variables
		 */
		int lengthLastName;											
		int lengthFirstName;
		String medecinMaxLastName = objects.get(0).getLastName();
		String medecinMaxFirstName = objects.get(0).getFirstName();
		
		/*
		 * Recupere le nom et prenom de liste de medecin
		 * Et les attributs aux variables
		 */
		for (int i = 1; i < objects.size(); i++) {
			if(objects.get(i).getFirstName().length() >= medecinMaxFirstName.length()) {
				medecinMaxFirstName = objects.get(i).getFirstName();
			}
			if(objects.get(i).getLastName().length() >= medecinMaxLastName.length()) {
				medecinMaxLastName = objects.get(i).getLastName();
			}
		}
		lengthLastName = medecinMaxLastName.length();
		lengthFirstName = medecinMaxFirstName.length();
		int lengthBanner = lengthLastName+lengthFirstName+12;
		
		/*
		 * Affichage du Medecin Hopital
		 */
		System.out.print("+-");
		for (int i = 0; i < lengthBanner; i++) {
			System.out.print("-");
		}
		System.out.print("-+\n");
		System.out.print("| "+title);
		for (int i = 0; i < lengthBanner - title.length(); i++) {
			System.out.print(" ");
		}
		System.out.print(" |\n");
		System.out.print("+-");
		for (int i = 0; i < 6; i++) {
			System.out.print("-");
		}
		System.out.print("-+-");
		for (int i = 0; i < lengthFirstName; i++) {
			System.out.print("-");
		}
		System.out.print("-+-");
		for (int i = 0; i < lengthLastName; i++) {
			System.out.print("-");
		}
		System.out.print("-+\n");

		/*
		 *  Affiche de id, nom, prenom
		 */
		System.out.print("| id");
		for (int i = 0; i < 4 ; i++) {
			System.out.print(" ");
		}
		System.out.print(" | Prenom");
		for (int i = 0; i < lengthFirstName-6; i++) {
			System.out.print(" ");
		}
		System.out.print(" | Nom");
		for (int i = 0; i < lengthLastName-3 ; i++) {
			System.out.print(" ");
		}
		System.out.print(" |");
		System.out.print("\n");
		// banner
		System.out.print("+-");
		for (int i = 0; i < 6; i++) {
			System.out.print("-");
		}
		System.out.print("-+-");
		for (int i = 0; i < lengthFirstName; i++) {
			System.out.print("-");
		}
		System.out.print("-+-");
		for (int i = 0; i < lengthLastName; i++) {
			System.out.print("-");
		}
		System.out.print("-+\n");	
		
		/*
		 * Affichage des medecins en fonction de leur id, nom et prenom 
		 */
		for (E object : objects) {
			System.out.print("| "+object.getIdentifiant());
			for (int i = 0; i < 6-String.valueOf(object.getIdentifiant()).length(); i++) {
				System.out.print(" ");
			}
			System.out.print(" | "+object.getFirstName());
			if(lengthFirstName >= 6) {
				for (int i = 0; i < lengthFirstName-object.getFirstName().length(); i++) {
					System.out.print(" ");
				}
			}
			else {
				for (int i = 0; i < 6-lengthFirstName; i++) {
					System.out.print(" ");
				}
			}
			System.out.print(" | ");
			System.out.print(object.getLastName());
			if(lengthLastName >= 3) {
				for (int i = 0; i < lengthLastName - object.getLastName().length() ; i++) { System.out.print(" "); }
			}
			else {
				for (int i = 0; i < 3 - lengthLastName ; i++) { System.out.print(" "); }
			}
			System.out.print(" |\n");
		}
		System.out.print("+-");
		for (int i = 0; i < 6; i++) {
			System.out.print("-");
		}
		System.out.print("-+-");
		for (int i = 0; i < lengthFirstName; i++) {
			System.out.print("-");
		}
		System.out.print("-+-");
		for (int i = 0; i < lengthLastName; i++) {
			System.out.print("-");
		}
		System.out.print("-+\n");
	}
	
	/*
	 * Affiche les patients d'un medecin avec un id
	 */
	public static void affichagePatientsMedecin(int id) {
		int lengthName;
		for (Medecin medecin : Hopital.getMedecins()) {
			if(medecin.getIdentifiant() == id) {
				lengthName = medecin.getLastName().length();
				System.out.print("+");
				for (int i = 0; i < lengthName+12+18; i++) {
					System.out.print("-");
				}
				System.out.println("+");
//				System.out.println("+-----------------------------------+");
//				System.out.println("|Patients de "+ medecin.getLastName()+"|");
//				System.out.println("+-----------------------------------+");
				for (Patient patient : medecin.getPatients()) {
					System.out.println("\t"+patient.getIdentifiant()+" "+
							patient.getFirstName() + " "+ patient.getLastName());
				}
				break;
			}
		}
	}
	
	/*
	 * Affichage liste commande
	 */
	private static void commandeConsole() {
		System.out.println(listMedecins+" pour voir la liste des medecins de l'hopital");
		System.out.println(listAdmins+" pour voir la liste des admins de l'hopital");
		System.out.println(listPatients+" pour voir la liste des patients de l'hopital");
		System.out.println(listPatientsMedecin+" pour voir la liste des patients du medecins");
		System.out.println(stop+" pour arreter le programme");
	}
}
