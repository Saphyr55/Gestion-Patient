package hopital.personnels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import hopital.Hopital;
import hopital.patient.Patient;

public class Medecin extends Personnel{
		
	private String pathFolder;
	private int nLine = 1;
	private ArrayList<Patient> patients = new ArrayList<>();
	private String nameFolder;
	private File medecinFolder;
	private File patientsFile;
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	public Medecin(String firstName, String lastName, String email, String password) {
		super(firstName, lastName, email, password);
		
		try {
			/*
			 * Met un identifiant par raport au nombre de ligne du fichier
			 */
			BufferedReader reader = new BufferedReader(Hopital.getMedecinReaderFile());
			@SuppressWarnings("unused")
			String line;
			while((line = reader.readLine()) != null) {
				nLine++;
			}
			reader.close();
			setIdentifiant(100000+nLine);
			
			/*
			 * Creation du dossier du medecin permetant de contenir un fichier "patients.txt" du medecin
			 */
			this.nameFolder = this.getFirstName().toLowerCase() + this.getLastName().toLowerCase();		
			this.pathFolder = Hopital.pathFolderMedecin+nameFolder+"/";
			nameFolder.replace(" ", "");
			medecinFolder = new File(pathFolder);		
			if(!medecinFolder.exists()) {
				medecinFolder.mkdir();
				System.out.println("Creation du dossier pour "+this.getLastName());
			}
			
			/*
			 * Creation du fichier texte contenant la liste des patients du medecin
			 */
			patientsFile = new File(pathFolder+"patients.txt"); 
			if(patientsFile.createNewFile())
				System.out.println("Dossier du fichier patient du medecin "+this.getLastName() +"creer");
			
			/*
			 * Ecriture du medecin le fichier "medecins.txt"
			 */
			BufferedWriter writer = new BufferedWriter(Hopital.getMedecinWriterFile());
			writer.write(nLine+"&"+getIdentifiant()+"&"+getFirstName()+"&"+getLastName()+"&"+getEmail()+"&"+getPassword());
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	public Medecin(int id, String firstName, String lastName, String email, String password) {
		super(firstName, lastName, email, password);
		super.setIdentifiant(id);
		Hopital.getMedecins().add(this);
	}
	
	/** 
	 * @return fichier patient du medecin
	 */
	public File getPatientsFile() {
		return patientsFile;
	}
	
	/**
	 * @return the patients
	 */
	public ArrayList<Patient> getPatients() {
		return patients;
	}

	/**
	 * @param patients the patients to set
	 */
	public void setPatients(ArrayList<Patient> patients) {
		this.patients = patients;
	}
	
}
