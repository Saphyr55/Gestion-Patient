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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;

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
import javax.swing.text.MaskFormatter;

import hopital.Hopital;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnection;

/**
 * 
 */
public class FrameAdmin extends JFrame {

	private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();
	private static LoadingDimens loadingDimens = FrameConnection.getLoadingDimens();

	/**
	 * Données de la frame
	 */
	private static final int width = (int) ((long) loadingDimens.getJsonObject().get("frame_admin_width"));
	private static final int height = (int) ((long) loadingDimens.getJsonObject().get("frame_admin_height"));
	private static final String title = (String) loadingLanguage.getJsonObject().get("frame_admin_title");
	private static boolean isVisible = true;

	/**
	 * Le panel principal
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

	/**
	 * Données de certains composant
	 */
	private String[] namesStringsForTextFields = {
			(String) loadingLanguage.getJsonObject().get("frame_admin_lastname"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_firstname"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_birthday"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_secu_number"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_phone"),
			(String) loadingLanguage.getJsonObject().get("frame_admin_adresse") };
	private ArrayList<String> listNamePatient = new ArrayList<>();
	private static MaskFormatter dateFormatter;
	private static MaskFormatter secuNumbeFormatter;
	private static MaskFormatter phoneNumberFormatter;
	private static Font font1 = new Font("SansSerif", Font.BOLD, 20);
	private static Font font2 = new Font("SansSerif", Font.BOLD, 14);

	/**
	 * Frame d'ajout de patient
	 */
	private static FrameAdminAddPatient frameAdminAddPatient;

	/**
	 * Patient courant a la selection de la liste
	 */
	private Patient currentPatient;

	/**
	 * Constructeur de la frenetre de l'admin
	 */
	public FrameAdmin() {
		super(title);
		setOptionFrame();
		contentPanel.add(setListPatient(), BorderLayout.WEST);
		contentPanel.add(setDataPatient(), BorderLayout.CENTER);
		setVisible(isVisible);
	}

