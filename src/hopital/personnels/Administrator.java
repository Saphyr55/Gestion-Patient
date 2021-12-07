package hopital.personnels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import hopital.Hopital;
import hopital.patient.Patient;

public class Administrator extends Personnel {
		
	private int nLine = 1;
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	public Administrator(String firstName, String lastName, String email, String password) {
		super(firstName, lastName, email, password);
		try {
			BufferedReader reader = new BufferedReader(Hopital.getAdminsReaderFile());
			String line;
			while((line = reader.readLine()) != null) {
				nLine++;
			}
			reader.close();
			setIdentifiant(200000+nLine);
			BufferedWriter writer = new BufferedWriter(Hopital.getAdminsWriterFile());
			writer.write(nLine+"§"+getIdentifiant()+"§"+getFirstName()+"§"+getLastName()+"§"+getEmail()+"§"+getPassword());
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Administrator(int id,String firstName, String lastName, String email, String password) {
		super(firstName, lastName, email, password);
		super.setIdentifiant(id);
		Hopital.getAdmins().add(this);
	}
}
