package windows.medecin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.simple.JSONArray;

import hopital.Hopital;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnection;

/**
 * Classe qui creer un fenetre pour le medecin qui lui permet de creer une
 * consultation au patient avec une prescription et un avis medecical
 * 
 * @author Andy
 */
public class FrameConsultation extends JFrame {

	// --------------------
	// Fields
	// --------------------

	/**
	 * Langue, dimensions de la fenetre et titre
	 */
	private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();
	private static LoadingDimens loadingDimens = FrameConnection.getLoadingDimens();
	public final static int width = (int) ((long) loadingDimens.getJsonObject()
			.get("frame_medecin_create_consultation_width"));
	public final static int height = (int) ((long) loadingDimens.getJsonObject()
			.get("frame_medecin_create_consultation_height"));
	public static final String title = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_new_consultation_tile");
	private static boolean isVisible = true;

	/**
	 * panel principal
	 */
	private JPanel contentPane = (JPanel) this.getContentPane();

	/**
	 * Panel west
	 */
	private JPanel panelWest;
	private JPanel optionCanAddPanel;
	public final static String[] optionAddListStrings = {
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_prescription"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_medical_device"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_magnetic_resonance_imaging"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_radiology"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_surgery"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_diagnostics") };
	private JList<String> panelSwitchList;
	private JScrollPane optionCanAddScrollPane;
	private DefaultListModel<String> optionCanAddModel = new DefaultListModel<>();

	/**
	 * panel central
	 */
	private JPanel panelCenter, switchPanel;

	/**
	 * panel des donn??es du patient
	 */
	private JPanel dataPatientPanel;
	private JLabel lastnamePatientLabel, firstnamePatientLabel;
	private JLabel birthdayPatientLabel, agePatientLabel, secuNumberPatientLabel;

	/**
	 * panel de l'avis medical
	 */
	private JPanel avisMedicalPanel;
	private JLabel avisMedicalLabel;
	private JTextArea avisMedicaltextArea;
	private JScrollPane avisMedicalScrollPane;

	/**
	 * panel de l'ordonnance
	 */
	private JPanel prescriptionPanel;
	private JLabel prescriptionLabel;
	private JTextArea prescriptionTextArea;
	private JScrollPane prescriptionScrollPane;

	/**
	 * panel du diagnostique
	 */
	private JPanel diagnosticPanel;
	private JLabel diagnosticLabel;
	private JTextArea diagnosticTextArea;
	private JScrollPane diagnosticScrollPane;

	/**
	 * Panel d'appariellage
	 */
	private JPanel appariellagePanel;
	private JPanel appariellageAlreadyAddPanel;
	private JLabel apparielageRequestLabel;
	private JList<String> appariellageAlreadyAddList;
	private DefaultListModel<String> appariellageAlreadyAddModel = new DefaultListModel<>();
	private JScrollPane appariellageAlreadyAddScrollPane;
	private ArrayList<String> appariellageAlreadyAddArrayList = new ArrayList<>();
	private JPanel appariellageToAddPanel;
	private JPanel appariellageToAddInputPanel;
	private JTextField apparielFiltreTextField;
	private JButton appariellageToAddButton;
	private JList<String> appariellageToAddList;
	private DefaultListModel<String> appariellageToAddModel = new DefaultListModel<>();
	private String[] appariellageToAddTab;
	private JScrollPane appariellageToAddScrollPane;
	private JSONArray appariellagesJsonArray = (JSONArray) loadingLanguage.getJsonObject().get("appariel_medical");
	private Iterator<?> appariellages = appariellagesJsonArray.iterator();

	/**
	 * panel de confirmation
	 */
	private JPanel confirmConsultationPanel;
	private JLabel signLabel;
	private JCheckBox signCheckBox;
	private JButton confirmButton, cancelButton;

	/**
	 * 
	 */
	private FrameMedecin frameMedecin = FrameConnection.getFrameMedecin();
	private FrameMedecin.ConfirmButtonListener confirmButtonListener = frameMedecin.new ConfirmButtonListener();
	private Medecin currentMedecin = FrameConnection.getCurrentMedecin();
	private Patient currentPatient = FrameMedecin.getSelectedPatient();
	private LocalDate today = LocalDate.now();
	private String value;

	/**
	 * Constructeur de la fenetre de la creation d'ordonnance
	 * 
	 */
	public FrameConsultation() {
		super(title);
		setOptionWindow();
		contentPane.add(setOptionPanelWest(), BorderLayout.WEST);
		contentPane.add(setCenterPanel(), BorderLayout.CENTER);
		pack();
		setVisible(isVisible);
	}

	/**
	 * Option generale de la fenetre
	 */
	private void setOptionWindow() {
		try {
			UIManager.setLookAndFeel(FrameConnection.getModel());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
		setSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if (currentMedecin == null && currentPatient == null) {
			currentMedecin = Hopital.getMedecins().get(0);
			currentPatient = Hopital.getPatients().get(0);
		}
	}

	/**
	 * Creer le panel ouest, contient la liste qui vont servir
	 * de passer d'un panel a un autre panel
	 * 
	 * @return panel west
	 */
	private JPanel setOptionPanelWest() {

		optionCanAddPanel = new JPanel();
		optionCanAddPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		for (int i = 0; i < optionAddListStrings.length; i++) {
			optionCanAddModel.addElement(optionAddListStrings[i]);
		}

		panelSwitchList = new JList<>(optionCanAddModel);
		optionCanAddScrollPane = new JScrollPane(panelSwitchList);
		optionCanAddScrollPane.setPreferredSize(new Dimension(200, height));
		optionCanAddPanel.add(optionCanAddScrollPane);

		panelWest = new JPanel();
		panelWest.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelWest.add(optionCanAddPanel);
		panelWest.setLayout(new BoxLayout(panelWest, BoxLayout.Y_AXIS));

		panelSwitchList.addListSelectionListener(new panelSwitchListSelectionListener());

		return panelWest;
	}

	/**
	 * Creer le panel central
	 * Contient le switch panel et le panel de l'avis medical
	 * 
	 * @return panelCenter
	 */
	private JPanel setCenterPanel() {
		panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
		panelCenter.add(setDataPatientPanel());
		panelCenter.add(setAvisMedicalPanel());
		panelCenter.add(setSwitchPanel());
		panelCenter.add(setConfirmConsultationPanel());
		panelCenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		return panelCenter;
	}

	/**
	 * Creer le panel de switch
	 * Ce panel contient plusieurs panel qui sur eux meme
	 * Selectionne un panel lors du clique de la liste de switch panel
	 * 
	 * @return switchPanel
	 */
	private JPanel setSwitchPanel() {
		switchPanel = new JPanel(new CardLayout());
		switchPanel.add(setPrescriptionPanel());
		switchPanel.add(setAppariellagePanel());
		switchPanel.add(setDiagnosticPanel());
		return switchPanel;
	}

	/**
	 * Affiche les donn??es du patient courant
	 * Affiche nom, prenom, age, date de naissance
	 * et le numero de securit?? social
	 * 
	 * @return dataPatientPanel
	 */
	private JPanel setDataPatientPanel() {

		dataPatientPanel = new JPanel(new FlowLayout());
		lastnamePatientLabel = new JLabel(currentPatient.getLastName());
		lastnamePatientLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		firstnamePatientLabel = new JLabel(currentPatient.getFirstName());
		firstnamePatientLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		birthdayPatientLabel = new JLabel(currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE));
		birthdayPatientLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		agePatientLabel = new JLabel(String.valueOf(Period.between(currentPatient.getBirthday(), today).getYears()));
		agePatientLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		secuNumberPatientLabel = new JLabel(
				currentPatient.getSecuNumber().replaceAll("(.{" + "3" + "})", "$1 ").trim());
		secuNumberPatientLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
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

	/**
	 * Creer le panel de avis medical
	 * Permet au medecin d'??crire son avis medical
	 * 
	 * @return avisMedicalPanel
	 */
	private JPanel setAvisMedicalPanel() {

		avisMedicalPanel = new JPanel();
		avisMedicalLabel = new JLabel((String) loadingLanguage
				.getJsonObject()
				.get("frame_medecin_new_consultation_medical_advice"));
		avisMedicalLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		avisMedicaltextArea = new JTextArea();
		avisMedicaltextArea.setLineWrap(true);
		avisMedicaltextArea.setWrapStyleWord(true);
		avisMedicalScrollPane = new JScrollPane(avisMedicaltextArea);
		avisMedicalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		avisMedicalScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		avisMedicalPanel.setLayout(new BoxLayout(avisMedicalPanel, BoxLayout.Y_AXIS));
		avisMedicalPanel.add(avisMedicalLabel);
		avisMedicalPanel.add(avisMedicalScrollPane);
		avisMedicalPanel.setPreferredSize(new Dimension(0, 100));

		return avisMedicalPanel;
	}

	/**
	 * Creer le panel de diagnostique
	 * Qui permet au medecin d'??crire son diagnostique
	 * au niveau de la consultation
	 * 
	 * @return diagnosticPanel
	 */
	private JPanel setDiagnosticPanel() {
		diagnosticPanel = new JPanel();
		diagnosticLabel = new JLabel(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_diagnostics"));
		diagnosticLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		diagnosticTextArea = new JTextArea();
		diagnosticTextArea.setLineWrap(true);
		diagnosticTextArea.setWrapStyleWord(true);
		diagnosticScrollPane = new JScrollPane(diagnosticTextArea);
		diagnosticScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		diagnosticScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		diagnosticPanel.setLayout(new BoxLayout(diagnosticPanel, BoxLayout.Y_AXIS));
		diagnosticPanel.add(diagnosticLabel);
		diagnosticPanel.add(diagnosticScrollPane);
		diagnosticPanel.setPreferredSize(new Dimension(0, 100));

		return diagnosticPanel;
	}

	/**
	 * Creer le panel de prescription
	 * Permet au medecin d'??crire la prescription
	 * 
	 * @return prescriptionPanel
	 */
	private JPanel setPrescriptionPanel() {
		prescriptionPanel = new JPanel();
		prescriptionLabel = new JLabel((String) loadingLanguage
				.getJsonObject()
				.get("frame_medecin_new_consultation_your_prescriptions"));
		prescriptionLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		prescriptionTextArea = new JTextArea();
		prescriptionTextArea.setLineWrap(true);
		prescriptionTextArea.setWrapStyleWord(true);
		prescriptionScrollPane = new JScrollPane(prescriptionTextArea);
		prescriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		prescriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		prescriptionPanel.setLayout(new BoxLayout(prescriptionPanel, BoxLayout.Y_AXIS));
		prescriptionPanel.add(prescriptionLabel);
		prescriptionPanel.add(prescriptionScrollPane);
		prescriptionPanel.setPreferredSize(new Dimension(0, 100));

		return prescriptionPanel;
	}

	/**
	 * Creer le panel de demande de requette d'apperiellage
	 * Affiche deux liste ; la liste qui contient tout les apperielles
	 * q'un patient peut avoir et une liste qui contient les requettes
	 * d'appariellage
	 * 
	 * @return appariellagePanel
	 */
	private JPanel setAppariellagePanel() {

		/**
		 * 
		 */
		appariellageToAddPanel = new JPanel(new BorderLayout());

		while (appariellages.hasNext()) {
			appariellageToAddModel.addElement((String) appariellages.next());
		}
		appariellageToAddTab = new String[appariellageToAddModel.size()];
		for (int i = 0; i < appariellageToAddModel.size(); i++) {
			appariellageToAddTab[i] = appariellageToAddModel.get(i);
		}

		//
		appariellageToAddInputPanel = new JPanel(new BorderLayout());
		apparielFiltreTextField = new JTextField();
		apparielFiltreTextField.setPreferredSize(new Dimension(150, 30));
		appariellageToAddButton = new JButton("+");
		appariellageToAddButton.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		appariellageToAddButton.setPreferredSize(new Dimension(30, 30));
		appariellageToAddInputPanel.add(apparielFiltreTextField, BorderLayout.CENTER);
		appariellageToAddInputPanel.add(appariellageToAddButton, BorderLayout.EAST);
		appariellageToAddInputPanel.setPreferredSize(new Dimension(200, 30));
		appariellageToAddButton.addActionListener(new AppariellageToAddButtonActionListener());
		//
		appariellageToAddList = new JList<>(appariellageToAddModel);
		appariellageToAddScrollPane = new JScrollPane(appariellageToAddList);
		appariellageToAddList.addListSelectionListener(new AppariellageToAddListListener());
		//
		appariellageToAddPanel.add(appariellageToAddInputPanel, BorderLayout.NORTH);
		appariellageToAddPanel.add(appariellageToAddScrollPane, BorderLayout.CENTER);
		/**
		 * 
		 */
		appariellageAlreadyAddPanel = new JPanel(new BorderLayout());
		apparielageRequestLabel = new JLabel((String) loadingLanguage
				.getJsonObject()
				.get("frame_medecin_new_consultation_your_medical_divices_requests"));
		apparielageRequestLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		appariellageAlreadyAddList = new JList<>(appariellageAlreadyAddModel);
		appariellageAlreadyAddScrollPane = new JScrollPane(appariellageAlreadyAddList);

		appariellageAlreadyAddPanel.add(apparielageRequestLabel, BorderLayout.NORTH);
		appariellageAlreadyAddPanel.add(appariellageAlreadyAddScrollPane, BorderLayout.CENTER);

		appariellageAlreadyAddPanel
				.setPreferredSize(new Dimension(200, 350));

		appariellagePanel = new JPanel(new GridLayout(1, 2));
		appariellagePanel.add(appariellageAlreadyAddPanel);
		appariellagePanel.add(appariellageToAddPanel);

		return appariellagePanel;
	}

	/**
	 * Creer le panel de confimation d'une consultation.
	 * Contient la signature, le bouton de confimation qui s'active
	 * si le medecin signe. Et contient de bouton annuler
	 * 
	 * @return confirmConsultationPanel
	 */
	private JPanel setConfirmConsultationPanel() {

		confirmConsultationPanel = new JPanel(new FlowLayout());
		signLabel = new JLabel((String) loadingLanguage
				.getJsonObject()
				.get("frame_medecin_new_consultation_sign"));
		signLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		signCheckBox = new JCheckBox();
		confirmButton = new JButton((String) loadingLanguage
				.getJsonObject()
				.get("frame_medecin_new_consultation_confirm"));
		confirmButton.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		cancelButton = new JButton((String) loadingLanguage
				.getJsonObject()
				.get("frame_medecin_new_consultation_cancel"));
		cancelButton.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		confirmButton.setEnabled(false);
		confirmConsultationPanel.add(signLabel);
		confirmConsultationPanel.add(signCheckBox);
		confirmConsultationPanel.add(Box.createHorizontalStrut(150));
		confirmConsultationPanel.add(confirmButton);
		confirmConsultationPanel.add(cancelButton);
		signCheckBox.addActionListener(new ActionListenerSignBox());
		confirmButton.addActionListener(confirmButtonListener);

		return confirmConsultationPanel;
	}

	/**
	 * -----------------------------------------
	 * Principaux listeners
	 * -----------------------------------------
	 */

	/**
	 * Affiche l'appariellage selectionner dans le text field
	 */
	private class AppariellageToAddListListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			value = appariellageToAddList.getSelectedValue();
			apparielFiltreTextField.setText(value);
		}
	}

	/**
	 * Recupere l'appariellage selectionner la dans liste, et
	 * v??rifie si le text field contient un apparielle.
	 * Si oui l'ajoute a liste de requette d'appariellage q"une seul fois
	 */
	private class AppariellageToAddButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (appariellageToAddModel.contains(apparielFiltreTextField.getText())
					&& !appariellageAlreadyAddArrayList.contains(apparielFiltreTextField.getText())) {

				// ajout des elements
				appariellageAlreadyAddModel.addElement(apparielFiltreTextField.getText());
				appariellageAlreadyAddArrayList.add(apparielFiltreTextField.getText());
			}
		}

	}

	/**
	 * Affiche la partie de la fenetre selectionner
	 * en rendant de panel selectionner visible et
	 * les autres invisibles
	 */
	private class panelSwitchListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int index = panelSwitchList.getSelectedIndex();
			if (index == 0) {
				appariellagePanel.setVisible(false);
				prescriptionPanel.setVisible(true);
				diagnosticPanel.setVisible(false);

			} else if (index == 1) {
				diagnosticPanel.setVisible(false);
				prescriptionPanel.setVisible(false);
				appariellagePanel.setVisible(true);

			} else if (index == 5) {
				diagnosticPanel.setVisible(true);
				prescriptionPanel.setVisible(false);
				appariellagePanel.setVisible(false);
			}
			contentPane.revalidate();
			contentPane.repaint();
		}
	}

	/**
	 * Active le bouton ajouter si le medecin signe
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

	/**
	 * --------------------
	 * Getters and setters
	 * --------------------
	 */

	/**
	 * @return cancelButton
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * @param cancelButton
	 */
	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * @return avisMedicaltextArea
	 */
	public JTextArea getAvisMedicaltextArea() {
		return avisMedicaltextArea;
	}

	/**
	 * @param avisMedicaltextArea
	 */
	public void setAvisMedicaltextArea(JTextArea avisMedicaltextArea) {
		this.avisMedicaltextArea = avisMedicaltextArea;
	}

	/**
	 * @return prescriptionTextArea
	 */
	public JTextArea getPrescriptionTextArea() {
		return prescriptionTextArea;
	}

	/**
	 * @param prescriptionTextArea
	 */
	public void setPrescriptionTextArea(JTextArea prescriptionTextArea) {
		this.prescriptionTextArea = prescriptionTextArea;
	}

	/**
	 * @return diagnosticTextArea
	 */
	public JTextArea getDiagnosticTextArea() {
		return diagnosticTextArea;
	}

	/**
	 * @param diagnosticTextArea
	 */
	public void setDiagnosticTextArea(JTextArea diagnosticTextArea) {
		this.diagnosticTextArea = diagnosticTextArea;
	}

	/**
	 * @return appariellageAlreadyAddArrayList
	 */
	public ArrayList<String> getAppariellageAlreadyAddArrayList() {
		return appariellageAlreadyAddArrayList;
	}

	/**
	 * @param appariellageAlreadyAddArrayList
	 */
	public void setAppariellageAlreadyAddArrayList(ArrayList<String> appariellageAlreadyAddArrayList) {
		this.appariellageAlreadyAddArrayList = appariellageAlreadyAddArrayList;
	}

}
