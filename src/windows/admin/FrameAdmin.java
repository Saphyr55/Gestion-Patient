package windows.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;

import hopital.Hopital;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import windows.FrameConnection;

/**
 * 
 */
public class FrameAdmin extends JFrame {

	private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();

	/**
	 * 
	 */
	private static final int width = 720;
	private static final int height = 520;
	private static final String title = "Admin Gestion";
	private static boolean isVisible = true;

	/**
	 * 
	 */
	private JPanel contentPanel = (JPanel) getContentPane();

	/**
	 * Composant du JPanel de liste de patient
	 */
	private JPanel panelListPatient;
	private JList<String> listPatient;
	private JScrollPane listPatientScrollPane;
	private JButton addPatient;
	private JTextField foundPatientField;
	private DefaultListModel<String> namePatients = new DefaultListModel<>();
	private JPopupMenu popupMenuListPatient;
	private JMenuItem menuItemAddPatient, menuItemSupprPatient;
	private ArrayList<String> listNamePatient = new ArrayList<>();

	private String[] namesStringsForTextFields = { (String) loadingLanguage.getJsonObject().get("frame_admin_lastname"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_firstname"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_birthday"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_secu_number"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_phone"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_adresse") };
	private JPanel dataPatientPanel, panelTop, panelCenter, panelBottom;
	private JTextField[] dataPatientTextFields;
	private JTextField patientLastnameInputTextField;
	private JTextField patientFirstnameInpuTextField;
	private JFormattedTextField patientDateInputFormattedTextField;
	private JFormattedTextField patientSecuNumberInputFormattedTextField;
	private JFormattedTextField patientNumberPhoneInputFormFormattedTextField;
	private JTextField patientAddressInputTextField;
	private JButton switchLectureModifDataPatient;
	private JButton confirmModifButton;

	private static FrameAdminAddPatient frameAdminAddPatient;

	private static MaskFormatter dateFormatter;
	private static MaskFormatter secuNumbeFormatter;
	private static MaskFormatter phoneNumberFormatter;
	private static Font font1 = new Font("SansSerif", Font.BOLD, 20);
	private static Font font2 = new Font("SansSerif", Font.BOLD, 14);

	/**
	 * 
	 */
	private Patient currentPatient;

	/**
	 * 
	 */
	public FrameAdmin() {
		super(title);
		setOptionFrame();
		contentPanel.add(setListPatient(), BorderLayout.WEST);
		contentPanel.add(setDataPatient(), BorderLayout.CENTER);
		setVisible(isVisible);
	}

	/**
	 *
	 */
	private void setOptionFrame() {
		Hopital.loadingPatient();
		try {
			UIManager.setLookAndFeel(FrameConnection.getModel());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
		setSize(width, height);
		setResizable(true);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Permet d'afficher la liste de patient,
	 * de pouvoir de rajouter des patients,
	 * de chercher un patient dans la liste
	 * 
	 * @return liste de patient
	 */
	private JPanel setListPatient() {
		/**
		 * Inititalisation des attribus pour la liste de patient
		 */
		panelListPatient = new JPanel(new BorderLayout());
		panelListPatient.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

		JPanel panelTop = new JPanel(new BorderLayout());
		addPatient = new JButton("+");
		foundPatientField = new JTextField();

		/**
		 * Affichage de la frame pour ajouter un patient lors du clique du bouton '+'
		 */
		addPatient.addActionListener(new AddButtonListener());

		/**
		 * 
		 */
		for (int i = 0; i < Hopital.getPatients().size(); i++) {
			String namePatientString = Hopital.getPatients().get(i).getLastName() + " "
					+ Hopital.getPatients().get(i).getLastName();
			namePatients.addElement(namePatientString);
			listNamePatient.add(namePatientString);
		}
		listPatient = new JList<>(namePatients);
		listPatientScrollPane = new JScrollPane(listPatient);

		/**
		 * Option text sur le button et autre
		 */
		addPatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		foundPatientField.setPreferredSize(new Dimension(0, 20));

		/**
		 * Dimensions
		 */
		panelListPatient.setPreferredSize(new Dimension(200, FrameAdmin.HEIGHT));
		panelListPatient.setPreferredSize(new Dimension(200, FrameAdmin.HEIGHT));
		panelListPatient.setMinimumSize(new Dimension(200, FrameAdmin.HEIGHT));
		panelListPatient.setMaximumSize(new Dimension(200, FrameAdmin.HEIGHT));
		panelTop.setPreferredSize(new Dimension(0, 40));
		foundPatientField.setPreferredSize(new Dimension(0, 100));

		/**
		 * Ajout du button d'ajout de patient et de recherche de patient dans le
		 * panelTop
		 * Et ajout du panelTop et de la listPatient dans le panelListPatient
		 */
		panelTop.add(addPatient, BorderLayout.WEST);
		panelTop.add(foundPatientField, BorderLayout.CENTER);
		panelListPatient.add(panelTop, BorderLayout.NORTH);
		panelListPatient.add(listPatientScrollPane, BorderLayout.CENTER);

		/*
		 * Actionne l'affichage de la liste d'ordonnances
		 * et les données du patient selectionner
		 */
		listPatient.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				/**
				 * Recupere index de selection si on clique une fois
				 */
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) event.getSource();
				int index = list.locationToIndex(event.getPoint());
				if (event.getClickCount() == 1) {
					// le patient courant sur l'index recuperer lors du clique
					currentPatient = Hopital.getPatients().get(index);

					/**
					 * Le clique gauche declenche la liste la de consultation du patient
					 * selectionner
					 */
					if (SwingUtilities.isLeftMouseButton(event)) {
						patientLastnameInputTextField.setText(currentPatient.getLastName());
						patientFirstnameInpuTextField.setText(currentPatient.getFirstName());
						patientDateInputFormattedTextField
								.setText(currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE));
						patientAddressInputTextField.setText(currentPatient.getAddress());
						patientSecuNumberInputFormattedTextField.setText(currentPatient.getSecuNumber());
						patientNumberPhoneInputFormFormattedTextField.setText(currentPatient.getSecuNumber());
					}

					/**
					 * Le clique droit declenche une popup permetant de supprimer ou ajouter un
					 * patient
					 */
					else if (SwingUtilities.isRightMouseButton(event)) {
						try {
							/**
							 * La popup
							 */
							setPopupMenuOnRigthClickListPatient().show(
									event.getComponent(), event.getX(), event.getY());

							/**
							 * Si la selection est supprimer alors lance showConfimDialog
							 * pour pouvoir confirmer l'acte de suppression
							 */
							menuItemSupprPatient.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									int input = JOptionPane.showConfirmDialog(null,
											(String) loadingLanguage.getJsonObject()
													.get("frame_admin_confirm_delete_patient"));

									/*
									 * 
									 */
									if (input == JOptionPane.YES_OPTION) {
										System.out.println(currentPatient.getLastName() + " a ete supprimer");
									}
								}
							});

							/**
							 * Si la selection est d'ajouter un patient
							 * affichage une fenetre d'ajout de patient dans la liste de medecin
							 */
							menuItemAddPatient.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {

								}
							});

						} catch (IndexOutOfBoundsException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		/**
		 * Trouve le patient ecrit
		 */
		foundPatientField.getDocument().addDocumentListener(new DocumentListenerPatientField());

		return panelListPatient;
	}

