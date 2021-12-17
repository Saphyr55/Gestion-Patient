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

import hopital.Consultation;
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
	private static final String frame_medecin_lastname = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_lastname");
	private static final String frame_medecin_firstname = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_firstname");
	private static final String frame_medecin_birthday = (String) loadingLanguage.getJsonObject()
			.get("frame_medecin_birthday");
	private static final String frame_medecin_age = (String) loadingLanguage.getJsonObject().get("frame_medecin_age");
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
	private JPopupMenu popupMenuListPatient;
	private JMenuItem menuItemAddPatient, menuItemSupprPatient, menuItemAddConsultation;
	private ArrayList<String> listNamePatient = new ArrayList<>();

	/**
	 * Composant des données du patient
	 */
	private JPanel panelPatient, panelTop, panelBottom, panelData;
	private JLabel lastNamePatient, firstNamePatient, birthdayPatient, agePatient;

	private JTextArea consultationText;
	private JButton suppr;

	/**
	 * Le panel central avec les données dus patient
	 */
	private JPanel dataPatientPanel;
	private JPanel patientStringDataPanel, switchTypeConsultationPanel;
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
	private JPanel consultationPanel;
	private JList<String> listConsultationJList;
	private DefaultListModel<String> nameListConsultationDefaultModel = new DefaultListModel<>();
	private JScrollPane listConsultationScrollPane = new JScrollPane();
	private JTextField foundConsultationField;
	private JButton addConsultationButton;

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
	 * Constructeur
	 */
	public FrameMedecin() {
		super(title);
		setOptionFrame();
		panelPrincipal.add(setListPatient(), BorderLayout.WEST);
		panelPrincipal.add(setPanelDataPatient(), BorderLayout.CENTER);
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
					 * Le clique gauche declenche la liste la de consultation du patient
					 * selectionner
					 */
					if (SwingUtilities.isLeftMouseButton(event)) {
						panelPrincipal.remove(panelPatient);
						loadingListConsultation(currentPatient);
						panelPrincipal.add(setPatient(currentPatient));
						panelPrincipal.revalidate();
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
	 * 
	 * @return dataPatientPanel
	 */
	private JPanel setPanelDataPatient() {
		dataPatientPanel = new JPanel(new GridLayout(1, 2));

		setPanelDataString();
		switchTypeConsultationPanel = new JPanel();
		testButtonForSwitch = new JButton("+");
		switchTypeConsultationPanel.add(testButtonForSwitch);
		switchTypeConsultationPanel.setPreferredSize(new Dimension(width / 3, height - 50));

		dataPatientPanel.add(patientStringDataPanel);
		dataPatientPanel.add(switchTypeConsultationPanel);

		return dataPatientPanel;
	}

	private void setPanelDataString() {
		/**
		 * 
		 */
		patientStringDataPanel = new JPanel();
		// nom
		lastnameStringTextField = new JTextField(frame_medecin_lastname);
		lastnamePatientTextField = new JTextField();
		// prenom
		firstnameStringTextField = new JTextField(frame_medecin_firstname);
		firstnamePatientTextField = new JTextField();
		// birthday
		birthdayStringTextField = new JTextField(frame_medecin_birthday);
		birthdayPatientTextField = new JFormattedTextField();
		// numero de securité social
		secuNumberStringTextField = new JTextField("Secu number");
		secuNumberPatientTextField = new JFormattedTextField();
		// numero de telephone
		phoneStringTextField = new JTextField("Phone number");
		phonePatientTextField = new JFormattedTextField();
		// adresse
		addressStringTextField = new JTextField("Address");
		addressPatientTextField = new JTextField();

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

		listConsultationJList = new JList<>();
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

		return consultationPanel;
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
				 * Ajout de tous les elements pour la JList dans
				 * nameListConsultationDefaultModel
				 * si model de la liste de onsultation ne containt pas deja l'élement
				 */
				if (!nameListConsultationDefaultModel.contains(
						patient.getConsultationFile().get(i).getName()
								.replace("&", " ").replace(".txt", ""))) {
					nameListConsultationDefaultModel.addElement(
							patient.getConsultationFile().get(i).getName()
									.replace("&", " ").replace(".txt", ""));
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
			listConsultationJList = new JList<>(nameListConsultationDefaultModel);
			listConsultationScrollPane = new JScrollPane(listConsultationJList);

			loadingListConsultation(patient);
		}
	}

	/**
	 * Permet d'afficher les metadonnees du patient
	 * selectionne lors de la liste de patient
	 * 
	 * @param patient
	 * @return panelPatient
	 */
	private JPanel setPatient(Patient patient) {

		/*
		 * Initialisation des composants
		 */
		panelPatient = new JPanel(new BorderLayout());

		/*
		 * Gestion date de naissance et calcul age
		 */
		LocalDate patientBirthday = patient.getBirthday();
		LocalDate today = LocalDate.now();
		int agePatientInteger = Period.between(patientBirthday, today).getYears();

		/*
		 * panel top
		 */
		panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 60, 10));
		lastNamePatient = new JLabel(frame_medecin_lastname + " : " + patient.getLastName());
		firstNamePatient = new JLabel(frame_medecin_firstname + " : " + patient.getFirstName());
		birthdayPatient = new JLabel(frame_medecin_birthday + " : " + patient.getBirthday());
		agePatient = new JLabel(frame_medecin_age + " : " + agePatientInteger);
		suppr = new JButton(frame_medecin_delete);
		// ajoutConsultation = new JButton(frame_medecin_new_consultation);

		/*
		 * panel bottom
		 */
		panelBottom = new JPanel(new BorderLayout());
		consultationText = new JTextArea();
		panelData = new JPanel();

		/*
		 * Option generales sur la liste d'ordonnance, le button supprimer un patient,
		 * le button ajouter un patient et nom, prenom, date de naissance du patient
		 * courant.
		 */
		consultationText.setEditable(false);
		listConsultationScrollPane.setPreferredSize(new Dimension(200, 0));
		// ajoutConsultation.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));
		lastNamePatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		firstNamePatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		birthdayPatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		agePatient.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		suppr.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 10));

		/*
		 * Ajout des composants du panel du haut
		 */
		panelTop.add(lastNamePatient);
		panelTop.add(firstNamePatient);
		panelTop.add(birthdayPatient);
		panelTop.add(agePatient);
		panelTop.add(suppr);

		/*
		 * Ajout des composants du panel du bas
		 */
		panelBottom.add(consultationText, BorderLayout.CENTER);
		panelBottom.add(panelData, BorderLayout.WEST);

		/*
		 * Ajout JPanel top et bottom au JPanel panelPatient
		 */
		panelPatient.add(panelTop, BorderLayout.NORTH);
		panelPatient.add(panelBottom, BorderLayout.CENTER);

		/*
		 * Action sur la liste d'ordonnance
		 */
		if (listConsultationJList != null) {
			listConsultationJList.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent event) {
					File ordonnance;
					if (event.getClickCount() == 1) {
						@SuppressWarnings("unchecked")
						JList<String> list = (JList<String>) event.getSource();

						if (SwingUtilities.isLeftMouseButton(event)) {
							int index = list.locationToIndex(event.getPoint());
							ordonnance = patient.getConsultationFile().get(index);
							consultationText.setText("");
							readOrdonnance(ordonnance);
							panelPrincipal.revalidate();
						} else if (SwingUtilities.isRightMouseButton(event)) {
							System.out.println("Click Right");
						}
					}
				}
			});
		}

		/*
		 * Lors du clique du button nouvelle consultation
		 * Affiche une fenetre avec les options de creation de consultation
		 */
		/*
		 * ajoutConsultation.addActionListener(new ActionListener() {
		 * 
		 * @Override
		 * public void actionPerformed(ActionEvent e) {
		 * if (currentPatient != null)
		 * setFrameConsultation();
		 * }
		 * });
		 */
		return panelPatient;
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
		Consultation ordo;
		try {
			if (ordonnance == null) {
				ordo = new Consultation();
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
	 * @return the listOrdonnance
	 */
	public JList<String> getListConsultation() {
		return listConsultationJList;
	}

	/**
	 * @return the birthdayPatient
	 */
	public JLabel getBirthdayPatient() {
		return birthdayPatient;
	}

	/**
	 * @param birthdayPatient the birthdayPatient to set
	 */
	public void setBirthdayPatient(JLabel birthdayPatient) {
		this.birthdayPatient = birthdayPatient;
	}

	/**
	 * @return the agePatient
	 */
	public JLabel getAgePatient() {
		return agePatient;
	}

	/**
	 * @param agePatient the agePatient to set
	 */
	public void setAgePatient(JLabel agePatient) {
		this.agePatient = agePatient;
	}

	/**
	 * @return nameListOrdonnance
	 */
	public DefaultListModel<String> getNameListOrdonnance() {
		return nameListConsultationDefaultModel;
	}

	/**
	 * @param nameListOrdonnance the nameListOrdonnance to set
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
			filterModel((DefaultListModel<String>) listPatient.getModel(), filter);
		}
	}

	/**
	 * Ferme la fenetre au clique du bouton annuler de la fenetre de consultation
	 */
	public static class ActionListenerCancelButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frameConsultation.dispose();
			frameConsultation = null;
		}
	}

}