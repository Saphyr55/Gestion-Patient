/**
 * 
 */
package windows.medecin;

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

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import org.json.simple.JSONArray;

import hopital.Hopital;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnection;

import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Andy
 *
 */
public class FrameConsultation extends JFrame {

	/**
	 *
	 */
	private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();
	private static LoadingDimens loadingDimens = FrameConnection.getLoadingDimens();

	private static boolean isVisible = true;
	private final static int width = (int) ((long) loadingDimens.getJsonObject()
			.get("frame_medecin_create_consultation_width"));
	private final static int height = (int) ((long) loadingDimens.getJsonObject()
			.get("frame_medecin_create_consultation_height"));

	/**
	 * Textes a charger
	 */
	private static final String title = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_new_consultation_tile");
	private static final String frame_medecin_new_consultation_your_prescriptions = (String) loadingLanguage
			.getJsonObject()
			.get("frame_medecin_new_consultation_your_prescriptions");
	private static final String frame_medecin_new_consultation_sign = (String) loadingLanguage
			.getJsonObject()
			.get("frame_medecin_new_consultation_sign");
	private static final String frame_medecin_new_consultation_cancel = (String) loadingLanguage
			.getJsonObject()
			.get("frame_medecin_new_consultation_cancel");
	private static final String frame_medecin_new_consultation_confirm = (String) loadingLanguage
			.getJsonObject()
			.get("frame_medecin_new_consultation_confirm");
	private static final String frame_medecin_new_consultation_medical_advice = (String) loadingLanguage
			.getJsonObject()
			.get("frame_medecin_new_consultation_medical_advice");
	private static final String frame_medecin_new_consultation_your_medical_divices_requests = (String) loadingLanguage
			.getJsonObject()
			.get("frame_medecin_new_consultation_your_medical_divices_requests");
	/**
	 * 
	 */
	private JPanel contentPane = (JPanel) this.getContentPane();

	/**
	 * 
	 */
	private JPanel panelWest;
	private JPanel optionCanAddPanel;
	private String[] optionAddListStrings = {
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_prescription"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_medical_device"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_magnetic_resonance_imaging"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_radiology"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_surgery"),
			(String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation_diagnostics") };
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
	private JPanel panelCenter, switchPanel;

	/**
	 * 
	 */
	private JPanel dataPatientPanel;
	private JLabel lastnamePatientLabel, firstnamePatientLabel;
	private JLabel birthdayPatientLabel, agePatientLabel, secuNumberPatientLabel;

	/**
	 * 
	 */
	private JPanel avisMedicalPanel;
	private JLabel avisMedicalLabel;
	private JTextArea avisMedicaltextArea;
	private JScrollPane avisMedicalScrollPane;

	/**
	 * 
	 */
	private JPanel prescriptionPanel;
	private JLabel prescriptionLabel;
	private JTextArea prescriptionTextArea;
	private JScrollPane prescriptionScrollPane;

	/**
	 * 
	 */
	private JPanel appariellagePanel;

	private JPanel appariellageAlreadyAddPanel;
	private JLabel apparielageRequestLabel;
	private JList<String> appariellageAlreadyAddList;
	private DefaultListModel<String> appariellageAlreadyAddModel = new DefaultListModel<>();
	private JScrollPane appariellageAlreadyAddScrollPane;
	private ArrayList<String> appariellageAlreadyAddArrayList;

	private JPanel appariellageToAddPanel;
	private JPanel appariellageToAddListPanel, appariellageToAddInputPanel;
	private JTextField apparielFiltreTextField;
	private JButton appariellageToAddButton;
	private JList<String> appariellageToAddList;
	private DefaultListModel<String> appariellageToAddModel = new DefaultListModel<>();
	private JScrollPane appariellageToAddScrollPane;
	private JSONArray appariellagesJsonArray = (JSONArray) loadingLanguage.getJsonObject().get("appariel_medical");
	private Iterator<?> appariellages = appariellagesJsonArray.iterator();

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
	public FrameConsultation() {
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
		optionCanAddScrollPane.setPreferredSize(new Dimension(200, height));
		optionCanAddPanel.add(optionCanAddScrollPane);

		panelWest = new JPanel();
		panelWest.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelWest.add(optionCanAddPanel);
		panelWest.setLayout(new BoxLayout(panelWest, BoxLayout.Y_AXIS));

		optionCanAddList.addListSelectionListener(new OptionCanAddListSelectionListener());

		return panelWest;
	}

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

	private JPanel setSwitchPanel() {
		switchPanel = new JPanel(new CardLayout());
		switchPanel.add(setPrescriptionPanel());
		switchPanel.add(setAppariellagePanel());
		return switchPanel;
	}

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

	private JPanel setAvisMedicalPanel() {

		avisMedicalPanel = new JPanel();
		avisMedicalLabel = new JLabel(frame_medecin_new_consultation_medical_advice);
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

	private JPanel setPrescriptionPanel() {
		prescriptionPanel = new JPanel();
		prescriptionLabel = new JLabel(frame_medecin_new_consultation_your_prescriptions);
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

	private JPanel setAppariellagePanel() {

		/**
		 * 
		 */
		appariellageToAddPanel = new JPanel(new BorderLayout());

		while (appariellages.hasNext()) {
			appariellageToAddModel.addElement((String) appariellages.next());
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
		//
		appariellageToAddList = new JList<>(appariellageToAddModel);
		appariellageToAddScrollPane = new JScrollPane(appariellageToAddList);
		//
		appariellageToAddPanel.add(appariellageToAddInputPanel, BorderLayout.NORTH);
		appariellageToAddPanel.add(appariellageToAddScrollPane, BorderLayout.CENTER);
		/**
		 * 
		 */
		appariellageAlreadyAddPanel = new JPanel(new BorderLayout());
		apparielageRequestLabel = new JLabel(frame_medecin_new_consultation_your_medical_divices_requests);
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

	private JPanel setConfirmConsultationPanel() {

		confirmConsultationPanel = new JPanel(new FlowLayout());
		signLabel = new JLabel(frame_medecin_new_consultation_sign);
		signLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		signCheckBox = new JCheckBox();
		confirmButton = new JButton(frame_medecin_new_consultation_confirm);
		confirmButton.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		cancelButton = new JButton(frame_medecin_new_consultation_cancel);
		cancelButton.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		confirmButton.setEnabled(false);
		confirmConsultationPanel.add(signLabel);
		confirmConsultationPanel.add(signCheckBox);
		confirmConsultationPanel.add(Box.createHorizontalStrut(150));
		confirmConsultationPanel.add(confirmButton);
		confirmConsultationPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListenerCancelButton());
		signCheckBox.addActionListener(new ActionListenerSignBox());

		return confirmConsultationPanel;
	}

	private class OptionCanAddListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int index = optionCanAddList.getSelectedIndex();
			if (index == 0) {
				appariellagePanel.setVisible(false);
				prescriptionPanel.setVisible(true);
				contentPane.revalidate();
				contentPane.repaint();
			} else if (index == 1) {
				prescriptionPanel.setVisible(false);
				appariellagePanel.setVisible(true);
				contentPane.revalidate();
				contentPane.repaint();
			}
		}
	}

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

	private class ActionListenerCancelButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