	/**
	 * 
	 * @return dataPatientPanel
	 */
	private JPanel setDataPatient() {
		dataPatientPanel = new JPanel(new BorderLayout());
		panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		switchLectureModifDataPatient = new JButton(
				(String) loadingLanguage.getJsonObject().get("frame_admin_switch_mode_modification"));
		panelTop.add(switchLectureModifDataPatient);

		panelCenter = new JPanel(new BorderLayout());
		JPanel stringsTextFieldsPanel = new JPanel();
		JPanel dataPatientTextFieldsPanel = new JPanel();
		dataPatientTextFields = new JTextField[6];
		for (int i = 0; i < namesStringsForTextFields.length; i++) {
			dataPatientTextFields[i] = new JTextField(namesStringsForTextFields[i]);
			dataPatientTextFields[i].setEditable(false);
			dataPatientTextFields[i].setFont(font2);
			stringsTextFieldsPanel.add(dataPatientTextFields[i]);
		}

		patientLastnameInputTextField = new JTextField();
		patientLastnameInputTextField.setEditable(false);
		patientLastnameInputTextField.setFont(font1);
		patientFirstnameInpuTextField = new JTextField();
		patientFirstnameInpuTextField.setEditable(false);
		patientFirstnameInpuTextField.setFont(font1);
		patientDateInputFormattedTextField = new JFormattedTextField(dateFormatter);
		patientDateInputFormattedTextField.setEditable(false);
		patientDateInputFormattedTextField.setFont(font1);
		patientSecuNumberInputFormattedTextField = new JFormattedTextField(secuNumbeFormatter);
		patientSecuNumberInputFormattedTextField.setEditable(false);
		patientSecuNumberInputFormattedTextField.setFont(font1);
		patientNumberPhoneInputFormFormattedTextField = new JFormattedTextField(phoneNumberFormatter);
		patientNumberPhoneInputFormFormattedTextField.setEditable(false);
		patientNumberPhoneInputFormFormattedTextField.setFont(font1);
		patientAddressInputTextField = new JTextField();
		patientAddressInputTextField.setEditable(false);
		patientAddressInputTextField.setFont(font1);

		dataPatientTextFieldsPanel.add(patientLastnameInputTextField);
		dataPatientTextFieldsPanel.add(patientFirstnameInpuTextField);
		dataPatientTextFieldsPanel.add(patientDateInputFormattedTextField);
		dataPatientTextFieldsPanel.add(patientSecuNumberInputFormattedTextField);
		dataPatientTextFieldsPanel.add(patientNumberPhoneInputFormFormattedTextField);
		dataPatientTextFieldsPanel.add(patientAddressInputTextField);

		stringsTextFieldsPanel.setLayout(new BoxLayout(stringsTextFieldsPanel, BoxLayout.PAGE_AXIS));
		dataPatientTextFieldsPanel.setLayout(new BoxLayout(dataPatientTextFieldsPanel, BoxLayout.PAGE_AXIS));

		panelCenter.add(stringsTextFieldsPanel, BorderLayout.WEST);
		panelCenter.add(dataPatientTextFieldsPanel, BorderLayout.CENTER);

		panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		confirmModifButton = new JButton(
				(String) loadingLanguage.getJsonObject().get("frame_admin_confirm_modification"));
		panelBottom.add(confirmModifButton);

		dataPatientPanel.add(panelTop, BorderLayout.NORTH);
		dataPatientPanel.add(panelCenter, BorderLayout.CENTER);
		dataPatientPanel.add(panelBottom, BorderLayout.SOUTH);
		return dataPatientPanel;
	}

