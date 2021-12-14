/**
 * 
 */
package windows.medecin;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import hopital.Hopital;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;

/**
 * @author Andy
 *
 */
public class FrameCreateConsultation extends JFrame {

	/**
	 *
	 */
	private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();
	private static LoadingDimens loadingDimens = FrameConnection.getLoadingDimens();

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

	private JPanel optionCanAddPanel;
	private String[] optionAddListStrings = { "Ordonnance", "Appariel Medical", "IRM", "Radiologie" };
	private JList<String> optionCanAddList;
	private JScrollPane optionCanAddScrollPane;
	private DefaultListModel<String> optionCanAddModel = new DefaultListModel<>();

	private JPanel optionAlreadyAddPanel;
	private JList<String> optionAlreadyAddList;
	private JScrollPane optionAlreadyAddScrollPane;
	private DefaultListModel<String> optionAlreadyAddModel = new DefaultListModel<>();

	/**
	 * 
	 */
	private JPanel panelCenter;

	private JPanel dataPatientPanel;
	private JLabel lastnamePatientLabel, firstnamePatientLabel;
	private JLabel birthdayPatientLabel, agePatientLabel, secuNumberPatientLabel;

	private JPanel avisMedicalPanel;
	private JLabel avisMedicalLabel;
	private JTextArea avisMedicaltextArea;
	private JScrollPane avisMedicalScrollPane;

	private JPanel prescriptionPanel;
	private JLabel prescriptionLabel;
	private JTextArea prescriptionTextArea;
	private JScrollPane prescriptionScrollPane;

	/**
	 * 
	 */
	private JPanel confirmConsultationPanel;

	private JLabel signLabel;
	private JCheckBox signCheckBox;
	private JButton confirmButton, cancelButton;

	/**
	 * 
	 */
	private Medecin currentMedecin = FrameConnection.getCurrentMedecin();
	private Patient currentPatient = FrameMedecin.getSelectedPatient();
	private LocalDate today = LocalDate.now();

