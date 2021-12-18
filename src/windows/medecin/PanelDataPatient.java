package windows.medecin;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import windows.FrameConnection;

public class PanelDataPatient extends JPanel {

	/*
	 * Charge de quoi charger les differents textes dans strings.json
	 */
	private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();
	private static LoadingDimens dimens = new LoadingDimens();

	/**
	 * Fonts
	 */
	private static Font font1 = new Font("SansSerif", Font.BOLD, 20);

	/**
	 * Variables d'options pour la fenetre de gestion
	 */
	private static final int width = (int) ((long) dimens.getJsonObject().get("frame_medecin_width"));
	private static final int height = (int) ((long) dimens.getJsonObject().get("frame_medecin_height"));

	private static final String frame_medecin_lastname = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_lastname");
	private static final String frame_medecin_firstname = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_firstname");
	private static final String frame_medecin_birthday = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_birthday");
	private static final String frame_medecin_age = (String) loadingLanguage.getJsonObject().get("frame_medecin_age");

	/**
	 * Le panel central avec les données dus patient
	 */
	private JPanel dataPatientPanel;
	private JPanel patientStringDataPanel;
	private JTextField lastnameStringTextField, lastnamePatientTextField;
	private JTextField firstnameStringTextField, firstnamePatientTextField;
	private JTextField birthdayStringTextField;
	private JFormattedTextField birthdayPatientTextField;
	private JTextField secuNumberStringTextField;
	private JFormattedTextField secuNumberPatientTextField;
	private JTextField phoneStringTextField;
	private JFormattedTextField phonePatientTextField;
	private JTextField addressStringTextField, addressPatientTextField;
	private JButton testButtonForSwitch;

	/**
	 * 
	 */
	private PanelSwitchTypeConsultation switchTypeConsultationPanel = new PanelSwitchTypeConsultation();

	/**
	 * Consutructeur du panel des données du patient
	 */
	public PanelDataPatient() {
		super();
		super.add(setPanelDataPatient());
	}

	/**
	 * 
	 * @return dataPatientPanel
	 */
	private JPanel setPanelDataPatient() {
		dataPatientPanel = new JPanel(new GridLayout(1, 2));

		setPanelDataString();
		setSwitchTypeConsultationPanel();

		dataPatientPanel.add(patientStringDataPanel);
		dataPatientPanel.add(switchTypeConsultationPanel);

		return dataPatientPanel;
	}

	/**
	 * 
	 */
	private void setSwitchTypeConsultationPanel() {
		switchTypeConsultationPanel = new PanelSwitchTypeConsultation();
		testButtonForSwitch = new JButton("+");
		switchTypeConsultationPanel.add(testButtonForSwitch);
		switchTypeConsultationPanel.setPreferredSize(new Dimension(width / 3, height - 50));
	}

	/**
	 * 
	 */
	private void setPanelDataString() {
		/**
		 * 
		 */
		patientStringDataPanel = new JPanel();
		// nom
		lastnameStringTextField = new JTextField(frame_medecin_lastname);
		lastnamePatientTextField = new JTextField();
		lastnameStringTextField.setFont(font1);
		lastnamePatientTextField.setFont(font1);

		// prenom
		firstnameStringTextField = new JTextField(frame_medecin_firstname);
		firstnamePatientTextField = new JTextField();
		firstnameStringTextField.setFont(font1);
		firstnamePatientTextField.setFont(font1);

		// birthday
		birthdayStringTextField = new JTextField(frame_medecin_birthday);
		birthdayPatientTextField = new JFormattedTextField();
		birthdayStringTextField.setFont(font1);
		birthdayPatientTextField.setFont(font1);

		// numero de securité social
		secuNumberStringTextField = new JTextField("Secu number");
		secuNumberPatientTextField = new JFormattedTextField();
		secuNumberStringTextField.setFont(font1);
		secuNumberPatientTextField.setFont(font1);

		// numero de telephone
		phoneStringTextField = new JTextField("Phone number");
		phonePatientTextField = new JFormattedTextField();
		phoneStringTextField.setFont(font1);
		phonePatientTextField.setFont(font1);

		// adresse
		addressStringTextField = new JTextField("Address");
		addressPatientTextField = new JTextField();
		addressStringTextField.setFont(font1);
		addressPatientTextField.setFont(font1);

		lastnameStringTextField.setEditable(false);
		lastnamePatientTextField.setEditable(false);

		firstnameStringTextField.setEditable(false);
		firstnamePatientTextField.setEditable(false);

		birthdayStringTextField.setEditable(false);
		birthdayPatientTextField.setEditable(false);

		secuNumberStringTextField.setEditable(false);
		secuNumberPatientTextField.setEditable(false);

		phoneStringTextField.setEditable(false);
		phonePatientTextField.setEditable(false);

		addressStringTextField.setEditable(false);
		addressPatientTextField.setEditable(false);

		patientStringDataPanel.add(lastnameStringTextField);
		patientStringDataPanel.add(lastnamePatientTextField);

		patientStringDataPanel.add(firstnameStringTextField);
		patientStringDataPanel.add(firstnamePatientTextField);

		patientStringDataPanel.add(birthdayStringTextField);
		patientStringDataPanel.add(birthdayPatientTextField);

		patientStringDataPanel.add(secuNumberStringTextField);
		patientStringDataPanel.add(secuNumberPatientTextField);

		patientStringDataPanel.add(phoneStringTextField);
		patientStringDataPanel.add(phonePatientTextField);

		patientStringDataPanel.add(addressStringTextField);
		patientStringDataPanel.add(addressPatientTextField);

		patientStringDataPanel.setLayout(new BoxLayout(patientStringDataPanel, BoxLayout.PAGE_AXIS));
		patientStringDataPanel.setPreferredSize(new Dimension(width / 3, height - 50));
	}

