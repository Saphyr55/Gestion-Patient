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

	private static final int width = 700;
	private static final int height = 720;
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
	private JPanel panelNamePatient, panelNameMedecin;
	private JPanel panelPrescription, panelAvisMedical;
	private JLabel lastNamePatient, firstNamePatient, lastNameMedecin, firstNameMedecin;
	private JLabel avisMedicalLabel, prescriptionLabel;
	private JTextArea avisMedicalTextArea, prescriptionTextArea;
	private JScrollPane avisMedicalScrollPane, prescriptionScrollPane;
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
		panelTop = new JPanel(new BorderLayout());
		panelTop.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
		panelNameMedecin = new JPanel();
		panelNamePatient = new JPanel();

		lastNameMedecin = new JLabel(frame_medecin_new_consultation_lastname_medecin+" : "+currentMedecin.getLastName());
		firstNameMedecin = new JLabel(frame_medecin_new_consultation_firstname_medecin+" : "+currentMedecin.getFirstName());

		lastNamePatient =new JLabel(frame_medecin_new_consultation_lastname_patient+" : "+ currentPatient.getLastName());
		firstNamePatient =new JLabel(frame_medecin_new_consultation_firstname_patient+" : "+currentPatient.getFirstName());

		lastNamePatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		firstNamePatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		lastNameMedecin.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		firstNameMedecin.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));

		panelNameMedecin.add(lastNameMedecin);
		panelNameMedecin.add(firstNameMedecin);

		panelNamePatient.add(lastNamePatient);
		panelNamePatient.add(firstNamePatient);

		panelNameMedecin.setLayout(new BoxLayout(panelNameMedecin, BoxLayout.Y_AXIS));
		panelNamePatient.setLayout(new BoxLayout(panelNamePatient, BoxLayout.Y_AXIS));

		panelTop.add(panelNameMedecin, BorderLayout.WEST);
		panelTop.add(panelNamePatient, BorderLayout.EAST);

		return panelTop;
	}

	private JPanel setConsultationCenter(){
		panelCenter = new JPanel(new GridLayout(2,1, 0,50));
		panelAvisMedical = new JPanel();
		panelPrescription = new JPanel();

		avisMedicalLabel = new JLabel("Avis Medical : ");
		avisMedicalTextArea = new JTextArea();
		avisMedicalScrollPane = new JScrollPane(avisMedicalTextArea);

		avisMedicalTextArea.setLineWrap(true);
		avisMedicalTextArea.setWrapStyleWord(false);
		avisMedicalTextArea.setFont(avisMedicalTextArea.getFont().deriveFont(25f));

		panelAvisMedical.add(avisMedicalLabel);
		panelAvisMedical.add(avisMedicalScrollPane);
		panelAvisMedical.setLayout(new BoxLayout(panelAvisMedical, BoxLayout.Y_AXIS));

		prescriptionLabel = new JLabel("Prescription : ");
		prescriptionTextArea = new JTextArea();
		prescriptionScrollPane = new JScrollPane(prescriptionTextArea);

		prescriptionTextArea.setLineWrap(true);
		prescriptionTextArea.setWrapStyleWord(false);
		prescriptionTextArea.setFont(prescriptionTextArea.getFont().deriveFont(25f));

		panelPrescription.add(prescriptionLabel);
		panelPrescription.add(prescriptionScrollPane);
		panelPrescription.setLayout(new BoxLayout(panelPrescription, BoxLayout.Y_AXIS));

		avisMedicalLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		prescriptionLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));

		panelCenter.add(panelAvisMedical);
		panelCenter.add(panelPrescription);

		return panelCenter;
	}

	private JPanel setConsultationBottom(){

		return panelBottom;
	}
}
