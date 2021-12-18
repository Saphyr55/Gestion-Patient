/**
 * 
 */
package windows.medecin;

import java.awt.GridLayout;
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
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import hopital.Hopital;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnection;

/**
 * @author Andy
 *
 */
public class FrameMedecin extends JFrame {

	/**
	 * ID
	 */
	private static final long serialVersionUID = -257774359093762865L;

	/*
	 * Charge de quoi charger les differents textes dans strings.json
	 */
	// private static Language language = FrameConnexion.getLanguage();
	private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();
	private static LoadingDimens dimens = new LoadingDimens();

	/**
	 * Variables d'options pour la fenetre de gestion
	 */
	private static final int width = (int) ((long) dimens.getJsonObject().get("frame_medecin_width"));
	private static final int height = (int) ((long) dimens.getJsonObject().get("frame_medecin_height"));
	private static final String title = (String) loadingLanguage.getJsonObject().get("frame_medecin_title");
	private static boolean isVisible = true;

	/**
	 * Tous les textes a charger
	 */
	private static final String frame_medecin_confirm_delete_patient = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_confirm_delete_patient");
	private static final String frame_medecin_delete = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_delete");
	private static final String frame_medecin_new_consultation = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_new_consultation");
	private static final String frame_medecin_popup_add = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_popup_add");
	private static final String frame_medecin_popup_delete = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_popup_delete");
	private static final String frame_medecin_popup_add_consultation = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_popup_add_consultation");
	private static final String frame_medecin_lastname = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_lastname");
	private static final String frame_medecin_firstname = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_firstname");
	private static final String frame_medecin_birthday = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_birthday");
	private static final String frame_medecin_age = (String) loadingLanguage.getJsonObject().get("frame_medecin_age");

	/**
	 * Composant de la frame
	 */
	private JPanel panelPrincipal = (JPanel) this.getContentPane();

	/**
	 * Composant du JPanel de liste de patient
	 */
	private JPanel panelListPatient;
	private JList<String> listPatient;
	private JScrollPane listPatientScrollPane;
	private JButton addPatient;
	private JTextField foundPatientField;
	private DefaultListModel<String> namePatients = new DefaultListModel<>();
	private ArrayList<String> listNamePatient = new ArrayList<>();
	private JPopupMenu popupMenuListPatient;
	private JMenuItem menuItemAddPatient, menuItemSupprPatient, menuItemAddConsultation;

	/**
	 * Composant des données du patient
	 */
	private PanelDataPatient panelDataPatient;
	private JPanel panelPatient, panelTop, panelBottom, panelData;
	private JLabel lastNamePatient, firstNamePatient, birthdayPatient, agePatient;

	private JTextArea consultationText;
	private JButton suppr;

	/**
	 * 
	 */
	private JPanel consultationPanel;
	private JList<String> listConsultationJList;
	private DefaultListModel<String> nameListConsultationDefaultModel = new DefaultListModel<>();
	private ArrayList<String> nameConsultationList = new ArrayList<>();
	private JScrollPane listConsultationScrollPane = new JScrollPane();
	private JTextField foundConsultationField;
	private JButton addConsultationButton;

	/**
	 * Popup menu pour la liste de patient
	 */
	private JPopupMenu consultationPopupMenu;
	private JMenuItem displayPrescriptionMenuItem, displayIRMMenuItem,
			displayRadiologyMenuItem, displayAvisMedicalMenuItem,
			displayDiagnosticsMenuItem, displaySurgeryMenuItem, addConsultationMenuItem, deleteConsultationMenuItem;

	/**
	 * Frame generer
	 */
	private static FrameConsultation frameConsultation;
	private static FrameAddPatientWithMedecin frameAddPatientWithMedecin;

	/**
	 * Gestion donnée frame
	 */
	private static Medecin currentMedecin = FrameConnection.getCurrentMedecin();
	private static Patient currentPatient;

