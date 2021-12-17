package hopital;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hopital.patient.Patient;
import hopital.personnels.Medecin;

/**
 * Class ordonnance
 * 
 * @author Andy
 *
 */

public class Consultation {

	public static enum WriteType {
		WRITE_IN_ORDONNANCE,
		NOT_WRITE_IN_ORDONNANCE
	}

	/**
	 * Attributs d'une ordonnance
	 */
	private Medecin medecin;
	private Patient patient;
	private String medicaments;
	private String avisMedical;
	private ArrayList<String> appareillageList;
	private File consultation;
	private File ordonnanceFolder;
	private File ordonnance;
	private File appareillageFolder;
	private File appareillage;
	private Date dateConsultation;
	private String nameConsultation;
	private String pathConsultation;
	private static String format = ".txt";

	public static final String nameOrdonnanceDebugLoadWindow = "ordonnance0";

	/**
	 * Constructeur d'une ordonnance
	 * 
	 * @param medecin
	 * @param patient
	 */
	public Consultation(Medecin medecin, Patient patient, String medicaments, String avisMedical,
			ArrayList<String> appareillageList, WriteType writeType) {

		this.patient = patient;
		this.medecin = medecin;
		this.medicaments = medicaments;
		this.appareillageList = appareillageList;
		this.avisMedical = avisMedical;

		/**
		 * Formatage de la date et les noms
		 * du patient et du medecin pour le nom de l'ordonnance
		 */
		String nameMedecin = medecin.getLastName().replace(" ", "");
		String namePatient = patient.getLastName().replace(" ", "");
		dateConsultation = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		nameConsultation = formatter.format(dateConsultation) + "&" + nameMedecin + "&" + namePatient;
		nameConsultation = nameConsultation.replace(" ", "");
		pathConsultation = "./scr/log/patient/" + patient.getFirstName().toLowerCase() +
				patient.getLastName().toLowerCase() + "/" + nameConsultation + "/";

		/**
		 * Creation d'un dossier du patient et ajout
		 * des ordonnances du patient dans ce dossier
		 */
		if (writeType.equals(WriteType.WRITE_IN_ORDONNANCE)) {
			try {

				/*
				 * Creation du dossier de la consultation
				 */
				this.consultation = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
						+ patient.getLastName().toLowerCase() + "/" + nameConsultation + "/");
				if (!consultation.exists()) {
					consultation.mkdir();
					System.out.println("Consultation creer");
				} else
					throw new IOException("Dossier existant");

				/**
				 * creation du dossier ordonnance et et de l'ordonnance
				 */
				if (medicaments != null) {
					try {
						this.ordonnanceFolder = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + nameConsultation + "/" + "ordonnances/");
						if (!ordonnanceFolder.exists()) {
							ordonnanceFolder.mkdir();
							System.out.println("Le dossier d'ordonnance creer");
						} else
							throw new IOException("Dossier existant");

						/**
						 * Creation de l'ordonnance
						 * puis la formate en fonction des paremetres
						 */
						this.ordonnance = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + nameConsultation + "/" + "ordonnances/"
								+ nameConsultation + format);
						if (ordonnance.createNewFile()) {
							System.out.println("Ordonnance creer");
						} else
							throw new IOException("Creation de l'ordonnance a echoué");
						this.formatageOrdonnance(this.ordonnance, medecin, patient, medicaments);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (this.appareillageList != null || !this.appareillageList.isEmpty()) {
					try {
						/**
						 * Creation du dossier de demande apperiallage
						 */
						this.appareillageFolder = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + nameConsultation + "/"
								+ "appareillages/");
						if (!appareillageFolder.exists()) {
							appareillageFolder.mkdir();
							System.out.println("Le dossier appariellage creer");
						} else
							throw new IOException("Dossier existant");

						/**
						 * Creation du fichier
						 */
						this.appareillage = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + nameConsultation + "/"
								+ "appareillages/" + nameConsultation + format);
						if (appareillage.createNewFile()) {
							System.out.println("Appareillage creer");
						} else
							throw new IOException("Creation de l'appareillage a echoué");

						/**
						 * Formatage d'appariellage
						 */
						formatageAppariellage(this.appareillage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				patient.getConsultation().add(this);
				patient.getConsultationFile().add(consultation);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			patient.getConsultation().add(this);
			patient.getConsultationFile().add(consultation);
		}
	}

	/**
	 * 
	 */
	public Consultation() {
		this.nameConsultation = nameOrdonnanceDebugLoadWindow;
	}

	/**
	 * Formatage de fichier de l'appariellage
	 */
	private void formatageAppariellage(File appareillage) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(appareillage, true))) {
			for (int i = 0; i < appareillageList.size(); i++) {
				writer.write(appareillageList.get(i));
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de formater une ordonnance en fonction des paramatre donner
	 * 
	 * @param ordonnance
	 * @param medecin
	 * @param patient
	 * @param medicaments
	 */
	private void formatageOrdonnance(File ordonnance, Medecin medecin, Patient patient, String medicaments) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ordonnance, true));
			writer.write("Ordonnance du " + Hopital.FORMATEUR_DATE.format(this.getDateConsultation()));
			writer.newLine();
			writer.newLine();
			writer.write("Nom du patient : " + patient.getLastName());
			writer.newLine();
			writer.write("Prenom du patient: " + patient.getFirstName());
			writer.newLine();
			writer.newLine();
			writer.write("Nom du medecin : " + medecin.getFirstName());
			writer.newLine();
			writer.write("Prenom du medecin : " + medecin.getFirstName());
			writer.newLine();
			writer.newLine();
			writer.write("Medicaments prescrits : ");
			writer.write(medicaments);
			writer.newLine();
			writer.newLine();
			writer.write("Signature : " + medecin.getLastName());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the ordonnance
	 */
	public File getConsultation() {
		return consultation;
	}

	/**
	 * @param consultation
	 */
	public void setConsultation(File consultation) {
		this.consultation = consultation;
	}

	/**
	 * @return medecin
	 */
	public Medecin getMedecin() {
		return medecin;
	}

	/**
	 * @param medecin
	 */
	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}

	/**
	 * @return dateOrdonnance
	 */
	public Date getDateConsultation() {
		return dateConsultation;
	}

	/**
	 * @param dateOrdonnance
	 */
	public void setDateConsultation(Date dateConsultation) {
		this.dateConsultation = dateConsultation;
	}

	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * @param patient
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * @return nameConsultation
	 */
	public String getName() {
		return nameConsultation;
	}

	/**
	 * @param nameConsultation
	 */
	public void setName(String name) {
		this.nameConsultation = name;
	}

	/**
	 * @return the medicaments
	 */
	public String getMedicaments() {
		return medicaments;
	}

	/**
	 * @param medicaments
	 */
	public void setMedicaments(String medicaments) {
		this.medicaments = medicaments;
	}
}
