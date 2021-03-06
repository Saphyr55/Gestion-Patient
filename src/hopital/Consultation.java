package hopital;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import hopital.patient.Patient;
import hopital.personnels.Medecin;

/**
 * Class permetant de creer une ordonnance
 * 
 * @author Andy
 */
public class Consultation {

	public static enum WriteType {
		WRITE_IN_ORDONNANCE,
		NOT_WRITE_IN_ORDONNANCE
	}

	/**
	 * ---------------------------
	 * Attributs d'une ordonnance
	 * ---------------------------
	 */
	private Medecin medecin;
	private Patient patient;
	private String medicaments;
	private List<String> avisMedical;
	private Map<String, Boolean> appareillageMap;
	private List<String> diagnosticList;
	private File avisMedicalFile;
	private File avisMedicalFolder;
	private File consultation;
	private File ordonnanceFolder;
	private File ordonnance;
	private File appareillageFolder;
	private File appareillage;
	private File diagnostic;
	private File diagnosticFolder;
	private Date dateConsultation;
	private String nameConsultation;
	private JSONParser parserAppareillage;
	private JSONObject jsonObjectAppareillage;
	private static final String encoding = "UTF-8";
	private static final String format = ".txt";

	/**
	 * Consustruteur de la consultation
	 * Permet de gerer la gestion des fichiers de la consultation
	 * Le consutructeur a deux choix SOIT de creer la consultation avec l'ecriture
	 * dans le fichier du patient puis l'ajout de la consultation dans liste de
	 * consultation du patient OU seulement l'ajout de la consultation dans liste de
	 * consultation du patient
	 * 
	 * @param medecin
	 * @param patient
	 * @param medicaments
	 * @param avisMedical
	 * @param diagnosticList
	 * @param appareillageMap
	 * @param writeType
	 */
	public Consultation(Medecin medecin, Patient patient, String medicaments,
			List<String> avisMedical, List<String> diagnosticList,
			Map<String, Boolean> appareillageMap, WriteType writeType) {

		/**
		 * Verification : si le dossier du patient n'existe pas on le creer
		 */
		File folderPatient = new File("./src/log/patient/" +
				patient.getFirstName().toLowerCase() +
				patient.getLastName().toLowerCase() + "/");
		if (!folderPatient.exists()) // creation du dossier du patient
			folderPatient.mkdirs();

		this.patient = patient;
		this.medecin = medecin;
		this.medicaments = medicaments;
		this.appareillageMap = appareillageMap;
		this.avisMedical = avisMedical;
		this.diagnosticList = diagnosticList;

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

		/**
		 * Creation d'un dossier du patient et ajout
		 * des ordonnances du patient dans ce dossier
		 */
		if (writeType.equals(WriteType.WRITE_IN_ORDONNANCE)) {
			try {

				/**
				 * ---------------------------------------
				 * Creation du dossier de la consultation
				 * ---------------------------------------
				 */
				this.consultation = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
						+ patient.getLastName().toLowerCase() + "/" + this.nameConsultation + "/");
				if (!this.consultation.exists()) {
					this.consultation.mkdir();
					System.out.println("Consultation creer");
				} else
					throw new IOException("Dossier existant");

				/**
				 * -------------------------
				 * creation de l'ordonnance
				 * -------------------------
				 */
				if (this.medicaments != null) {
					try {
						this.ordonnanceFolder = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + nameConsultation + "/" + "ordonnance/");
						if (!ordonnanceFolder.exists()) {
							this.ordonnanceFolder.mkdir();
							System.out.println("Le dossier d'ordonnance creer");
						} else
							throw new IOException("Dossier existant");

						/**
						 * Creation de l'ordonnance
						 * puis la formate en fonction des paremetres
						 */
						this.ordonnance = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + this.nameConsultation + "/"
								+ "ordonnance/"
								+ this.nameConsultation + format);
						if (ordonnance.createNewFile()) {
							System.out.println("Ordonnance creer");
						} else
							throw new IOException("Creation de l'ordonnance a echou??");
						this.formatageOrdonnance(this.ordonnance, medecin, patient, medicaments);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				/**
				 * --------------------------------------
				 * Creation de la demande d'appariellage
				 * --------------------------------------
				 */
				if (this.appareillageMap != null) {
					try {
						/**
						 * Creation du dossier de demande apperiallage
						 */
						this.appareillageFolder = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + this.nameConsultation + "/"
								+ "appareillage/");
						if (!this.appareillageFolder.exists()) {
							this.appareillageFolder.mkdir();
							System.out.println("Le dossier appariellage creer");
						} else
							throw new IOException("Dossier existant");

						/**
						 * Creation du fichier
						 */
						this.appareillage = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + this.nameConsultation + "/"
								+ "appareillage/" + this.nameConsultation + ".json");
						if (appareillage.createNewFile()) {
							System.out.println("Appareillage creer");
						} else
							throw new IOException("Creation de l'appareillage a echou??");

						/**
						 * Formatage d'appariellage
						 */
						formatageAppariellage(this.appareillage, appareillageMap);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				/**
				 * ------------------------------
				 * - Creation de l'avis medical -
				 * ------------------------------
				 */
				if (this.avisMedical != null) {
					try {
						/**
						 * Creation du dossier de l'avis medical
						 */
						this.avisMedicalFolder = new File("./src/log/patient/"
								+ patient.getFirstName().toLowerCase() // nom patient
								+ patient.getLastName().toLowerCase() // prenom patient
								+ "/" + nameConsultation + "/" + "avismedical/"); // nom du dossier
						if (!avisMedicalFolder.exists()) {
							this.avisMedicalFolder.mkdir();
							System.out.println("Le dossier de l'avis medical creer");
						} else
							throw new IOException("Dossier existant");

						/**
						 * Creation du fichier de l'avis medical
						 */
						this.avisMedicalFile = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + this.nameConsultation + "/"
								+ "avismedical/" + nameConsultation + format);
						if (this.avisMedicalFile.createNewFile()) {
							System.out.println("Avis medical creer");
						} else
							throw new IOException("Creation de l'avis medical a echou??");

						/**
						 * Formattage de l'avis medicale
						 */
						formatageAvismedical(this.avisMedicalFile, this.avisMedical);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					throw new IOException("Une consultation doit avoir un avis medical");

				/**
				 * -----------------------------------
				 * Creation du fichier de dignostique
				 * -----------------------------------
				 */
				if (this.diagnosticList != null) {
					try {
						/**
						 * Creation du dossier de diagnostique
						 */
						this.diagnosticFolder = new File("./src/log/patient/"
								+ patient.getFirstName().toLowerCase() // nom patient
								+ patient.getLastName().toLowerCase() // prenom patient
								+ "/" + nameConsultation + "/" + "diagnostic/"); // nom du dossier
						if (!diagnosticFolder.exists()) {
							this.diagnosticFolder.mkdir();
							System.out.println("Le dossier de diagnostique creer");
						} else
							throw new IOException("Dossier existant");

						/**
						 * Creation du fichier de l'avis medical
						 */
						this.diagnostic = new File("./src/log/patient/" + patient.getFirstName().toLowerCase()
								+ patient.getLastName().toLowerCase() + "/" + this.nameConsultation + "/"
								+ "diagnostic/" + nameConsultation + format);
						if (this.diagnostic.createNewFile()) {
							System.out.println("Diagnostique creer");
						} else
							throw new IOException("Creation de fichier de diagnostique a echou??");

						/**
						 * Formattage de l'avis medicale
						 */
						formatageDiagnostic(this.diagnostic, this.diagnosticList);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				patient.getConsultations().add(this);
				patient.getConsultationsFile().add(this.consultation);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			patient.getConsultations().add(this);
			patient.getConsultationsFile().add(this.consultation);
		}
	}

	/**
	 * Formatage de fichier de l'appariellage
	 * 
	 * @param appareillage
	 */
	private void formatageAppariellage(File appareillageJSON, Map<String, Boolean> appareillages) {
		jsonObjectAppareillage = new JSONObject();
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(appareillageJSON, true),
				StandardCharsets.UTF_8)) {
			jsonObjectAppareillage.putAll(appareillages);
			writer.write(jsonObjectAppareillage.toJSONString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Formattage de l'avis medical
	 * 
	 * @param avisMedicalFile
	 * @param avisMedicaList
	 */
	private void formatageAvismedical(File avisMedicalFile, List<String> avisMedicaList) {
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(avisMedicalFile, true),
				StandardCharsets.UTF_8)) {
			for (int i = 0; i < avisMedicaList.size(); i++) {
				writer.write(avisMedicaList.get(i) + "\n");
			}
			writer.close();
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
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ordonnance, true),
					StandardCharsets.UTF_8);
			writer.write("Ordonnance du " + Hopital.FORMATEUR_DATE.format(this.getDateConsultation()));
			writer.write("\n" + "\n");
			writer.write("Nom du patient : " + patient.getLastName());
			writer.write("\n");
			writer.write("Pr??nom du patient: " + patient.getFirstName());
			writer.write("\n" + "\n");
			writer.write("Nom du medecin : " + medecin.getLastName());
			writer.write("\n");
			writer.write("Pr??nom du medecin : " + medecin.getFirstName());
			writer.write("\n" + "\n");
			writer.write("Medicaments prescrits : ");
			writer.write(medicaments);
			writer.write("\n" + "\n");
			writer.write("Signature : " + medecin.getLastName());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Formattage du ficher de diagnostique
	 * 
	 * @param diagnostic
	 * @param diagnosticListString
	 */
	private void formatageDiagnostic(File diagnostic, List<String> diagnosticListString) {
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(
				diagnostic, true),
				StandardCharsets.UTF_8)) {
			for (int i = 0; i < diagnosticListString.size(); i++) {
				writer.write(diagnosticListString.get(i) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------
	// Getters and setters
	// -----------------------

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

	public Map<String, Boolean> getAppareillageMap() {
		return appareillageMap;
	}

	public void setAppareillageMap(Map<String, Boolean> appareillageMap) {
		this.appareillageMap = appareillageMap;
	}

}