	/**
	 * Constructeur de la fenetre de la creation d'ordonnance
	 */
	public FrameCreateConsultation() {
		super(title);
		setOptionWindow();
		contentPane.add(setOptionPanelWest(), BorderLayout.WEST);
		contentPane.add(setCenterPanel(), BorderLayout.CENTER);
		pack();
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

		optionCanAddPanel = new JPanel();
		optionCanAddPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		for (int i = 0; i < optionAddListStrings.length; i++) {
			optionCanAddModel.addElement(optionAddListStrings[i]);
		}

		optionCanAddList = new JList<>(optionCanAddModel);
		optionCanAddScrollPane = new JScrollPane(optionCanAddList);
		optionCanAddScrollPane.setPreferredSize(new Dimension(200, (height / 2) - 30));
		optionCanAddPanel.add(optionCanAddScrollPane);

		optionCanAddList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {

			}

		});

		optionAlreadyAddPanel = new JPanel();
		optionAlreadyAddList = new JList<>(optionAlreadyAddModel);
		optionAlreadyAddScrollPane = new JScrollPane(optionAlreadyAddList);
		optionAlreadyAddScrollPane.setPreferredSize(new Dimension(200, (height / 2) - 50));
		optionAlreadyAddPanel.add(optionAlreadyAddScrollPane);

		panelWest = new JPanel();
		panelWest.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelWest.add(optionCanAddPanel);
		panelWest.add(optionAlreadyAddPanel);
		panelWest.setLayout(new BoxLayout(panelWest, BoxLayout.Y_AXIS));

		return panelWest;
	}

	private JPanel setCenterPanel() {
		panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
		panelCenter.add(setDataPatientPanel());
		panelCenter.add(setAvisMedicalPanel());
		panelCenter.add(setPrescriptionPanel());
		panelCenter.add(setConfirmConsultationPanel());
		panelCenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		return panelCenter;
	}

	private JPanel setDataPatientPanel() {

		dataPatientPanel = new JPanel(new FlowLayout());
		lastnamePatientLabel = new JLabel(currentPatient.getLastName());
		firstnamePatientLabel = new JLabel(currentPatient.getFirstName());
		birthdayPatientLabel = new JLabel(currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE));
		agePatientLabel = new JLabel(String.valueOf(Period.between(currentPatient.getBirthday(), today).getYears()));
		secuNumberPatientLabel = new JLabel(
				currentPatient.getSecuNumber().replaceAll("(.{" + "3" + "})", "$1 ").trim());
		dataPatientPanel.add(lastnamePatientLabel);
		dataPatientPanel.add(Box.createHorizontalStrut(10));
		dataPatientPanel.add(firstnamePatientLabel);
		dataPatientPanel.add(Box.createHorizontalStrut(10));
		dataPatientPanel.add(birthdayPatientLabel);
		dataPatientPanel.add(Box.createHorizontalStrut(10));
		dataPatientPanel.add(agePatientLabel);
		dataPatientPanel.add(Box.createHorizontalStrut(10));
		dataPatientPanel.add(secuNumberPatientLabel);
		dataPatientPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 10));

		return dataPatientPanel;
	}

	private JPanel setAvisMedicalPanel() {

		avisMedicalPanel = new JPanel();
		avisMedicalLabel = new JLabel("Avis Médical");
		avisMedicalLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		avisMedicaltextArea = new JTextArea();
		avisMedicaltextArea.setLineWrap(true);
		avisMedicaltextArea.setWrapStyleWord(true);
		avisMedicalScrollPane = new JScrollPane(avisMedicaltextArea);
		avisMedicalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		avisMedicalScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		avisMedicalScrollPane.setPreferredSize(new Dimension((width / 2) - 100, 300));

		avisMedicalPanel.setLayout(new BoxLayout(avisMedicalPanel, BoxLayout.Y_AXIS));
		avisMedicalPanel.add(avisMedicalLabel);
		avisMedicalPanel.add(avisMedicalScrollPane);

		return avisMedicalPanel;
	}

	private JPanel setPrescriptionPanel() {
		prescriptionPanel = new JPanel();
		prescriptionLabel = new JLabel("Vos prescriptions");
		prescriptionLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		prescriptionTextArea = new JTextArea();
		prescriptionTextArea.setLineWrap(true);
		prescriptionTextArea.setWrapStyleWord(true);
		prescriptionScrollPane = new JScrollPane(prescriptionTextArea);
		prescriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		prescriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		prescriptionScrollPane.setPreferredSize(new Dimension((width / 2) - 100, 300));

		prescriptionPanel.setLayout(new BoxLayout(prescriptionPanel, BoxLayout.Y_AXIS));
		prescriptionPanel.add(prescriptionLabel);
		prescriptionPanel.add(prescriptionScrollPane);

		return prescriptionPanel;
	}

	private JPanel setConfirmConsultationPanel() {

		confirmConsultationPanel = new JPanel(new FlowLayout());
		signLabel = new JLabel("Signature");
		signCheckBox = new JCheckBox();
		confirmButton = new JButton("Ajouter");
		cancelButton = new JButton("Annuler");
		confirmButton.setEnabled(false);
		confirmConsultationPanel.add(signLabel);
		confirmConsultationPanel.add(signCheckBox);
		confirmConsultationPanel.add(Box.createHorizontalStrut(150));
		confirmConsultationPanel.add(confirmButton);
		confirmConsultationPanel.add(cancelButton);

		signCheckBox.addActionListener(new ActionListenerSignBox());

		return confirmConsultationPanel;
	}

	/**
	 * ActionListenerSignBox
	 */
	private class ActionListenerSignBox implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (signCheckBox.isSelected()) {
				confirmButton.setEnabled(true);
			} else if (!signCheckBox.isSelected()) {
				confirmButton.setEnabled(false);
			}
		}
	} 

}
