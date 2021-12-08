/**
 * 
 */
package windows.medecin;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;

import hopital.Hopital;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnexion;

import java.awt.*;

/**
 * @author Andy
 *
 */
public class FrameCreateConsultation extends JFrame{

	/**
	 *
	 */
	private static LoadingLanguage loadingLanguage = FrameConnexion.getLoadingLanguage();

	private static final int width = 600;
	private static final int height = 700;
	private static final String title = (String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_tile");
	private static boolean isVisible = true;

	/**
	 * Textes a charger
	 */
	private static final String frame_medecin_new_consultation_lastname_medecin = (String)
			loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_lastname_medecin");
	private static final String frame_medecin_new_consultation_firstname_medecin = (String)
			loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_firstname_medecin");
	private static final String frame_medecin_new_consultation_lastname_patient = (String)
			loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_lastname_patient");
	private static final String frame_medecin_new_consultation_firstname_patient = (String)
			loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_firstname_patient");
	/**
	 * 
	 */
	private JPanel contentPanel = (JPanel) getContentPane();
	private JPanel panelTop, panelCenter, panelBottom;
	private JLabel lastNamePatient, firstNamePatient, lastNameMedecin, firstNameMedecin;
	private JLabel avisMedecicalLabel, prescriptionLabel;
	private JTextArea avisMedecical, prescription;
	private JCheckBox signatureMedecin;

	/**
	 * 
	 */
	private Medecin currentMedecin = FrameConnexion.getCurrentMedecin();
	private Patient currentPatient = FrameMedecin.getSelectedPatient();
	
	/**
	 * Constructeur de la fenetre de la creation d'ordonnance 
	 */
	public FrameCreateConsultation() {
		super(title);
		setOptionWindow();
		contentPanel.add(consultationDataTop(), BorderLayout.NORTH);
		contentPanel.add(setConsultationCenter(), BorderLayout.CENTER);
		setVisible(isVisible);

	}

	/**
	 *
	 */
	private void setOptionWindow() {
		try {
			UIManager.setLookAndFeel( new FlatDarculaLaf() );
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}
		setSize(width, height);
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if(currentMedecin == null && currentPatient == null) {
			currentMedecin = Hopital.getMedecins().get(0);
			currentPatient = Hopital.getPatients().get(0);
		}
	}

	/**
	 *
	 * @return panelTop
	 */
	private JPanel consultationDataTop(){
		panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		JPanel panelPatientName = new JPanel(new GridLayout(2,1,10,10));
		JPanel panelMedecinName = new JPanel(new GridLayout(2,1,10,10));

		// patient nom et prenom
		firstNamePatient = new JLabel(frame_medecin_new_consultation_firstname_patient+" : " + currentPatient.getFirstName());
		lastNamePatient = new JLabel(frame_medecin_new_consultation_lastname_patient+" : "+ currentPatient.getLastName());

		// medecin nom et prenom
		firstNameMedecin = new JLabel(frame_medecin_new_consultation_firstname_medecin+" : "+currentMedecin.getFirstName());
		lastNameMedecin = new JLabel(frame_medecin_new_consultation_lastname_medecin+ " : "+currentMedecin.getLastName());

		/**
		 * Dimension et font size
		 */
		firstNamePatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 18));
		firstNameMedecin.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 18));
		lastNamePatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 18));
		lastNameMedecin.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 18));

		/**
		 *
		 */
		panelMedecinName.add(lastNameMedecin);
		panelMedecinName.add(firstNameMedecin);
		panelPatientName.add(lastNamePatient);
		panelPatientName.add(firstNamePatient);

		// Ajout des composant au panel top
		panelTop.add(panelPatientName);
		panelTop.add(panelMedecinName);

		return panelTop;
	}

	private JPanel setConsultationCenter(){
		panelCenter = new JPanel(new GridLayout(4,1));

		prescriptionLabel = new JLabel("Ajouter des prescriptions");
		avisMedecicalLabel = new JLabel("Votre avis médical");

		prescription = new JTextArea();
		avisMedecical =new JTextArea();

		panelCenter.add(prescriptionLabel);
		panelCenter.add(prescription);
		panelCenter.add(avisMedecicalLabel);
		panelCenter.add(avisMedecical);

		return panelCenter;
	}

}
