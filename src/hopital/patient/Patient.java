package hopital.patient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import hopital.Consultation;
import hopital.Hopital;
import hopital.IPersonne;
import hopital.personnels.Medecin;

/**
 * 
 * @author Andy
 *
 */
public class Patient implements IPersonne{
	
	private String lastName;
	private String firstName;
	private LocalDate birthday;
	
	private File ordonnanceDirectory;
	private ArrayList<Consultation> ordonnances = new ArrayList<>();
	private ArrayList<File> ordonnancesFile = new ArrayList<>();
	private int nLine = 1;
	private int id;
	private String nameFolder;
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	/**
	 * 
	 * @param lastName
	 * @param firstName
	 */
	public Patient(String firstName, String lastName, LocalDate birthday) {
		//dataPatient = new ArrayList<>();
		try {
			reader = new BufferedReader(Hopital.getPatientsReaderFile());
			String line;
			while((line = reader.readLine()) != null) {
				nLine++;
			}
			this.id = nLine; 
			this.lastName = lastName;
			this.firstName = firstName;
			this.birthday = birthday;
			ordonnances = new ArrayList<>();
			if(this.lastName == null) {
				this.lastName = "None";
			}
			if(this.firstName == null) {
				this.firstName = "None";
			}
			
			/*
			 * Creation du dossier du patient
			 */
			this.nameFolder = this.getFirstName().toLowerCase() + this.getLastName().toLowerCase()+"/";
			nameFolder.replace(" ", "");
			ordonnanceDirectory = new File(Hopital.pathOrdonnances+nameFolder);
			if(!ordonnanceDirectory.exists()) {
				ordonnanceDirectory.mkdir();
				System.out.println("Creation du dossier pour "+this.getLastName());
			}				

			/*
			 * Ecriture dans le dossier patients de l'hopital
			 */
			writer = new BufferedWriter(Hopital.getPatientsWriterFile());
			writer.write(id+"§"+getFirstName()+"§"+getLastName()+"§"+Hopital.FORMATEUR_DATE.format(getBirthday()));
			writer.newLine();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Patient(int id, String firstName , String lastName, LocalDate birthday) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		Hopital.getPatients().add(this);
	}
	
	public Patient() {
		this.id = 0;
		this.firstName = "";
		this.lastName = "";
		this.birthday = LocalDate.now();
	}
	
	public Patient(int id, Medecin medecin, String firstName , String lastName, LocalDate birthday, boolean run) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		
		/*
		 * Ecriture dans le fichier patients du medecin
		 */
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(medecin.getPatientsFile() , true));
			writer.write(nLine+"§"+this.id+"§"+getFirstName()+"§"+getLastName()+"§"+Hopital.FORMATEUR_DATE.format(getBirthday()));
			nLine++;
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Patient(id, medecin, firstName, lastName, birthday);
	}
	
	public Patient(int id, Medecin medecin, String firstName, String lastName, LocalDate birthday) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		medecin.getPatients().add(this);
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
	public ArrayList<Consultation> getOrdonnances() {
		return ordonnances;
	}

	/**
	 * @param ordonnances the ordonnances to set
	 */
	public void setOrdonnances(ArrayList<Consultation> ordonnances) {
		this.ordonnances = ordonnances;
	}

	/**
	 * @return the dataPatient
	 */
	public File getOrdonnanceDirectory() {
		return ordonnanceDirectory;
	}

	/**
	 * @param dataPatient the dataPatient to set
	 */
	public void setOrdonnanceDirectory(File ordonnanceDirectory) {
		this.ordonnanceDirectory = ordonnanceDirectory;
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
	public ArrayList<File> getOrdonnancesFile() {
		return ordonnancesFile;
	}

	/**
	 * @param ordonnancesFile the ordonnancesFile to set
	 */
	public void setOrdonnancesFile(ArrayList<File> ordonnancesFile) {
		this.ordonnancesFile = ordonnancesFile;
	}
	
}
