package hopital;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import hopital.patient.Patient;
import hopital.personnels.Administrator;
import hopital.personnels.Medecin;

public abstract class Hopital {
	
	/**
	 * ArrayList
	 */
	private static ArrayList<Medecin> medecins = new ArrayList<>(); 
	private static ArrayList<Administrator> admins = new ArrayList<>(); 
	private static ArrayList<Patient> patients = new ArrayList<>();
	
	/**
	 *	Paths
	 */
	public static final String pathOrdonnances = "./src/log/patient/ordonnances/";
	public static final String pathPatients = "./src/log/patient/patients.txt";
	public static final String pathMedecins = "./src/log/medecin/medecins.txt";
	public static final String pathAdmins = "./src/log/admin/admins.txt";
	public static final String pathFolderMedecin = "./src/log/medecin/";
	
	/**
	 * Les files Writers
	 */
	private static FileWriter patientsWriterFile;
	private static FileWriter medecinWriterFile;
	private static FileWriter adminsWriterFile;
	
	/**
	 * Les files readers
	 */
	private static FileReader patientsReaderFile;
	private static FileReader medecinReaderFile;
	private static FileReader adminsReaderFile;
	
	/**
	 * Les bufferedReader 
	 */
	private static BufferedReader readerMedecin;
	private static BufferedReader readerAdmins;
	private static BufferedReader readerPatients;
	private static BufferedReader readerMedecinPatients;
	
	/**
	 * Format
	 */
	public static final String FORMAT_DATE = "dd/MM/yyyy";
	public static final SimpleDateFormat FORMATEUR_DATE = new SimpleDateFormat(FORMAT_DATE);
	public static final DateTimeFormatter FORMATEUR_LOCALDATE = DateTimeFormatter.ofPattern(FORMAT_DATE);
	
	/**
	 * Permet de lire tous les fichier contenant les donn�es
	 * des medecins, des patients, des admins pour ensuite 
	 * les mettre dans une arraylist qui va facilit� le code
	 * dans les fenetres
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void loadingHopitalPersonnel() {;
		readerMedecin = new BufferedReader(getMedecinReaderFile());
		readerAdmins = new BufferedReader(getAdminsReaderFile());
		readerPatients = new BufferedReader(getPatientsReaderFile());
		try {
			/*
			 * Lis le fichier medecins puis ajoute les medecins du fichier
			 * dans une arryalist
			 */
			String line;
			String string;
			String[] strings;
			while((line = readerMedecin.readLine()) != null) {
				string = line;
				strings = string.split("�");
				new Medecin(Integer.parseInt(strings[1]),strings[2], strings[3], strings[4], strings[5]);
			}
			
			/*
			 * Lis le fichier admins puis ajoute les administrateur du fichier
			 * dans une arryalist
			 */
			line = null;
			string = null;
			strings = null;
			while((line = readerAdmins.readLine()) != null) {
				string = line;
				strings = string.split("�");
				new Administrator(Integer.parseInt(strings[1]),strings[2], strings[3], strings[4], strings[5]);
			}
			
			/*
			 * Lis le fichier patients puis ajoute les patients du fichier
			 * dans une arryalist
			 */
			line = null;
			string = null;
			strings = null;
			while((line = readerPatients.readLine()) != null) {
				string = line;
				strings = string.split("�");
				LocalDate date = LocalDate.parse(strings[3], FORMATEUR_LOCALDATE);				
				new Patient(Integer.parseInt(strings[0]), strings[1], strings[2], date);
			}
			
			/*
			 * Lis tous les fichiers "patients.txt" chez tous les medecins 
			 * Puis ajoute les patients dans les arrayliste respective des medecins
 			 */
			for (Medecin medecin : medecins) {
				readerMedecinPatients = new BufferedReader(
										new FileReader(pathFolderMedecin+medecin.getFirstName().
														toLowerCase()+medecin.getLastName().
														toLowerCase()+"/patients.txt"));
				line = null;
				string = null;
				strings = null;
				while((line = readerMedecinPatients.readLine()) != null) {
					string = line;
					strings = string.split("�");
					LocalDate date = LocalDate.parse(strings[4], FORMATEUR_LOCALDATE);
					new Patient(Integer.parseInt(strings[1]), medecin ,strings[2], strings[3], date);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				readerMedecin.close();
				readerAdmins.close();
				readerPatients.close();
				readerMedecinPatients.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Charge les ordonnances d'un patients,
	 * methode utiliser lors du clique sur uns
	 * patient pour afficher la liste d'ordonnances
	 * @param patient
	 */
	public static void loadingOrdonnancesPatient(Patient patient) {
		File[] ordonnancesPatientFile = new File(pathOrdonnances+
				patient.getFirstName()+patient.getLastName()+"/").listFiles();
		//Ordonnance ordonnancesPatients = new Ordonnance();
		System.out.println(ordonnancesPatientFile.length);
		for (int i = 0; i < ordonnancesPatientFile.length; i++) {
			if (!patient.getOrdonnancesFile().contains(ordonnancesPatientFile[i]))
				patient.getOrdonnancesFile().add(ordonnancesPatientFile[i]);
		}
	}
	
	/**
	 * @return the patientsReaderFile
	 */
	public static FileReader getPatientsReaderFile() {
		try {
			patientsReaderFile = new FileReader(pathPatients);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return patientsReaderFile;
	}

	/**
	 * @return patientsWriterFile
	 */
	public static Writer getPatientsWriterFile() {
		try {
			patientsWriterFile = new FileWriter(pathPatients, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return patientsWriterFile;
	}

	/**
	 * @return the medecinWriterFile
	 */
	public static FileWriter getMedecinWriterFile() {
		try {
			medecinWriterFile = new FileWriter(pathMedecins, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return medecinWriterFile;
	}

	/**
	 * @return the medecinReaderFile
	 */
	public static FileReader getMedecinReaderFile() {
		try {
			medecinReaderFile = new FileReader(pathMedecins);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return medecinReaderFile;
	}

	/**
	 * @return the adminsWriterFile
	 */
	public static FileWriter getAdminsWriterFile() {
		try {
			adminsWriterFile = new FileWriter(pathAdmins, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return adminsWriterFile;
	}

	/**
	 * @return the adminsReaderFile
	 */
	public static FileReader getAdminsReaderFile() {
		try {
			adminsReaderFile = new FileReader(pathAdmins);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return adminsReaderFile;
	}


	/**
	 * @return the medecins
	 */
	public static ArrayList<Medecin> getMedecins() {
		return medecins;
	}

	/**
	 * @return the admins
	 */
	public static ArrayList<Administrator> getAdmins() {
		return admins;
	}

	/**
	 * @return the patients
	 */
	public static ArrayList<Patient> getPatients() {
		return patients;
	}
}
