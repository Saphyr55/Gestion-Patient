/**
 * 
 */
package test;

import java.time.LocalDate;
import java.util.ArrayList;

import hopital.Hopital;
import hopital.patient.Patient;
import hopital.patient.Patient.PatientTypeCreate;
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
		/*Medecin francois = new Medecin("Francois", "Hollande", "hoolande@gmail.com","1234");
		Medecin christophe = new Medecin("christophe", "Hollande", "hoolande@gmail.com","1234");
		Medecin medecin1 = new Medecin("prenom1", "nom1", "hoolande@gmail.com","1234");
		Medecin medecin2 = new Medecin("prenom2", "nom2", "hoolande@gmail.com","1234");
		Medecin medecin3 = new Medecin("prenom3", "nom3", "hoolande@gmail.com","1234");
		Medecin medecin4 = new Medecin("prenom4", "nom4", "hoolande@gmail.com","1234");
		Medecin medecin5 = new Medecin("prenom5", "nom5", "hoolande@gmail.com","1234");
		Medecin medecin6 = new Medecin("prenom6", "nom6", "hoolande@gmail.com","1234");
		*/
		Patient claude = new Patient("Claude", "Francois",LocalDate.of(2003,9,22), "00000000000","0000000000" ,"daddada");
		Patient claude1 = new Patient("prenom1", "nom1",LocalDate.of(1934,8,2), "00000000000","0000000000" ,"dadadadadada");
		Patient claude2 = new Patient("prenom2", "nom2",LocalDate.of(1972,10,2), "00000000000","0000000000" ,"daadadadadadadadada");
		Patient claude3 = new Patient("prenom3", "nom3",LocalDate.of(1990,11,22), "000000000000","0000000000" ,"dadadadda");
		Patient claude4 = new Patient("prenom4", "nom4",LocalDate.of(1934,01,13), "000000000000","0000000000" ,"dada");
		Patient claude5 = new Patient("prenom5", "nom5",LocalDate.of(1955,11,23),"000000000000" ,"0000000000" ,"dadadadasdadsadsa");
		Patient claude6 = new Patient("prenom6", "nom6",LocalDate.of(2005,03,33), "000000000000","0000000000" ,"sdadsdadasdsdas");	
		
		/*Administrator admin = new Administrator("Andy", "Saincir", "andy@gmail.com", "1234");*/
		
		new Patient(claude.getIdentifiant(), Hopital.getMedecins().get(0), claude.getFirstName(), claude.getLastName(), claude.getBirthday(), "00000000000","0000000000" ,"daddada",PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
		new Patient(claude1.getIdentifiant(), Hopital.getMedecins().get(0), claude1.getFirstName(), claude1.getLastName(),claude1.getBirthday(), "00000000000","0000000000" ,"dadadadadada",PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
		new Patient(claude2.getIdentifiant(), Hopital.getMedecins().get(0), claude2.getFirstName(), claude2.getLastName(),claude2.getBirthday(), "00000000000","0000000000" ,"daadadadadadadadada",PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
		new Patient(claude3.getIdentifiant(), Hopital.getMedecins().get(0), claude3.getFirstName(), claude3.getLastName(),claude3.getBirthday(), "00000000000","0000000000" ,"dadadadda",PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
		new Patient(claude4.getIdentifiant(), Hopital.getMedecins().get(0), claude4.getFirstName(), claude4.getLastName(),claude4.getBirthday(), "00000000000","0000000000" ,"dada",PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
		new Patient(claude5.getIdentifiant(), Hopital.getMedecins().get(0), claude5.getFirstName(), claude5.getLastName(),claude5.getBirthday(), "00000000000","0000000000" ,"dadadadasdadsadsa",PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
		new Patient(claude6.getIdentifiant(), Hopital.getMedecins().get(0), claude6.getFirstName(), claude6.getLastName(),claude6.getBirthday(), "00000000000","0000000000" ,"sdadsdadasdsdas",PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
		
	}

}