	/**
	 * Fonts
	 */
	private static Font font1 = new Font("SansSerif", Font.BOLD, 20);

	/**
	 * Constructeur
	 */
	public FrameMedecin() {
		super(title);
		setOptionFrame();
		panelDataPatient = new PanelDataPatient();
		panelPrincipal.add(setListPatient(), BorderLayout.WEST);
		panelPrincipal.add(panelDataPatient, BorderLayout.CENTER);
		panelPrincipal.add(setListConsultion(), BorderLayout.EAST);
		setVisible(isVisible);
	}

	/**
	 * Option de la frame
	 */
	private void setOptionFrame() {
		try {
			UIManager.setLookAndFeel(FrameConnection.getModel());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
		this.setSize(width, height);
		// this.setMinimumSize(new Dimension(width, height));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Permet d'afficher la liste de patient,
	 * de pouvoir de rajouter des patients,
	 * de chercher un patient dans la liste
	 * 
	 * @return panelListPatient
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

		String namePatientString;
		for (int i = 0; i < currentMedecin.getPatients().size(); i++) {
			namePatientString = currentMedecin.getPatients().get(i).getLastName().toUpperCase() + " "
					+ currentMedecin.getPatients().get(i).getFirstName();
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
		panelListPatient.setPreferredSize(new Dimension(200, height));
		panelListPatient.setPreferredSize(new Dimension(200, height));
		panelListPatient.setMinimumSize(new Dimension(200, height));
		panelListPatient.setMaximumSize(new Dimension(200, height));
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
					currentPatient = currentMedecin.getPatients().get(index);

					/**
					 * Le clique gauche declenche la liste la de consultation et affiche les données
					 * du patient du patient selectionner
					 */
					if (SwingUtilities.isLeftMouseButton(event)) {
						setActionOnLeftClickOnListPatient(event);
					}

					/**
					 * Le clique droit declenche une popup permetant de supprimer ou ajouter un
					 * patient
					 */
					else if (SwingUtilities.isRightMouseButton(event)) {
						setActionOnRightClickListPatient(event);
					}
				}
			}
		});

		/**
		 * Trouve le patient ecrit
		 */
		foundPatientField.getDocument().addDocumentListener(new DocumentListenerPatientField());

		/**
		 * Affichage de la frame pour ajouter un patient lors du clique du bouton '+'
		 */
		addPatient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFrameAddPatientWithMedecin();
			}

		});

		return panelListPatient;
	}

	/**
	 * Action sur la liste de patient au clique droit
	 */
	private void setActionOnRightClickListPatient(MouseEvent event) {
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
							frame_medecin_confirm_delete_patient);

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
					setFrameAddPatientWithMedecin();
				}
			});

			menuItemAddConsultation.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setFrameConsultation();
				}
			});

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Action sur la liste de patient au clique gauche
	 */
	private void setActionOnLeftClickOnListPatient(MouseEvent event) {
		/**
		 * Affichie les données du patient
		 */
		panelDataPatient.getLastnamePatientTextField().setText(currentPatient.getLastName());
		panelDataPatient.getFirstnamePatientTextField().setText(currentPatient.getFirstName());
		panelDataPatient.getBirthdayPatientTextField()
				.setText(currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE));
		panelDataPatient.getSecuNumberPatientTextField().setText(currentPatient.getSecuNumber());
		panelDataPatient.getPhonePatientTextField().setText(currentPatient.getPhoneNumber());
		panelDataPatient.getAddressPatientTextField().setText(currentPatient.getAddress());

		/**
		 * Charge la liste de consultation
		 */
		loadingListConsultation(currentPatient);
		panelPrincipal.revalidate();
		panelPrincipal.repaint();
	}

	/**
	 * 
	 * 
	 * @return
	 */
	private JPopupMenu setPopupMenuOnRightClickListConsultationWithNoConsultation() {
		consultationPopupMenu = new JPopupMenu();
		addConsultationMenuItem = new JMenuItem("Ajouter consultation");
		consultationPopupMenu.add(addConsultationMenuItem);
		return consultationPopupMenu;
	}

	/**
	 * 
	 * @return
	 */
	private JPopupMenu setPopupMenuOnRightClickListConsultation() {
		consultationPopupMenu = new JPopupMenu();

		displayAvisMedicalMenuItem = new JMenuItem("Afficher l'avis medical");
		displayDiagnosticsMenuItem = new JMenuItem("Afficher le diagnostique");
		displayPrescriptionMenuItem = new JMenuItem("Afficher l'ordonnance");
		displayIRMMenuItem = new JMenuItem("Afficher le suivi IRM");
		displayRadiologyMenuItem = new JMenuItem("Afficher le suivi de Radiology");
		addConsultationMenuItem = new JMenuItem("Ajouter consultation");
		deleteConsultationMenuItem = new JMenuItem("Supprimer consultation");
		displaySurgeryMenuItem = new JMenuItem("Affichier le suivie de chirurgie");

		consultationPopupMenu.add(addConsultationMenuItem);
		consultationPopupMenu.add(deleteConsultationMenuItem);
		consultationPopupMenu.add(displayAvisMedicalMenuItem);
		consultationPopupMenu.add(displayPrescriptionMenuItem);
		consultationPopupMenu.add(displayDiagnosticsMenuItem);
		consultationPopupMenu.add(displayIRMMenuItem);
		consultationPopupMenu.add(displayRadiologyMenuItem);
		consultationPopupMenu.add(displaySurgeryMenuItem);

		return consultationPopupMenu;
	}

	/**
	 * Affiche la liste de consultation, le texte pour rechercher une consultation.
	 * Et un button pour ajouter une consultation
	 * 
	 * @return consultationPanel
	 */
	private JPanel setListConsultion() {

		/**
		 * Inititalisation des attribus pour la liste de patient
		 */
		consultationPanel = new JPanel(new BorderLayout());
		consultationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

		JPanel panelTop = new JPanel(new BorderLayout());
		addConsultationButton = new JButton("+");
		foundConsultationField = new JTextField();
		foundConsultationField.getDocument()
				.addDocumentListener(new DocumentListenerConsultationField());

		listConsultationJList = new JList<>(nameListConsultationDefaultModel);
		listConsultationScrollPane = new JScrollPane(listConsultationJList);

		/**
		 * Option text sur le button et autre
		 */
		addConsultationButton.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		foundConsultationField.setPreferredSize(new Dimension(0, 20));

		/**
		 * Dimensions
		 */
		consultationPanel.setPreferredSize(new Dimension(200, height));
		consultationPanel.setPreferredSize(new Dimension(200, height));
		consultationPanel.setMinimumSize(new Dimension(200, height));
		consultationPanel.setMaximumSize(new Dimension(200, height));
		panelTop.setPreferredSize(new Dimension(0, 40));
		foundConsultationField.setPreferredSize(new Dimension(0, 100));

		/**
		 * Ajout du button d'ajout de patient et de recherche de patient dans le
		 * panelTop
		 * Et ajout du panelTop et de la listPatient dans le panelListPatient
		 */
		panelTop.add(addConsultationButton, BorderLayout.WEST);
		panelTop.add(foundConsultationField, BorderLayout.CENTER);
		consultationPanel.add(panelTop, BorderLayout.NORTH);
		consultationPanel.add(listConsultationScrollPane, BorderLayout.CENTER);

		/*
		 * Action sur la liste d'ordonnance
		 */
		if (listConsultationJList != null) {
			listConsultationJList.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent event) {

					if (event.getClickCount() == 1) {
						if (SwingUtilities.isLeftMouseButton(event)) {

						} else if (SwingUtilities.isRightMouseButton(event)) {
							setActionOnRightClickOnListConsultation(event);
						}
					} else if (event.getClickCount() == 2) {

					}
				}
			});
		}

		return consultationPanel;
	}

	/**
	 * Methode appeller lors du clique droit de la liste de consultation
	 * Permet d'afficher un popup menu avec plusieurs usage
	 */
	private void setActionOnRightClickOnListConsultation(MouseEvent event) {
		if (currentPatient != null) {
			File[] consultationPatientFile = new File(
					"./src/log/patient/" + currentPatient.getFirstName() + currentPatient.getLastName() + "/")
							.listFiles();
			if (consultationPatientFile == null || consultationPatientFile.length == 0) {
				setPopupMenuOnRightClickListConsultationWithNoConsultation().show(
						event.getComponent(), event.getX(), event.getY());
			} else {
				setPopupMenuOnRightClickListConsultation().show(
						event.getComponent(), event.getX(), event.getY());
			}
		}
	}

	/**
	 * Methode appeller lors du clique gauche de la de consultation
	 * Permet d'afficher le panel de l'ordonnance
	 */
	private void setActionOnLeftClickOnListConsultation() {
		
	}

	/**
	 * Affiche la frame pour ajouter des consultations
	 * Ne peut etre afficher plusieur fois
	 */
	private void setFrameConsultation() {
		if (frameConsultation == null)
			frameConsultation = new FrameConsultation();

		frameConsultation.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				frameConsultation = null;
			}
		});
	}

	/**
	 * Affiche la frame pour ajouter des patient
	 * Ne peut etre afficher plusieur fois
	 */
	private void setFrameAddPatientWithMedecin() {
		if (frameAddPatientWithMedecin == null)
			frameAddPatientWithMedecin = new FrameAddPatientWithMedecin();

		frameAddPatientWithMedecin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				frameAddPatientWithMedecin = null;
			}
		});
	}

	/**
	 * Filtre de menu de recherche d'un patient
	 * 
	 * @param model
	 * @param filter
	 */
	void filterModel(DefaultListModel<String> model, String filter, List<String> list) {
		for (String string : list) {
			if (!string.startsWith(filter)) {
				if (model.contains(string)) {
					model.removeElement(string);
				}
			} else {
				if (!model.contains(string)) {
					model.addElement(string);
				}
			}
		}
	}

	/**
	 * Charge toutes les ordonnances du dossier du patient en parametre
	 * Puis ajoute le nom des fichier au x
	 * 
	 * @param patient
	 */
	private void loadingListConsultation(Patient patient) {

		/**
		 * Recuperation de toutes les consultations du patient
		 */
		Hopital.loadingConsultationPatient(patient);

		/**
		 * Si la liste est vide on la remplie
		 */
		if (nameListConsultationDefaultModel.isEmpty()) {
			for (int i = 0; i < patient.getConsultationFile().size(); i++) {

				/**
				 * Formate le nom du fichier de le consultation
				 */
				String nameConsultation = patient.getConsultationFile().get(i).getName()
						.replace("&", " ").replace(".txt", "");
				/**
				 * Ajout de tous les elements pour la JList dans
				 * nameListConsultationDefaultModel
				 * si model de la liste de onsultation ne containt pas deja l'élement
				 */
				if (!nameListConsultationDefaultModel.contains(nameConsultation)) {

					/**
					 * Met tous les noms des consultation dans le model et dans la liste
					 */
					nameListConsultationDefaultModel.addElement(nameConsultation);
					nameConsultationList.add(nameConsultation);
				}
			}

			/**
			 * Initiatlisation de la JList et le JScrollPane
			 */
			listConsultationJList = new JList<>(nameListConsultationDefaultModel);
			listConsultationScrollPane = new JScrollPane(listConsultationJList);
		}

		/**
		 * Si la liste n'est pas vide on la vide
		 * On reinitialise la jlist et jscrollpane
		 * Et on appelle la meme fonction
		 */
		else {
			nameListConsultationDefaultModel.removeAllElements();
			nameConsultationList.removeAll(nameConsultationList);
			listConsultationJList = new JList<>(nameListConsultationDefaultModel);
			listConsultationScrollPane = new JScrollPane(listConsultationJList);

			loadingListConsultation(patient);
		}
	}

	/**
	 * PopupMenu lors du click droit sur la liste de patient
	 * 
	 * @return popupMenu
	 */
	private JPopupMenu setPopupMenuOnRigthClickListPatient() {
		popupMenuListPatient = new JPopupMenu();
		menuItemAddPatient = new JMenuItem(frame_medecin_popup_add);
		menuItemSupprPatient = new JMenuItem(frame_medecin_popup_delete);
		menuItemAddConsultation = new JMenuItem(frame_medecin_popup_add_consultation);
		popupMenuListPatient.add(menuItemAddPatient);
		popupMenuListPatient.add(menuItemSupprPatient);
		popupMenuListPatient.add(menuItemAddConsultation);
		return popupMenuListPatient;
	}

	/**
	 * Lis une ordonnance et l'affiche au clique de la consultation
	 * 
	 * @param ordonnance
	 */
	private void readOrdonnance(File ordonnance) {
		BufferedReader reader;
		FileReader in;
		String line;
		try {
			if (ordonnance == null) {
				in = new FileReader("");
			} else {
				in = new FileReader(ordonnance.getAbsolutePath());
			}
			reader = new BufferedReader(in);
			do {
				line = reader.readLine();
				if (line != null)
					this.consultationText.setText(this.consultationText.getText() + line + "\n");
			} while (line != null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Le listener du text field pour chercher un patient
	 */
	public class DocumentListenerPatientField implements DocumentListener {

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
			filterModel((DefaultListModel<String>) listPatient.getModel(), filter, listNamePatient);
		}
	}

	/**
	 * Le listener du text field pour chercher une consultation
	 */
	public class DocumentListenerConsultationField implements DocumentListener {

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
			String filter = foundConsultationField.getText();
			filterModel((DefaultListModel<String>) listConsultationJList.getModel(), filter,
					nameConsultationList);
		}
	}

	/**
	 * Ferme la fenetre de consultation au clique du bouton annuler de la fenetre de
	 * consultation
	 */
	public static class ActionListenerCancelButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frameConsultation.dispose();
			frameConsultation = null;
		}
	}

	/*
	 * -----------------------------------------------
	 * Getters end setters
	 * -----------------------------------------------
	 */

	/**
	 * @return the listOrdonnance
	 */
	public JList<String> getListConsultation() {
		return listConsultationJList;
	}

	/**
	 * @return birthdayPatient
	 */
	public JLabel getBirthdayPatient() {
		return birthdayPatient;
	}

	/**
	 * @param birthdayPatient
	 */
	public void setBirthdayPatient(JLabel birthdayPatient) {
		this.birthdayPatient = birthdayPatient;
	}

	/**
	 * @return agePatient
	 */
	public JLabel getAgePatient() {
		return agePatient;
	}

	/**
	 * @param agePatient
	 */
	public void setAgePatient(JLabel agePatient) {
		this.agePatient = agePatient;
	}

	/**
	 * @return nameListConsultationDefaultModel
	 */
	public DefaultListModel<String> getNameListOrdonnance() {
		return nameListConsultationDefaultModel;
	}

	/**
	 * @param nameListOrdonnance
	 */
	public void setNameListOrdonnance(DefaultListModel<String> nameListOrdonnance) {
		this.nameListConsultationDefaultModel = nameListOrdonnance;
	}

	/**
	 * @return currentPatient
	 */
	public static Patient getSelectedPatient() {
		return currentPatient;
	}

	/**
	 * @return frameConsultation
	 */
	public static FrameConsultation getFrameConsultation() {
		return frameConsultation;
	}

	/**
	 * @return frameAddPatientWithMedecin
	 */
	public static FrameAddPatientWithMedecin getFrameAddPatientWithMedecin() {
		return frameAddPatientWithMedecin;
	}

}