	/**
	 * Affiche la frame pour ajouter des patients
	 * Ne peut etre afficher plusieur fois
	 */
	private void setFrameAddPatient() {
		if (frameAdminAddPatient == null)
			frameAdminAddPatient = new FrameAdminAddPatient();

		frameAdminAddPatient.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				frameAdminAddPatient = null;
			}
		});
	}

	/**
	 * Filtre de menu de recherche d'un patient
	 * 
	 * @param model
	 * @param filter
	 */
	private void filterModel(DefaultListModel<String> model, String filter) {
		for (String patientName : listNamePatient) {
			if (!patientName.startsWith(filter)) {
				if (model.contains(patientName)) {
					model.removeElement(patientName);
				}
			} else {
				if (!model.contains(patientName)) {
					model.addElement(patientName);
				}
			}
		}
	}

	/**
	 * PopupMenu lors du click droit sur la liste de patient
	 * 
	 * @return popupMenu
	 */
	private JPopupMenu setPopupMenuOnRigthClickListPatient() {
		popupMenuListPatient = new JPopupMenu();
		menuItemAddPatient = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_admin_popup_add"));
		menuItemSupprPatient = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_admin_popup_delete"));
		popupMenuListPatient.add(menuItemAddPatient);
		popupMenuListPatient.add(menuItemSupprPatient);
		return popupMenuListPatient;
	}

	/**
	 * Le listener du text field pour chercher un patient
	 */
	private class DocumentListenerPatientField implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			filter();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			filter();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			filter();
		}

		/**
		 * Recupere l'entrée dans le text field
		 * Et applique filtre model dans notre liste de patient
		 */
		private void filter() {
			String filter = foundPatientField.getText();
			filterModel((DefaultListModel<String>) listPatient.getModel(), filter);
		}
	}

	/**
	 * 
	 */
	private class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (frameAdminAddPatient == null) {
				setFrameAddPatient();
			}
		}
	}

	/**
	 * 
	 */
	public static class CancelButtonFrameAddPatientListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frameAdminAddPatient.dispose();
			frameAdminAddPatient = null;
		}
	}

}
