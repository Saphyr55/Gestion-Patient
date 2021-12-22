package hopital.patient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import hopital.Consultation;
import hopital.Hopital;
import hopital.personnels.IPersonne;
import hopital.personnels.Medecin;

/**
 * 
 * @author Andy
 *
 */

public class Patient implements IPersonne {

	public static enum PatientTypeCreate {
		LOADING_PATIENT_WITH_MEDECIN_IN_LIST, CREATE_PATIENT_WITH_MEDECIN
	}

	private String lastName;
	private String firstName;
	private LocalDate birthday;
	private String secuNumber;
	private String phoneNumber;
	private String address;
	private Medecin medecin;

	private File consultationDirectory;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	private ArrayList<Consultation> consultation = new ArrayList<>();
	private ArrayList<File> consultationFile = new ArrayList<>();
	private int nLine = 1;
	private int id;
	private String nameFolder;

	private BufferedReader reader;
	private OutputStreamWriter writer;

	/**
	 * Construteur de Patient
	 * Permet de créer un patient pour la premiere fois dans les fichiers
	 * et le charge dans la liste de patient
	 * 
	 * @param firstName
	 * @param lastName
	 * @param birthday
	 * @param secuNumber
	 * @param patientTypeCreate
	 */
	public Patient(String firstName, String lastName, LocalDate birthday, String secuNumber, String phoneNumber,
			String address) {

		/**
		 * Toute premiere creation du patient
		 * Dans une liste et en fichier
		 */
		try {
			reader = new BufferedReader(Hopital.getPatientsReaderFile());
			while ((reader.readLine()) != null) {
				nLine++;
			}
			this.id = nLine;
			this.lastName = lastName;
			this.firstName = firstName;
			this.birthday = birthday;
			this.secuNumber = secuNumber;
			this.address = address;
			this.phoneNumber = phoneNumber;
			this.consultation = new ArrayList<>();
			if (this.lastName == null) {
				this.lastName = "None";
			}
			if (this.firstName == null) {
				this.firstName = "None";
			}

			/*
			 * Creation du dossier du patient
			 */
			this.nameFolder = this.getFirstName().toLowerCase() + this.getLastName().toLowerCase();
			nameFolder.replace(" ", "");
			consultationDirectory = new File("./src/log/patient/" + nameFolder + "/");
			if (!consultationDirectory.exists()) {
				consultationDirectory.mkdir();
				System.out.println("Creation du dossier pour " + this.getLastName() + " " + this.getFirstName());
			}

			/*
			 * Ecriture dans le dossier patients de l'hopital
			 */
			writer = new OutputStreamWriter(Hopital.getPatientsWriterFile(), StandardCharsets.UTF_8);
			writer.write(id +
					Hopital.SEPARATOR + this.firstName + Hopital.SEPARATOR + this.lastName +
					Hopital.SEPARATOR + this.birthday.format(Hopital.FORMATEUR_LOCALDATE) + Hopital.SEPARATOR
					+ this.secuNumber +
					Hopital.SEPARATOR + this.phoneNumber + Hopital.SEPARATOR + this.address + "\n");
			reader.close();
			writer.close();
			Hopital.getPatients().add(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructeur Patient
	 * Charge le fichier des patients dans une liste
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param birthday
	 * @param secuNumber
	 * @param phoneNumber
	 * @param address
	 */
	public Patient(int id, String firstName, String lastName, LocalDate birthday, String secuNumber, String phoneNumber,
			String address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.secuNumber = secuNumber;
		this.address = address;
		this.phoneNumber = phoneNumber;
		Hopital.getPatients().add(this);
	}

	/**
	 * Construteur patient zero
	 * Patient pour debuging frame
	 */
	public Patient() {
		this.id = 0;
		this.firstName = "";
		this.lastName = "";
		this.birthday = LocalDate.now();
		this.secuNumber = "0000000000";
		this.phoneNumber = "00000000";
		this.address = "";
	}

	/**
	 * Construteur Patient qui permet de créer un patient en l'atribuant avec un
	 * medecin ou de chager le patient d'un medecin la liste de patient du medecin.
	 * Attention si le patient sans medecin n'a pas ete créer avant. Le patient ne
	 * doit pas alors etre créer
	 * 
	 * @param id
	 * @param medecin
	 * @param firstName
	 * @param lastName
	 * @param birthday
	 * @param secuNumber
	 * @param phoneNumber
	 * @param address
	 * @param patientTypeCreate
	 */
	public Patient(int id, Medecin medecin, String firstName, String lastName, LocalDate birthday, String secuNumber,
			String phoneNumber, String address, PatientTypeCreate patientTypeCreate) {
		if (patientTypeCreate == PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.birthday = birthday;
			this.secuNumber = secuNumber;
			this.address = address;
			this.phoneNumber = phoneNumber;
			this.medecin = medecin;

			/*
			 * Ecriture dans le fichier patients du medecin
			 */
			try {
				reader = new BufferedReader(new FileReader(new File("./src/log/medecin/"
						+ medecin.getFirstName().toLowerCase() + medecin.getLastName().toLowerCase()
						+ "/patients.txt")));
				while (reader.readLine() != null) {
					nLine++;
				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"./src/log/medecin/" + medecin.getFirstName().toLowerCase()
								+ medecin.getLastName().toLowerCase() + "/patients.txt",
						true));
				writer.write(nLine + Hopital.SEPARATOR + this.id + Hopital.SEPARATOR + getFirstName()
						+ Hopital.SEPARATOR + getLastName() + Hopital.SEPARATOR
						+ this.birthday.format(Hopital.FORMATEUR_LOCALDATE) + Hopital.SEPARATOR + this.secuNumber
						+ Hopital.SEPARATOR + this.phoneNumber + Hopital.SEPARATOR + this.address);
				writer.newLine();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			medecin.getPatients().add(this);
		}

		/**
		 * Chargement des fichier patient du medecin courant
		 */
		else if (patientTypeCreate == PatientTypeCreate.LOADING_PATIENT_WITH_MEDECIN_IN_LIST) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.birthday = birthday;
			this.secuNumber = secuNumber;
			this.address = address;
			this.phoneNumber = phoneNumber;
			this.medecin = medecin;
			medecin.getPatients().add(this);
		}
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the ordonnances
	 */
	public ArrayList<Consultation> getConsultations() {
		return consultation;
	}

	/**
	 * @param consultation
	 */
	public void setConsultations(ArrayList<Consultation> consultation) {
		this.consultation = consultation;
	}

	/**
	 * @return the dataPatient
	 */
	public File getConsultationDirectory() {
		return consultationDirectory;
	}

	/**
	 * @param dataPatient the dataPatient to set
	 */
	public void setConsultationDirectory(File consultationDirectory) {
		this.consultationDirectory = consultationDirectory;
	}

	/**
	 * @return the nameFolder
	 */
	public String getNameFolder() {
		return nameFolder;
	}

	/**
	 * @param nameFolder the nameFolder to set
	 */
	public void setNameFolder(String nameFolder) {
		this.nameFolder = nameFolder;
	}

	/**
	 * @return id
	 */
	public int getIdentifiant() {
		return id;
	}

	/**
	 * @return the birthday
	 */
	public LocalDate getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the ordonnancesFile
	 */
	public ArrayList<File> getConsultationsFile() {
		return consultationFile;
	}

	/**
	 * @param ordonnancesFile the ordonnancesFile to set
	 */
	public void setOrdonnancesFile(ArrayList<File> consultationFile) {
		this.consultationFile = consultationFile;
	}

	/**
	 * @return secuNumber
	 */
	public String getSecuNumber() {
		return secuNumber;
	}

	/**
	 * @param secuNumber
	 */
	public void setSecuNumber(String secuNumber) {
		this.secuNumber = secuNumber;
	}

	/**
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public Medecin getMedecin() {
		return medecin;
	}

	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}

}
