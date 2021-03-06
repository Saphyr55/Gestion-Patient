package hopital.personnels;

/**
 * Classe permettant de reduire le code des classes Medecin, Techinician et
 * Administrator
 * 
 * @author Andy
 */
public abstract class Personnel implements IPersonnel {

	// --------------------
	// Fields
	// --------------------
	private String lastName;
	private String firstName;
	private String email;
	private int identifiant;
	private String password;

	// --------------------
	// Constructeur
	// --------------------

	/**
	 * Construteur du personnel
	 * 
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	public Personnel(String firstName, String lastName, String email, String password) {
		if (firstName == null)
			firstName = "None";
		if (lastName == null)
			lastName = "None";
		if (email == null)
			email = "None";
		if (password == null)
			password = "";
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.password = password;
	}

	// --------------------
	// Getters and setters
	// --------------------

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(int identifiant) {
		this.identifiant = identifiant;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
