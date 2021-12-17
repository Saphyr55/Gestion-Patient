package hopital;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import hopital.loading.paths.LoadingPath;
import hopital.patient.Patient;
import hopital.personnels.Administrator;
import hopital.personnels.Medecin;

public abstract class Hopital {

	/**
	 * 
	 */
	private static LoadingPath loadingPath = new LoadingPath();

	/**
	 * ArrayList
	 */
	private static ArrayList<Medecin> medecins = new ArrayList<>();
	private static ArrayList<Administrator> admins = new ArrayList<>();
	private static ArrayList<Patient> patients = new ArrayList<>();
	private static ArrayList<Patient> patientsLogger = new ArrayList<>();
	private static ArrayList<Administrator> administratorsLooger = new ArrayList<>();
	private static ArrayList<Medecin> medecinsLogger = new ArrayList<>();

	/**
	 * Paths
	 */
	public static final String pathFolderLog = (String) loadingPath.getJsonObject().get("path_log");
	public static final String pathPatients = (String) loadingPath.getJsonObject().get("path_patients");
	public static final String pathMedecins = (String) loadingPath.getJsonObject().get("path_medecins");
	public static final String pathAdmins = (String) loadingPath.getJsonObject().get("path_admin");
	public static final String pathFolderMedecin = (String) loadingPath.getJsonObject().get("path_folder");
	public static final String pathRememberme = (String) loadingPath.getJsonObject().get("path_lastconnection");

	public static File fileRememberme = new File(pathRememberme);

	/**
	 * Les files Writers
	 */
	private static FileWriter patientsWriterFile;
	private static FileWriter medecinWriterFile;
	private static FileWriter adminsWriterFile;
	private static FileWriter remembermeFileWriter;

	/**
	 * Les files readers
	 */
	private static FileReader patientsReaderFile;
	private static FileReader medecinReaderFile;
	private static FileReader adminsReaderFile;
	private static FileReader remembermeFileReader;

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
	public static final String FORMAT_DATE = "dd-MM-yyyy";
	public static final SimpleDateFormat FORMATEUR_DATE = new SimpleDateFormat(FORMAT_DATE);
	public static final DateTimeFormatter FORMATEUR_LOCALDATE = DateTimeFormatter.ofPattern(FORMAT_DATE, Locale.US);
	public static final String SEPARATOR = "&";

	/**
	 * Crée le fichier remember me
	 */
	public static void createFileRememberme() {
		try {
			File fileRememberme = new File(pathRememberme);
			if (fileRememberme.createNewFile()) {
				Hopital.fileRememberme = fileRememberme;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Lis le fichier admins puis ajoute les administrateur du fichier
	 * dans une arryalist
	 */
	public static void loadingAdmin() {
		try {
			readerAdmins = new BufferedReader(getAdminsReaderFile());
			String line = null;
			String string = null;
			String[] strings = null;

			while ((line = readerAdmins.readLine()) != null) {
				string = line;
				strings = string.split(SEPARATOR);

				new Administrator(Integer.parseInt(strings[1]), strings[2], strings[3], strings[4], strings[5]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				readerAdmins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Lis le fichier medecins puis ajoute les medecins du fichier
	 * dans une liste
	 * Puis lis tous les fichiers "patients.txt" chez tous les medecins
	 * Puis ajoute les patients dans les arrayliste respective des medecins
	 */
	public static void loadingMedecin() {
		try {
			readerMedecin = new BufferedReader(getMedecinReaderFile());
			readerPatients = new BufferedReader(getPatientsReaderFile());
			String line;
			String string;
			String[] strings;

			while ((line = readerMedecin.readLine()) != null) {
				string = line;
				strings = string.split(SEPARATOR);

				new Medecin(Integer.parseInt(strings[1]), strings[2], strings[3], strings[4], strings[5]);
			}

			for (Medecin medecin : medecins) {
				readerMedecinPatients = new BufferedReader(
						new FileReader(pathFolderMedecin + medecin.getFirstName().toLowerCase()
								+ medecin.getLastName().toLowerCase() + "/patients.txt"));
				line = null;
				string = null;
				strings = null;
				while ((line = readerMedecinPatients.readLine()) != null) {
					string = line;
					strings = string.split(SEPARATOR);
					LocalDate date = LocalDate.parse(strings[4], FORMATEUR_LOCALDATE);
					new Patient(Integer.parseInt(strings[1]), medecin, strings[2], strings[3], date, strings[5],
							strings[6], strings[7],
							Patient.PatientTypeCreate.LOADING_PATIENT_WITH_MEDECIN_IN_LIST);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				readerMedecin.close();
				readerPatients.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Lis le fichier patients puis ajoute les patients du fichier
	 * dans une arryalist
	 */
	public static void loadingPatient() {
		try {
			readerPatients = new BufferedReader(getPatientsReaderFile());
			String line = null;
			String string = null;
			String[] strings = null;

			while ((line = readerPatients.readLine()) != null) {
				string = line;
				strings = string.split(SEPARATOR);

				LocalDate date = LocalDate.parse(strings[3], FORMATEUR_LOCALDATE);

				new Patient(Integer.parseInt(strings[0]), strings[1], strings[2], date, strings[4], strings[5],
						strings[6]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Charge les consultation d'un patients
	 * Cette methode est utilisé lors du clique sur d'un
	 * patient pour afficher sa liste de consultation
	 * 
	 * @param patient
	 */
	public static void loadingConsultationPatient(Patient patient) {
		File[] consultationPatientFile = new File(
				"./src/log/patient/" + patient.getFirstName() + patient.getLastName() + "/")
						.listFiles();
		if (consultationPatientFile != null) {
			for (int i = 0; i < consultationPatientFile.length; i++) {
				if (!patient.getConsultationFile().contains(consultationPatientFile[i]))
					patient.getConsultationFile().add(consultationPatientFile[i]);
			}
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

	/**
	 * @return remembermeFileWriter
	 */
	public static FileWriter getRemembermeFileWriter() {
		try {
			remembermeFileWriter = new FileWriter(pathRememberme, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return remembermeFileWriter;
	}

	/**
	 * @return remembermeFileReader
	 */
	public static FileReader getRemembermeReaderFile() {
		try {
			remembermeFileReader = new FileReader(pathRememberme);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return remembermeFileReader;
	}

}