	/**
	 * Option de la frame
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
		 * Liste de patient
		 */
		for (int i = 0; i < Hopital.getPatients().size(); i++) {
			String namePatientString = Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getLastName()
					.toUpperCase() + " "
					+ Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getFirstName();
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
				if (index >= 0) {
					if (event.getClickCount() == 1) {
						// le patient courant sur l'index recuperer lors du clique
						currentPatient = Hopital.getPatients().get(Hopital.getPatients().size() - 1 - index);

						/**
						 * Le clique gauche declenche la liste la de consultation du patient
						 * selectionner
						 */
						if (SwingUtilities.isLeftMouseButton(event)) {
							patientLastnameInputTextField.setText(currentPatient.getLastName());
							patientFirstnameInpuTextField.setText(currentPatient.getFirstName());
							patientDateInputFormattedTextField
									.setText(currentPatient.getBirthday()
											.format(Hopital.FORMATEUR_LOCALDATE).replace("-", ""));
							patientAddressInputTextField.setText(currentPatient.getAddress());
							patientSecuNumberInputFormattedTextField.setText(currentPatient.getSecuNumber());
							patientNumberPhoneInputFormFormattedTextField.setText(currentPatient.getPhoneNumber());
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
								 * Si la selection est d'ajouter un patient
								 * affichage une fenetre d'ajout de patient dans la liste de medecin
								 */
								menuItemAddPatient.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										setFrameAddPatient();
									}
								});

							} catch (IndexOutOfBoundsException e) {
								e.printStackTrace();
							}
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
	 * Panel des données du patient courant
	 * Affiche ces données sous forme de text field
	 * non editable, sauf appuyant sur le bouton de switch
	 * 
	 * @return dataPatientPanel
	 */
	private JPanel setDataPatient() {

		/**
		 * Charge les masks formatteur
		 */
		try {
			dateFormatter = new MaskFormatter("## / ## / ####");
			secuNumbeFormatter = new MaskFormatter("### ### ### ### ###");
			phoneNumberFormatter = new MaskFormatter("## ## ## ## ##");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/**
		 * panel data patient
		 */
		dataPatientPanel = new JPanel(new BorderLayout());
		panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		switchLectureModifDataPatient = new JButton(
				(String) loadingLanguage.getJsonObject().get("frame_admin_switch_mode_modification"));
		switchLectureModifDataPatient.addActionListener(new SwicthModeListener());
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

		/**
		 * Text feilds input
		 */
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

		/**
		 * Panel de confirmation
		 */
		panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		confirmModifButton = new JButton(
				(String) loadingLanguage.getJsonObject().get("frame_admin_confirm_modification"));
		confirmModifButton.setEnabled(false);
		confirmModifButton.addActionListener(new ConfirmModificationButtonListener());
		panelBottom.add(confirmModifButton);

		/**
		 * 
		 */
		dataPatientPanel.add(panelTop, BorderLayout.NORTH);
		dataPatientPanel.add(panelCenter, BorderLayout.CENTER);
		dataPatientPanel.add(panelBottom, BorderLayout.SOUTH);

		return dataPatientPanel;
	}

	/**
	 * Affiche la frame pour ajouter des patients
	 * Ne peut etre afficher qu'une seul fois
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
		menuItemAddPatient.addActionListener(new AddButtonListener());
		menuItemSupprPatient.addActionListener(new DeletePatientMenuItemListener());
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
	 * Le listener du bouton ajouter qui affiche la fenetre
	 * ajouter un patient qu'une seul fois
	 */
	private class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (frameAdminAddPatient == null) {
				setFrameAddPatient();
			}
			frameAdminAddPatient.getConfirmButton().addActionListener(new ConfimButtonListener());
		}
	}

	/**
	 * Le listener du bouton annuler qui la ferme fenetre
	 * d'ajout de patient
	 */
	public static class CancelButtonFrameAddPatientListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frameAdminAddPatient.dispose();
			frameAdminAddPatient = null;
		}
	}

	/**
	 * Listener du bouton switch mode
	 * Permet de switch en mode lecture ou modication
	 */
	private class SwicthModeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (switchLectureModifDataPatient.getText()
					.equals((String) loadingLanguage.getJsonObject().get("frame_admin_switch_mode_modification"))) {

				switchLectureModifDataPatient
						.setText((String) loadingLanguage.getJsonObject()
								.get("frame_admin_switch_mode_read"));
				patientLastnameInputTextField.setEditable(true);
				patientFirstnameInpuTextField.setEditable(true);
				patientDateInputFormattedTextField.setEditable(true);
				patientSecuNumberInputFormattedTextField.setEditable(true);
				patientNumberPhoneInputFormFormattedTextField.setEditable(true);
				patientAddressInputTextField.setEditable(true);
				confirmModifButton.setEnabled(true);
				contentPanel.revalidate();
				contentPanel.repaint();

			} else if (switchLectureModifDataPatient.getText()
					.equals((String) loadingLanguage.getJsonObject()
							.get("frame_admin_switch_mode_read"))) {

				switchLectureModifDataPatient
						.setText((String) loadingLanguage.getJsonObject()
								.get("frame_admin_switch_mode_modification"));
				patientLastnameInputTextField.setEditable(false);
				patientFirstnameInpuTextField.setEditable(false);
				patientDateInputFormattedTextField.setEditable(false);
				patientSecuNumberInputFormattedTextField.setEditable(false);
				patientNumberPhoneInputFormFormattedTextField.setEditable(false);
				patientAddressInputTextField.setEditable(false);
				confirmModifButton.setEnabled(false);
				contentPanel.revalidate();
				contentPanel.repaint();

			}

		}
	}

	/**
	 * Creer le patient en appuyant sur le bouton confirmer
	 */
	private class ConfimButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			/**
			 * Recupere les données du rentrée dans les textes : nom, prenom, date, secu
			 * number, phone number, et adresse
			 */
			String getLastnamePatient = frameAdminAddPatient.getPatientLastnameInputTextField().getText();
			String getFirstnamePatient = frameAdminAddPatient.getPatientFirstnameInpuTextField().getText();
			LocalDate getBirthdayPatient = LocalDate
					.parse(frameAdminAddPatient.getPatientDateInputFormattedTextField().getText().replace("/", "-"),
							Hopital.FORMATEUR_LOCALDATE);
			String getSecuNumberPatient = frameAdminAddPatient.getPatientSecuNumberInputFormattedTextField().getText()
					.replace(" ", "");
			String getPhoneNumberPatient = frameAdminAddPatient.getPatientNumberPhoneInputFormFormattedTextField()
					.getText().replace(" ", "");
			String getAdressePatient = frameAdminAddPatient.getPatientAddressInputTextField().getText();

			/**
			 * Rerifie si les inputs de nom, prenoms et secu number sont valides
			 */
			if (!getLastnamePatient.equals("") && !getLastnamePatient.contains(" ") && getLastnamePatient != null &&
					!getFirstnamePatient.equals("") && !getFirstnamePatient.contains(" ")
					&& getFirstnamePatient != null && getSecuNumberPatient.length() == 15) {

				new Patient(getFirstnamePatient, getLastnamePatient,
						getBirthdayPatient, getSecuNumberPatient,
						getPhoneNumberPatient, getAdressePatient);
				/**
				 * Fermeture de la fenetre d'ajout de patient
				 */
				frameAdminAddPatient.dispose();
				frameAdminAddPatient = null;

				/**
				 * Recharge tous les patients
				 */
				listNamePatient.removeAll(listNamePatient);
				namePatients.removeAllElements();
				for (int i = 0; i < Hopital.getPatients().size(); i++) {
					namePatients.addElement(
							Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getLastName() + " " +
									Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getFirstName());
					listNamePatient
							.add(Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getLastName() + " " +
									Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getFirstName());
				}
				listPatient = new JList<>(namePatients);
				contentPanel.revalidate();
				contentPanel.repaint();
			}

			/**
			 * Affiche un message d'erreur si les inputs sont mauvais
			 */
			else {
				JOptionPane.showMessageDialog(frameAdminAddPatient, "La saisie n'est pas correct.", "Input wrong",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 * Permet de supprimer un patient lors du clique du menu item
	 */
	private class DeletePatientMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			int input = JOptionPane.showConfirmDialog(null,
					(String) loadingLanguage.getJsonObject()
							.get("frame_admin_confirm_delete_patient"));
			if (input == JOptionPane.YES_OPTION) {

				String linePatientToDelete = ""
						+ currentPatient.getIdentifiant() + "&"
						+ currentPatient.getFirstName() + "&"
						+ currentPatient.getLastName().toUpperCase() + "&"
						+ currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE) + "&"
						+ currentPatient.getSecuNumber() + "&"
						+ currentPatient.getPhoneNumber() + "&"
						+ currentPatient.getAddress();

				String linePatientToDeleteInMedecin;
				Medecin medecin;
				int j = 0;
				for (int i = 0; i < Hopital.getMedecins().size(); i++) {
					medecin = Hopital.getMedecins().get(i);
					if (medecin.getPatients().get(i).getIdentifiant() == currentPatient.getIdentifiant()) {
						j++;
						break;
					}
				}
				if (j == 1) {
					

					

				}
					

				File[] filesPatient = new File("./src/log/medecin/").listFiles();
				
				

				File patientFile = new File("./src/log/patient/patients.txt");
				ArrayList<String> lines = new ArrayList<>();
				String line;
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
							patientFile.getAbsolutePath()), "UTF-8"));
					while ((line = reader.readLine()) != null) {
						if (!line.equals(linePatientToDelete))
							lines.add(line);
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					if (patientFile.delete())
						System.out.println("Suppression du fichier patient réussi");
					if (patientFile.createNewFile())
						System.out.println("Recreation du fichier patient réussi");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(
						patientFile.getAbsolutePath(), true), StandardCharsets.UTF_8)) {
					for (int i = 0; i < lines.size(); i++) {
						writer.write(lines.get(i) + "\n");
					}
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Hopital.getPatients().remove(currentPatient);
				namePatients.removeAllElements();
				listNamePatient.removeAll(listNamePatient);
				for (int i = 0; i < Hopital.getPatients().size(); i++) {
					String string = Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getLastName()
							.toUpperCase() + " "
							+ Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getFirstName();
					namePatients.addElement(string);
					listNamePatient.add(string);
				}
				listPatient = new JList<>(namePatients);
				System.out.println(currentPatient.getLastName() + " a ete supprimer");
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		}
	}

	/**
	 * Permet de confirmer la modification du patient courant
	 */
	private class ConfirmModificationButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			String lastname = patientLastnameInputTextField.getText();
			String firstname = patientFirstnameInpuTextField.getText();
			String secuNumber = patientSecuNumberInputFormattedTextField.getText();
			String phoneNumber = patientNumberPhoneInputFormFormattedTextField.getText();
			String address = patientAddressInputTextField.getText();
			String birthday = patientDateInputFormattedTextField.getText();

			String lineToChange = ""
					+ currentPatient.getIdentifiant() + "&"
					+ currentPatient.getFirstName() + "&"
					+ currentPatient.getLastName().toUpperCase() + "&"
					+ currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE).replace("/", "-") + "&"
					+ currentPatient.getSecuNumber() + "&"
					+ currentPatient.getPhoneNumber() + "&"
					+ currentPatient.getAddress();

			String newLine = ""
					+ currentPatient.getIdentifiant() + "&"
					+ firstname + "&"
					+ lastname.toUpperCase() + "&"
					+ birthday.replace("/", "-").replace(" ", "") + "&"
					+ secuNumber + "&"
					+ phoneNumber + "&"
					+ address;
			System.out.println(lineToChange);
			System.out.println(newLine);

			File patientFile = new File("./src/log/patient/patients.txt");
			ArrayList<String> lines = new ArrayList<>();
			String line;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
						patientFile.getAbsolutePath()), "UTF-8"));
				while ((line = reader.readLine()) != null) {
					if (!line.equals(lineToChange))
						lines.add(line);
					else
						lines.add(newLine);
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (int i = 0; i < lines.size(); i++) {
				System.out.println(lines.get(i));
			}

			try {
				patientFile.delete();
				patientFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(
					patientFile.getAbsolutePath(), true), StandardCharsets.UTF_8)) {
				for (int i = 0; i < lines.size(); i++) {
					writer.write(lines.get(i) + "\n");
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Hopital.getPatients().removeAll(Hopital.getPatients());
			Hopital.loadingPatient();
			namePatients.removeAllElements();
			listNamePatient.removeAll(listNamePatient);
			for (int i = 0; i < Hopital.getPatients().size(); i++) {
				String string = Hopital.getPatients().get(Hopital.getPatients().size() - i - 1)
						.getLastName().toUpperCase() + " "
						+ Hopital.getPatients().get(Hopital.getPatients().size() - i - 1).getFirstName();
				namePatients.addElement(string);
				listNamePatient.add(string);
			}
			listPatient = new JList<>(namePatients);
			contentPanel.revalidate();
			contentPanel.repaint();
		}
	}
}