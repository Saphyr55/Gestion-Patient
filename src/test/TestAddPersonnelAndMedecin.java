/**
 * 
 */
package test;

import java.io.File;
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

	public static void main(String[] args) {
		File[] filesPatient = new File("./src/log/medecin/").listFiles();
		for (int i = 0; i < filesPatient.length - 1; i++) {
			System.out.println(filesPatient[i].getName());
		}
	}

}
