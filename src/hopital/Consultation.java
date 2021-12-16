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

import hopital.patient.Patient;
import hopital.personnels.Medecin;
/**
 * Class ordonnance
 * @author Andy
 *
 */

enum WriteType {
	WRITE_IN_ORDONNANCE,
	NOT_WRITE_IN_ORDONNANCE
}

public class Consultation {
	
	/**
	 * Attributs d'une ordonnance
	 */
	private Medecin medecin;
	private Patient patient;
	private ArrayList<String> medicaments;
	
	private File ordonnance;
	private Date dateOrdonnance;
	private String name;
	private static String format = ".txt";
	
	public static final String nameOrdonnanceDebugLoadWindow = "ordonnance0";
	
	/**
	 * Constructeur d'une ordonnance
	 * @param medecin
	 * @param patient
	 */
	public Consultation(Medecin medecin, Patient patient, ArrayList<String> medicaments, WriteType writeType) {
		
		this.patient = patient;
		this.medecin = medecin;    
		this.setMedicaments(medicaments);
		
		/**
		 *  Formatage de la date et les noms
		 *  du patient et du medecin pour le nom de l'ordonnance
		 */
		String nameMedecin = medecin.getLastName().replace(" ", "");
		String namePatient = patient.getLastName().replace(" ", "");		
		dateOrdonnance = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");        
        name = formatter.format(dateOrdonnance)+"&"+nameMedecin+"&"+namePatient;
		name = name.replace(" ", "");
		
		/**
		 *  Creation d'un dossier du patient et ajout 
		 *  des ordonnances du patient dans ce dossier 
		 */
		if(writeType.equals(WriteType.WRITE_IN_ORDONNANCE)) {
			try {
				/*
				 * Creation de l'ordonnance
				 */
				ordonnance = new File(Hopital.pathOrdonnances+"/"+patient.getFirstName().toLowerCase()+
										patient.getLastName().toLowerCase()+"/"+name+format);
				if(ordonnance.createNewFile()) {
					System.out.println("Ordonnance créée");
				} else throw new IOException("Fichier existant"); 
				
				/*
				 *  formatage de l'ordonnance en fonction des parametre
				 */
				this.formatageOrdonnance(ordonnance, medecin, patient, medicaments);
				patient.getOrdonnances().add(this);
				patient.getOrdonnancesFile().add(ordonnance);
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		else {
			patient.getOrdonnances().add(this);
			patient.getOrdonnancesFile().add(ordonnance);
		}
	}
	
	public Consultation() {
		this.name = nameOrdonnanceDebugLoadWindow;
	}
	
    /**
     * Permet de formater une ordonnance en fonction des paramatre donner
     * @param ordonnance
     * @param medecin
     * @param patient
     * @param medicaments
     */
	private void formatageOrdonnance(File ordonnance, Medecin medecin, Patient patient, ArrayList<String> medicaments) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ordonnance, true));
			writer.write("Ordonnance du "+Hopital.FORMATEUR_DATE.format(this.getDateOrdonnance()));
			writer.newLine();
			writer.newLine();
			writer.write("Nom du patient : "+patient.getLastName());
			writer.newLine();
			writer.write("Prenom du patient: "+patient.getFirstName());
			writer.newLine();
			writer.newLine();
			writer.write("Nom du medecin : "+medecin.getFirstName());
			writer.newLine();
			writer.write("Prenom du medecin : "+medecin.getFirstName());
			writer.newLine();
			writer.newLine();
			writer.write("Medicaments prescrits : ");
			for (String string : medicaments) {
				writer.write(string+", ");				
			}
			writer.newLine();
			writer.newLine();
			writer.write("Signature : "+medecin.getLastName());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the ordonnance
	 */
	public File getOrdonnance() {
		return ordonnance;
	}

	/**
	 * @param ordonnance the ordonnance to set
	 */
	public void setOrdonnance(File ordonnance) {
		this.ordonnance = ordonnance;
	}

	/**
	 * @return the medecin
	 */
	public Medecin getMedecin() {
		return medecin;
	}

	/**
	 * @param medecin the medecin to set
	 */
	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}

	/**
	 * @return the dateOrdonnance
	 */
	public Date getDateOrdonnance() {
		return dateOrdonnance;
	}

	/**
	 * @param dateOrdonnance the dateOrdonnance to set
	 */
	public void setDateOrdonnance(Date dateOrdonnance) {
		this.dateOrdonnance = dateOrdonnance;
	}

	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the medicaments
	 */
	public ArrayList<String> getMedicaments() {
		return medicaments;
	}

	/**
	 * @param medicaments the medicaments to set
	 */
	public void setMedicaments(ArrayList<String> medicaments) {
		this.medicaments = medicaments;
	}
}
