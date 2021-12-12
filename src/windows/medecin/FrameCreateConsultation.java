/**
 * 
 */
package windows.medecin;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import hopital.Hopital;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnexion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Andy
 *
 */
public class FrameCreateConsultation extends JFrame {

	/**
	 *
	 */
	private static LoadingLanguage loadingLanguage = FrameConnexion.getLoadingLanguage();
	private static LoadingDimens loadingDimens = FrameConnexion.getLoadingDimens();

	private final static int width = (int) ((long) loadingDimens.getJsonObject()
			.get("frame_medecin_create_consultation_width"));
	private final static int height = (int) ((long) loadingDimens.getJsonObject()
			.get("frame_medecin_create_consultation_height"));
	private static final String title = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_new_consultation_tile");
	private static boolean isVisible = true;

	/**
	 * Textes a charger
	 */
	private static final String frame_medecin_new_consultation_lastname_medecin = (String) loadingLanguage
			.getJsonObject().get("frame_medecin_new_consultation_lastname_medecin");
	private static final String frame_medecin_new_consultation_firstname_medecin = (String) loadingLanguage
			.getJsonObject().get("frame_medecin_new_consultation_firstname_medecin");
	private static final String frame_medecin_new_consultation_lastname_patient = (String) loadingLanguage
			.getJsonObject().get("frame_medecin_new_consultation_lastname_patient");
	private static final String frame_medecin_new_consultation_firstname_patient = (String) loadingLanguage
			.getJsonObject().get("frame_medecin_new_consultation_firstname_patient");

	/**
	 * 
	 */
	private JPanel contentPane = (JPanel) getContentPane();

	/**
	 * 
	 */
	private JPanel panelWest;

	/**
	 * 
	 */
	private JPanel panelCenter;

	private JPanel panelNamePatient;
	private JLabel labelLastnamePatient, labelFirstnamePatient;

	private JPanel panelAvisMedical;
	private JTextArea textAreaAvisMedical;

	private JPanel panelPrescrition;
	private JTextField textFieldPrescription;
	private JSpinner spinnerCountNumberPrescription;
	private SpinnerModel spinnerModelNumberPrescription;
	private JButton buttonAddPrescription;

	private JPanel panelListPrescribedMedication;
	private JLabel labelPrescribedMedication;
	private JList<String> listPrescribedMedication;

	/**
	 * 
	 */
	private JPanel panelSouth;

	private JPanel panelSign;
	private JLabel labelSign;
	private JCheckBox checkBoxSign;

	private JPanel panelConfirm;
	private JButton buttonConfirm, buttonCancel;

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
		contentPane.add(setOptionPanelWest(), BorderLayout.CENTER);
		setVisible(isVisible);
	}

	/**
	 *
	 */
	private void setOptionWindow() {

		try {
			UIManager.setLookAndFeel(new FlatIntelliJLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
		setSize(width, height);
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if (currentMedecin == null && currentPatient == null) {
			currentMedecin = Hopital.getMedecins().get(0);
			currentPatient = Hopital.getPatients().get(0);
		}
	}

	/**
	 * 
	 * @return panel du west
	 */
	private JPanel setOptionPanelWest() {

		panelWest = new JPanel();
		panelWest.add(setPanelNamePatient());

		return panelWest;
	}

	private JPanel setPanelNamePatient() {

		panelNamePatient = new JPanel();

		panelNamePatient.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		labelLastnamePatient = new JLabel(frame_medecin_new_consultation_lastname_patient);
		labelFirstnamePatient = new JLabel(frame_medecin_new_consultation_firstname_patient);

		panelNamePatient.add(labelLastnamePatient);
		panelNamePatient.add(labelFirstnamePatient);

		panelNamePatient.setLayout(new BoxLayout(panelNamePatient, BoxLayout.Y_AXIS));

		return panelNamePatient;
	}

}
