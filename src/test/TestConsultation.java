package test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hopital.Consultation;
import hopital.Hopital;
import hopital.patient.Patient;
import hopital.patient.Patient.PatientTypeCreate;
import hopital.personnels.Medecin;

public class TestConsultation {

    public static void main(String[] args) {

        ArrayList<String> appareillageList = new ArrayList<>();
        appareillageList.add("Attelle");

        String medicaments = "2 doliprane ";
        String avisMedical = "";
        Hopital.loadingMedecin();
        Hopital.loadingPatient();

        // LocalDate date = LocalDate.parse(date1.format(Hopital.FORMATEUR_LOCALDATE),
        // DateTimeFormatter.ISO_LOCAL_DATE);
        // System.out.println(date);

        Medecin medecin = Hopital.getMedecins().get(0);
        /*
        Patient patient = new Patient("firstName", "lastName",
                                    LocalDate.parse("11-05-1999", Hopital.FORMATEUR_LOCALDATE),
                                     "151555412214456", "0695953069", "address");

        Patient patientWithMedecin = new Patient(patient.getIdentifiant(), medecin, patient.getFirstName(),
                patient.getLastName(), patient.getBirthday(), patient.getSecuNumber(), patient.getPhoneNumber(),
                patient.getAddress(), PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
        */

        Patient patient2 = medecin.getPatients().get(8);

        Consultation consultation = new Consultation(medecin, patient2, medicaments, avisMedical,
                appareillageList,
                Consultation.WriteType.WRITE_IN_ORDONNANCE);

    }

}
