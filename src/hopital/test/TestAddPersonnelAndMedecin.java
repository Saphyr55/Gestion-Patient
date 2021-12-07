/**
 * 
 */
package hopital.test;

import java.time.LocalDate;
import java.util.ArrayList;

import hopital.patient.Patient;
import hopital.personnels.Administrator;
import hopital.personnels.Medecin;

/**
 * @author Utilisateur
 *
 */
public class TestAddPersonnelAndMedecin {

	/**
	 * 
	 */
	public TestAddPersonnelAndMedecin() {
		
		ArrayList<String> medicaments = new ArrayList<>();
		medicaments.add("Doliprane x2");
		medicaments.add("Imodium");
		medicaments.add("Ventoline");
		
		Medecin andy = new Medecin("Andy", "Saincir","andy@gmail.com","1234");
		Medecin francois = new Medecin("Francois", "Hollande", "hoolande@gmail.com","1234");
		Medecin christophe = new Medecin("christophe", "Hollande", "hoolande@gmail.com","1234");
		Medecin medecin1 = new Medecin("prenom1", "nom1", "hoolande@gmail.com","1234");
		Medecin medecin2 = new Medecin("prenom2", "nom2", "hoolande@gmail.com","1234");
		Medecin medecin3 = new Medecin("prenom3", "nom3", "hoolande@gmail.com","1234");
		Medecin medecin4 = new Medecin("prenom4", "nom4", "hoolande@gmail.com","1234");
		Medecin medecin5 = new Medecin("prenom5", "nom5", "hoolande@gmail.com","1234");
		Medecin medecin6 = new Medecin("prenom6", "nom6", "hoolande@gmail.com","1234");

		Patient claude = new Patient("Claude", "Francois",LocalDate.of(22,9,2003));//"22/09/2003"));
		Patient claude1 = new Patient("prenom1", "nom1",LocalDate.of(2,8,1985));//"02/08/1985"));
		Patient claude2 = new Patient("prenom2", "nom2",LocalDate.of(23,10,1972));//"23/10/1972"));
		Patient claude3 = new Patient("prenom3", "nom3",LocalDate.of(31,12,1990));
		Patient claude4 = new Patient("prenom4", "nom4",LocalDate.of(13,01,1947));
		Patient claude5 = new Patient("prenom5", "nom5",LocalDate.of(05,11,1955));
		Patient claude6 = new Patient("prenom6", "nom6",LocalDate.of(30,03,2005));	
		
		Administrator admin = new Administrator("Andy", "Saincir", "andy@gmail.com", "1234");
		
		new Patient(claude.getIdentifiant(), andy, claude.getFirstName(), claude.getLastName(), claude.getBirthday(), true);
		new Patient(claude1.getIdentifiant(), andy, claude1.getFirstName(), claude1.getLastName(),claude1.getBirthday(),  true);
		new Patient(claude2.getIdentifiant(), andy, claude2.getFirstName(), claude2.getLastName(),claude2.getBirthday(),  true);
		new Patient(claude3.getIdentifiant(), andy, claude3.getFirstName(), claude3.getLastName(),claude3.getBirthday(),  true);
		new Patient(claude4.getIdentifiant(), andy, claude4.getFirstName(), claude4.getLastName(),claude4.getBirthday(),  true);
		new Patient(claude5.getIdentifiant(), andy, claude5.getFirstName(), claude5.getLastName(),claude5.getBirthday(),  true);
		new Patient(claude6.getIdentifiant(), andy, claude6.getFirstName(), claude6.getLastName(),claude6.getBirthday(),  true);
		
	}

}
