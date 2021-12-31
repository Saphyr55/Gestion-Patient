package hopital.personnels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import hopital.Hopital;

/**
 * Classe permettant de creer un admin
 * 
 * @author Andy
 */
public class Administrator extends Personnel {

	private int nLine = 1;

	/**
	 * Constructeur d'un admin, puis l'ecris dans le fichier des admins
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
			while ((reader.readLine()) != null) {
				nLine++;
			}
			reader.close();
			setIdentifiant(200000 + nLine);
			OutputStreamWriter writer = new OutputStreamWriter(Hopital.getAdminsWriterFile(), StandardCharsets.UTF_8);
			writer.write(nLine + "&" + getIdentifiant() + "&" + getFirstName() + "&" + getLastName() + "&" + getEmail()
					+ "&" + getPassword());
			writer.write("\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'ajouter un admin dans la liste de d'admin et de l'hopital
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	public Administrator(int id, String firstName, String lastName, String email, String password) {
		super(firstName, lastName, email, password);
		super.setIdentifiant(id);
		Hopital.getAdmins().add(this);
	}
}