	public JPanel getDataPatientPanel() {
		return dataPatientPanel;
	}

	public void setDataPatientPanel(JPanel dataPatientPanel) {
		this.dataPatientPanel = dataPatientPanel;
	}

	public JPanel getPatientStringDataPanel() {
		return patientStringDataPanel;
	}

	public void setPatientStringDataPanel(JPanel patientStringDataPanel) {
		this.patientStringDataPanel = patientStringDataPanel;
	}

	public JPanel getSwitchTypeConsultationPanel() {
		return switchTypeConsultationPanel;
	}

	public void setSwitchTypeConsultationPanel(PanelSwitchTypeConsultation switchTypeConsultationPanel) {
		this.switchTypeConsultationPanel = switchTypeConsultationPanel;
	}

	public JTextField getLastnameStringTextField() {
		return lastnameStringTextField;
	}

	public void setLastnameStringTextField(JTextField lastnameStringTextField) {
		this.lastnameStringTextField = lastnameStringTextField;
	}

	public JTextField getLastnamePatientTextField() {
		return lastnamePatientTextField;
	}

	public void setLastnamePatientTextField(JTextField lastnamePatientTextField) {
		this.lastnamePatientTextField = lastnamePatientTextField;
	}

	public JTextField getFirstnameStringTextField() {
		return firstnameStringTextField;
	}

	public void setFirstnameStringTextField(JTextField firstnameStringTextField) {
		this.firstnameStringTextField = firstnameStringTextField;
	}

	public JTextField getFirstnamePatientTextField() {
		return firstnamePatientTextField;
	}

	public void setFirstnamePatientTextField(JTextField firstnamePatientTextField) {
		this.firstnamePatientTextField = firstnamePatientTextField;
	}

	public JTextField getBirthdayStringTextField() {
		return birthdayStringTextField;
	}

	public void setBirthdayStringTextField(JTextField birthdayStringTextField) {
		this.birthdayStringTextField = birthdayStringTextField;
	}

	public JFormattedTextField getBirthdayPatientTextField() {
		return birthdayPatientTextField;
	}

	public void setBirthdayPatientTextField(JFormattedTextField birthdayPatientTextField) {
		this.birthdayPatientTextField = birthdayPatientTextField;
	}

	public JTextField getSecuNumberStringTextField() {
		return secuNumberStringTextField;
	}

	public void setSecuNumberStringTextField(JTextField secuNumberStringTextField) {
		this.secuNumberStringTextField = secuNumberStringTextField;
	}

	public JFormattedTextField getSecuNumberPatientTextField() {
		return secuNumberPatientTextField;
	}

	public void setSecuNumberPatientTextField(JFormattedTextField secuNumberPatientTextField) {
		this.secuNumberPatientTextField = secuNumberPatientTextField;
	}

	public JTextField getPhoneStringTextField() {
		return phoneStringTextField;
	}

	public void setPhoneStringTextField(JTextField phoneStringTextField) {
		this.phoneStringTextField = phoneStringTextField;
	}

	public JFormattedTextField getPhonePatientTextField() {
		return phonePatientTextField;
	}

	public void setPhonePatientTextField(JFormattedTextField phonePatientTextField) {
		this.phonePatientTextField = phonePatientTextField;
	}

	public JTextField getAddressStringTextField() {
		return addressStringTextField;
	}

	public void setAddressStringTextField(JTextField addressStringTextField) {
		this.addressStringTextField = addressStringTextField;
	}

	public JTextField getAddressPatientTextField() {
		return addressPatientTextField;
	}

	public void setAddressPatientTextField(JTextField addressPatientTextField) {
		this.addressPatientTextField = addressPatientTextField;
	}

	public JButton getTestButtonForSwitch() {
		return testButtonForSwitch;
	}

	public void setTestButtonForSwitch(JButton testButtonForSwitch) {
		this.testButtonForSwitch = testButtonForSwitch;
	}

}