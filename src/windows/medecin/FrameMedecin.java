/**
 * 
 */
package windows.medecin;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import javax.swing.text.MaskFormatter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import hopital.Consultation;
import hopital.Hopital;
import hopital.Consultation.WriteType;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnection;

/**
 * Frame du medecin permettant de creer des consultations et d'ajouter des
 * patients au medecin courant
 * 
 * @author Andy
 */
public class FrameMedecin extends JFrame {

	/**
	 * -------------------------------------------------------
	 * Données generales pour la frame
	 * -------------------------------------------------------
	 */

	/**
	 * 
	 */
	private static Patient currentPatient;
	private static int indexConsultationList;

	/*
	 * Charge de quoi charger les differents textes en differente langue dans
	 * strings.json
	 */
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
	 * Fonts
	 */
	private static Font font1 = new Font("SansSerif", Font.BOLD, 20);

	/**
	 * --------------------------------------------------------------
	 * Tous les composant de la frame
	 * --------------------------------------------------------------
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
	 * Panel de consultation
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
	private JMenuItem displayPrescriptionMenuItem, displayIRMMenuItem, displayAppariellageMenuItem,
			displayRadiologyMenuItem, displayAvisMedicalMenuItem,
			displayDiagnosticsMenuItem, displaySurgeryMenuItem, addConsultationMenuItem, deleteConsultationMenuItem;

	/**
	 * panel des données du patient
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

	/**
	 * Ordonnance panel
	 */
	private JPanel ordonnancePanel;
	private JLabel ordonnanceStringLabel;
	private JTextArea ordonnanceTextArea;
	private JScrollPane ordonnanceTextAreaPane;
	private BufferedReader readerOrdonnance;

	/**
	 * Avis medical panel
	 */
	private JPanel avisMedicalPanel;
	private JLabel avisMedicalLabel;
	private JTextArea avisMedicalTextArea;
	private JScrollPane avisMedicalTextAreaPane;
	private BufferedReader readerAvisMedical;

	/**
	 * Diagnostic panel
	 */
	private JPanel diagnosticPanel;
	private JLabel diagnosticLabel;
	private JTextArea diagnosticTextArea;
	private JScrollPane diagnosticTextAreaPane;
	private BufferedReader readerDiagnostic;

	/**
	 * Diagnostic panel
	 */
	private JPanel appariellagePanel;
	private JLabel appariellageLabel;
	private JTextArea appariellageTextArea;
	private JScrollPane appariellageTextAreaPane;
	private BufferedReader readerAppariellage;

	/**
	 * Frame generer par la Frame du medecin
	 */
	private static FrameConsultation frameConsultation;
	private static FrameMedecinAddPatient frameAddPatientWithMedecin;

	/**
	 * Donnée de l'hopital
	 */
	private static Medecin currentMedecin = FrameConnection.getCurrentMedecin();
	private static File consultationSwitchFileCurrentPatient;
	private static File consultationFileCurrentPatient;
	private static File avismedicalFileCurrentPatient;
	private static File prescriptionFileCurrentPatient;
	private static File diagnosticFileCurrentPatient;
	private static File appariellageFileCurrentPatient;
	private static MaskFormatter dateFormatter;
	private static MaskFormatter secuNumbeFormatter;
	private static MaskFormatter phoneNumberFormatter;
	private static Map<String, Boolean> appariellagesRequest;

	/**
	 * --------------------
	 * Panel personnalisé
	 * --------------------
	 */
	private JPanel switchTypeConsultationPanel;

	/**
	 * --------------
	 * Constructeur
	 * --------------
	 */
	public FrameMedecin() {
		super(title);
		setOptionFrame();
		panelPrincipal.add(setListPatient(), BorderLayout.WEST);
		panelPrincipal.add(setPanelDataPatient(null), BorderLayout.CENTER);
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
			namePatientString = currentMedecin.getPatients().get(currentMedecin.getPatients().size() - 1 - i)
					.getLastName().toUpperCase() + " "
					+ currentMedecin.getPatients().get(currentMedecin.getPatients().size() - 1 - i).getFirstName();
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
				if (index >= 0) {
					if (event.getClickCount() == 1) {
						// le patient courant sur l'index recuperer lors du clique
						currentPatient = currentMedecin.getPatients()
								.get(currentMedecin.getPatients().size() - 1 - index);

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
			menuItemSupprPatient.addActionListener(new MenuItemSupprPatientListener());

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
	public void setActionOnLeftClickOnListPatient(MouseEvent event) {
		/**
		 * Affichie les données du patient
		 */
		if (currentPatient != null) {

			if (switchTypeConsultationPanel != null) {
				dataPatientPanel.remove(switchTypeConsultationPanel);
				dataPatientPanel.add(setSwitchTypeConsultationPanel(new JPanel()));
			}
			firstnamePatientTextField.setText(currentPatient.getFirstName());
			lastnamePatientTextField.setText(currentPatient.getLastName());
			birthdayPatientTextField
					.setText(currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE).replace("-", ""));
			secuNumberPatientTextField.setText(currentPatient.getSecuNumber());
			phonePatientTextField.setText(currentPatient.getPhoneNumber());
			addressPatientTextField.setText(currentPatient.getAddress());

			/**
			 * Charge la liste de consultation
			 */
			loadingListConsultation(currentPatient);
			panelPrincipal.revalidate();
			panelPrincipal.repaint();
		}
	}

	/**
	 * Affiche une popup au clique droit d'une liste de consultation VIDE
	 * Popup affichant seulement ajouter une consultation
	 * 
	 * @return consultationPopupMenu
	 */
	private JPopupMenu setPopupMenuOnRightClickListConsultationWithNoConsultation() {
		consultationPopupMenu = new JPopupMenu();
		addConsultationMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_add_consultation"));
		addConsultationMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPatient != null) {
					setFrameConsultation();
				}
			}
		});
		consultationPopupMenu.add(addConsultationMenuItem);
		return consultationPopupMenu;
	}

	/**
	 * Affiche une popup au clique droit de la liste de consultation du patient
	 * courant
	 * Popup affichant ; ajouter une consultation, suppression de la consultation et
	 * autre par rapport au consultation.
	 * 
	 * @return consultationPopupMenu
	 */
	private JPopupMenu setPopupMenuOnRightClickListConsultation() {
		consultationPopupMenu = new JPopupMenu();

		addConsultationMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_add_consultation"));
		deleteConsultationMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_delete_consultation"));
		displayAvisMedicalMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_show_medical_advice"));
		displayDiagnosticsMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_show_diagnostics"));
		displayPrescriptionMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_show_prescription"));
		displayAppariellageMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_show_appareillages"));

		/**
		 * Les 3 ci-dessous sont non fonctionnels, ils sont juste là pour etre logique
		 * avec la frame d'ajout de consultation et pour supposition d'ajout de nouvelle
		 * fonctionnalité sur les consultations
		 */
		displayIRMMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_show_irm"));
		displayRadiologyMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_show_radiology"));
		displaySurgeryMenuItem = new JMenuItem(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_popup_show_surgery"));

		// ajout de tous les menus items
		consultationPopupMenu.add(addConsultationMenuItem);
		consultationPopupMenu.add(displayAvisMedicalMenuItem);
		consultationPopupMenu.add(displayDiagnosticsMenuItem);
		consultationPopupMenu.add(displayPrescriptionMenuItem);
		consultationPopupMenu.add(displayAppariellageMenuItem);
		consultationPopupMenu.add(displayIRMMenuItem);
		consultationPopupMenu.add(displayRadiologyMenuItem);
		consultationPopupMenu.add(displaySurgeryMenuItem);
		consultationPopupMenu.add(deleteConsultationMenuItem);

		// ajout des listeners
		addConsultationMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPatient != null) {
					setFrameConsultation();
				}
			}
		});
		deleteConsultationMenuItem.addActionListener(new DeleteConsultationMenuItemListener());
		displayPrescriptionMenuItem.addActionListener(new DisplayPrescriptionMenuItemListener());
		displayAvisMedicalMenuItem.addActionListener(new DisplayAvisMedicalMenuItemListener());
		displayDiagnosticsMenuItem.addActionListener(new DisplayDiagnosticMenuItemListener());
		displayAppariellageMenuItem.addActionListener(new DisplayApperiellageMenuItemListener());

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
		addConsultationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPatient != null) {
					setFrameConsultation();
				}
			}
		});
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

		/**
		 * Action sur la liste d'ordonnance
		 */
		if (listConsultationJList != null) {
			listConsultationJList.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent event) {
					JList<String> list = (JList<String>) event.getSource();
					indexConsultationList = list.locationToIndex(event.getPoint());
					if (event.getClickCount() == 1) {
						if (SwingUtilities.isLeftMouseButton(event)) {
							setActionOnLeftClickOnListConsultation();
						} else if (SwingUtilities.isRightMouseButton(event)) {
							setActionOnRightClickOnListConsultation(event);
						}
					} else if (event.getClickCount() == 2) {

					}
				}
			});
		}

		/**
		 * Lance la frame d'ajout de consultation lors du clique du boutton ajouter
		 */
		addConsultationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPatient != null)
					setFrameConsultation();
			}
		});

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
	public void setActionOnLeftClickOnListConsultation() {
		if (currentPatient != null && currentPatient.getConsultationsFile() != null &&
				!currentPatient.getConsultationsFile().isEmpty()) {
			if (indexConsultationList >= 0) {
				consultationFileCurrentPatient = currentPatient.getConsultationsFile()
						.get(currentPatient.getConsultationsFile().size() - 1 - indexConsultationList);
				avismedicalFileCurrentPatient = new File(consultationFileCurrentPatient.toPath() + "/avismedical/"
						+ consultationFileCurrentPatient.getName() + ".txt");
				dataPatientPanel.remove(switchTypeConsultationPanel);
				switchTypeConsultationPanel = setSwitchTypeConsultationPanel(
						setAvisMedicalPanel(avismedicalFileCurrentPatient));
				dataPatientPanel.add(switchTypeConsultationPanel);
				panelPrincipal.revalidate();
				panelPrincipal.repaint();
			}
		}
	}

	/**
	 * Affiche la frame pour ajouter des consultations
	 * Ne peut pas etre afficher plusieur fois
	 */
	private void setFrameConsultation() {
		if (frameConsultation == null) {
			frameConsultation = new FrameConsultation();
			frameConsultation.getCancelButton().addActionListener(new CancelButtonFrameConsultationListener());
		}
		frameConsultation.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				frameConsultation = null;
				panelPrincipal.revalidate();
				panelPrincipal.repaint();
			}
		});
	}

	/**
	 * Affiche la frame pour ajouter des patient
	 * Ne peut pas etre afficher plusieur fois
	 */
	private void setFrameAddPatientWithMedecin() {
		if (frameAddPatientWithMedecin == null) {
			frameAddPatientWithMedecin = new FrameMedecinAddPatient();
			frameAddPatientWithMedecin.getCancelButton().addActionListener(new CancelButtonFrameAddPatientListener());

		}
		frameAddPatientWithMedecin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				Hopital.getPatients().removeAll(Hopital.getPatients());
				frameAddPatientWithMedecin = null;

				namePatients.removeAllElements();
				listNamePatient.removeAll(listNamePatient);
				for (int i = 0; i < currentMedecin.getPatients().size(); i++) {
					String namePatientString = currentMedecin.getPatients()
							.get(currentMedecin.getPatients().size() - 1 - i).getLastName().toUpperCase() + " "
							+ currentMedecin.getPatients().get(currentMedecin.getPatients().size() - 1 - i)
									.getFirstName();
					namePatients.addElement(namePatientString);
					listNamePatient.add(namePatientString);
				}
				listPatient = new JList<>(namePatients);
				panelPrincipal.revalidate();
				panelPrincipal.repaint();
			}
		});
	}

	/**
	 * Filtre de menu de recherche d'une jlist
	 * 
	 * @param model
	 * @param filter
	 */
	private void filterModel(DefaultListModel<String> model, String filter, List<String> list) {
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
			for (int i = 0; i < patient.getConsultationsFile().size(); i++) {

				/**
				 * Formate le nom du fichier de le consultation
				 */
				String nameConsultation = patient.getConsultationsFile().get(patient
						.getConsultationsFile().size() - 1 - i).getName()
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
		menuItemAddPatient = new JMenuItem((String) loadingLanguage.getJsonObject()
				.get("frame_medecin_popup_add"));
		menuItemSupprPatient = new JMenuItem((String) loadingLanguage.getJsonObject()
				.get("frame_medecin_popup_delete"));
		menuItemAddConsultation = new JMenuItem((String) loadingLanguage.getJsonObject()
				.get("frame_medecin_popup_add_consultation"));
		popupMenuListPatient.add(menuItemAddPatient);
		popupMenuListPatient.add(menuItemSupprPatient);
		popupMenuListPatient.add(menuItemAddConsultation);
		return popupMenuListPatient;
	}

	/**
	 * Permet d'afficher le panel centrale avec les données du patient
	 * le switch panel
	 * 
	 * @return dataPatientPanel
	 */
	private JPanel setPanelDataPatient(JPanel panel) {
		dataPatientPanel = new JPanel(new GridLayout(1, 2));

		dataPatientPanel.add(setPanelDataString());
		dataPatientPanel.add(setSwitchTypeConsultationPanel(panel));

		return dataPatientPanel;
	}

	/**
	 * Permet changer le panel entre la liste de consultation et les données du
	 * patient par le panel rentrer en parametre
	 * 
	 * @param panel
	 * @return switchTypeConsultationPanel
	 */
	private JPanel setSwitchTypeConsultationPanel(JPanel panel) {
		if (panel != null) {
			switchTypeConsultationPanel = panel;
		} else {
			switchTypeConsultationPanel = new JPanel();
		}
		switchTypeConsultationPanel.setPreferredSize(new Dimension(width / 3, height - 50));
		return switchTypeConsultationPanel;
	}

	/**
	 * Permet de creer le panel des données du patient
	 * 
	 * @return patientStringDataPanel
	 */
	private JPanel setPanelDataString() {
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

		patientStringDataPanel = new JPanel();
		// nom
		lastnameStringTextField = new JTextField((String) loadingLanguage.getJsonObject()
				.get("frame_medecin_lastname"));
		lastnamePatientTextField = new JTextField();
		lastnameStringTextField.setFont(font1);
		lastnamePatientTextField.setFont(font1);

		// prenom
		firstnameStringTextField = new JTextField((String) loadingLanguage.getJsonObject()
				.get("frame_medecin_firstname"));
		firstnamePatientTextField = new JTextField();
		firstnameStringTextField.setFont(font1);
		firstnamePatientTextField.setFont(font1);

		// birthday
		birthdayStringTextField = new JTextField((String) loadingLanguage.getJsonObject()
				.get("frame_medecin_birthday"));
		birthdayPatientTextField = new JFormattedTextField(dateFormatter);
		birthdayStringTextField.setFont(font1);
		birthdayPatientTextField.setFont(font1);

		// numero de securité social
		secuNumberStringTextField = new JTextField(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_secu_number"));
		secuNumberPatientTextField = new JFormattedTextField(secuNumbeFormatter);
		secuNumberStringTextField.setFont(font1);
		secuNumberPatientTextField.setFont(font1);

		// numero de telephone
		phoneStringTextField = new JTextField(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_phone_number"));
		phonePatientTextField = new JFormattedTextField(phoneNumberFormatter);
		phoneStringTextField.setFont(font1);
		phonePatientTextField.setFont(font1);

		// adresse
		addressStringTextField = new JTextField(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_address"));
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

		return patientStringDataPanel;
	}

	/**
	 * Methode pour creer le panel de l'ordonnance
	 * 
	 * @return ordonnancePanel
	 */
	private JPanel setOrdonnancePanel(File file) {
		ordonnancePanel = new JPanel(new BorderLayout());
		ordonnanceStringLabel = new JLabel(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_prescription"));
		ordonnanceStringLabel.setFont(font1);
		ordonnanceTextArea = new JTextArea();
		ordonnanceTextArea.setFont(font1);
		ordonnanceTextAreaPane = new JScrollPane(ordonnanceTextArea);
		ordonnanceTextArea.setEditable(false);

		if (file != null) {
			try {
				String line;
				readerOrdonnance = new BufferedReader(new InputStreamReader(new FileInputStream(file),
						LoadingLanguage.encoding));

				while ((line = readerOrdonnance.readLine()) != null) {
					ordonnanceTextArea.append(line + "\n");
				}
				readerOrdonnance.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ordonnanceTextArea.setFont(new Font("SansSerif", Font.BOLD, 16));
		ordonnancePanel.add(ordonnanceStringLabel, BorderLayout.NORTH);
		ordonnancePanel.add(ordonnanceTextAreaPane, BorderLayout.CENTER);

		return ordonnancePanel;
	}

	/**
	 * Methode pour creer le panel de l'avis medical
	 * 
	 * @param file
	 * @return avisMedicalPanel
	 */
	private JPanel setAvisMedicalPanel(File file) {

		avisMedicalPanel = new JPanel(new BorderLayout());
		avisMedicalLabel = new JLabel(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_medical_advice"));
		avisMedicalLabel.setFont(font1);
		avisMedicalTextArea = new JTextArea();
		avisMedicalTextAreaPane = new JScrollPane(avisMedicalTextArea);
		avisMedicalTextArea.setEditable(false);

		if (file != null) {
			try {
				String line;
				readerAvisMedical = new BufferedReader(
						new InputStreamReader(new FileInputStream(file), LoadingLanguage.encoding));

				while ((line = readerAvisMedical.readLine()) != null) {
					avisMedicalTextArea.append(line + "\n");
				}
				readerAvisMedical.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		avisMedicalTextArea.setFont(new Font("SansSerif", Font.BOLD, 16));
		avisMedicalPanel.add(avisMedicalLabel, BorderLayout.NORTH);
		avisMedicalPanel.add(avisMedicalTextAreaPane, BorderLayout.CENTER);

		return avisMedicalPanel;
	}

	/**
	 * Methode pour creer le panel de diagnostique
	 * 
	 * @param file
	 * @return diagnosticPanel
	 */
	private JPanel setDiagnosticPanel(File file) {

		diagnosticPanel = new JPanel(new BorderLayout());
		diagnosticLabel = new JLabel(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_diagnostic"));
		diagnosticLabel.setFont(font1);
		diagnosticTextArea = new JTextArea();
		diagnosticTextAreaPane = new JScrollPane(diagnosticTextArea);
		diagnosticTextArea.setEditable(false);

		if (file != null) {
			try {
				String line;
				readerDiagnostic = new BufferedReader(
						new InputStreamReader(new FileInputStream(file), LoadingLanguage.encoding));

				while ((line = readerDiagnostic.readLine()) != null) {
					diagnosticTextArea.append(line + "\n");
				}
				readerDiagnostic.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		diagnosticTextArea.setFont(new Font("SansSerif", Font.BOLD, 16));
		diagnosticPanel.add(diagnosticLabel, BorderLayout.NORTH);
		diagnosticPanel.add(diagnosticTextAreaPane, BorderLayout.CENTER);

		return diagnosticPanel;
	}

	/**
	 * Affiche le panel d'appapiellage de la consultation
	 * 
	 * @param file
	 * @return diagnosticPanel
	 */
	private JPanel setAppareillagePanel(File file) {
		appariellagePanel = new JPanel(new BorderLayout());
		appariellageLabel = new JLabel(
				(String) loadingLanguage.getJsonObject().get("frame_medecin_appareillage"));
		appariellageLabel.setFont(font1);
		appariellageTextArea = new JTextArea();
		appariellageTextAreaPane = new JScrollPane(appariellageTextArea);
		appariellageTextArea.setEditable(false);
		JSONParser parser = new JSONParser();

		if (file != null) {
			try {
				readerAppariellage = new BufferedReader(
						new InputStreamReader(new FileInputStream(file), LoadingLanguage.encoding));
				JSONObject object = (JSONObject) parser.parse(readerAppariellage);
				HashMap<String, Boolean> appareillageHashMap = (HashMap<String, Boolean>) object.clone();
				for (Entry<String, Boolean> element : appareillageHashMap.entrySet()) {
					if (element.getValue() == false)
						appariellageTextArea.append(element.getKey() + " : "
								+ (String) loadingLanguage.getJsonObject().get("frame_medecin_appareillage_on_hold")
								+ "\n");
					else
						appariellageTextArea.append(element.getKey() + " : " +
								(String) loadingLanguage.getJsonObject().get("frame_medecin_appareillage_granted")
								+ "\n");
				}
				readerAppariellage.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
		}

		appariellageTextArea.setFont(new Font("SansSerif", Font.BOLD, 16));
		appariellagePanel.add(appariellageLabel, BorderLayout.NORTH);
		appariellagePanel.add(appariellageTextAreaPane, BorderLayout.CENTER);

		return appariellagePanel;
	}

	/**
	 * -------------------------------------------------------
	 * Les Principaux Listeners
	 * -------------------------------------------------------
	 */

	/**
	 * Creation de la consultation en recuperant toutes le données saisie lors
	 * de la frame de creation de consultation
	 */
	public class ConfirmButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			List<String> avisMedicalLineList = Arrays
					.asList(frameConsultation.getAvisMedicaltextArea().getText().split("\n"));
			String prescriptionLineList = frameConsultation.getPrescriptionTextArea().getText().replace("\n", " ");
			appariellagesRequest = new HashMap<>();
			for (int i = 0; i < frameConsultation.getAppariellageAlreadyAddArrayList().size(); i++) {
				appariellagesRequest.put(frameConsultation.getAppariellageAlreadyAddArrayList().get(i), false);
			}
			List<String> diagnosticList = Arrays
					.asList(frameConsultation.getDiagnosticTextArea().getText().split("\n"));

			/**
			 * Verification des entrées
			 */
			if (prescriptionLineList.equals("") ||
					prescriptionLineList.equals(" "))
				prescriptionLineList = null;

			if (appariellagesRequest.isEmpty())
				appariellagesRequest = null;

			if (diagnosticList.isEmpty() ||
					frameConsultation.getAvisMedicaltextArea().getText().equals("")) {
				diagnosticList = null;
			}

			/**
			 * Creation de la consultation
			 */
			if (!avisMedicalLineList.isEmpty() ||
					!frameConsultation.getAvisMedicaltextArea().getText().equals("")) {
				new Consultation(currentMedecin, currentPatient, prescriptionLineList,
						avisMedicalLineList, diagnosticList,
						appariellagesRequest, WriteType.WRITE_IN_ORDONNANCE);
				FrameMedecin.getFrameConsultation().dispose();
				FrameMedecin.setFrameConsultation(null);
				setActionOnLeftClickOnListPatient(null);
				setActionOnLeftClickOnListConsultation();
			} else // non fonctionnel
				JOptionPane.showMessageDialog(frameConsultation.getContentPane(), "L'avis médical doit etre remplie");
		}
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
			filterModel((DefaultListModel<String>) listPatient.getModel(), filter, listNamePatient);
		}
	}

	/**
	 * Le listener du text field pour chercher une consultation
	 */
	private class DocumentListenerConsultationField implements DocumentListener {

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
	 * Listener du button supprimer un patient.
	 * Lance une frame de confimation de suppression
	 */
	private class MenuItemSupprPatientListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int input = JOptionPane.showConfirmDialog(null,
					(String) loadingLanguage.getJsonObject()
							.get("frame_medecin_confirm_delete_patient"));

			/*
			 * 
			 */
			if (input == JOptionPane.YES_OPTION) {
				String lineToRemove = ""
						+ currentPatient.getIdentifiant() + "&"
						+ currentPatient.getFirstName() + "&"
						+ currentPatient.getLastName().toUpperCase() + "&"
						+ currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE).replace("/", "-") + "&"
						+ currentPatient.getSecuNumber() + "&"
						+ currentPatient.getPhoneNumber() + "&"
						+ currentPatient.getAddress();

				File filePatients = new File("./src/log/medecin/" + currentMedecin.getFirstName().toLowerCase()
						+ currentMedecin.getLastName().toLowerCase() + "/patients.txt");

				List<String> lines = new ArrayList<>();
				String line;
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(filePatients)))) {
					while ((line = reader.readLine()) != null) {
						if (!(line.equals(lineToRemove))) {
							lines.add(line);
						} else
							continue;
					}
					reader.close();
					if (filePatients.delete())
						System.out.println("Suppression du fichier patient réussi");
					if (filePatients.createNewFile())
						System.out.println("Recreation du fichier patient réussi");
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePatients, true),
						StandardCharsets.UTF_8)) {
					for (int i = 0; i < lines.size(); i++) {
						writer.write(lines.get(i) + "\n");
					}
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				currentMedecin.getPatients().remove(currentPatient);
				listNamePatient.removeAll(listNamePatient);
				namePatients.removeAllElements();
				for (int i = 0; i < currentMedecin.getPatients().size(); i++) {
					String lastname = currentMedecin.getPatients().get(i).getLastName().toUpperCase();
					String firstname = currentMedecin.getPatients().get(i).getFirstName();
					listNamePatient.add(lastname + " " + firstname);
					namePatients.addElement(lastname + " " + firstname);
				}
				listPatient = new JList<>(namePatients);
				panelPrincipal.revalidate();
				panelPrincipal.repaint();

				System.out.println(currentPatient.getLastName() + " a ete supprimer");
			}
		}
	}

	/**
	 * Ferme la fenetre de consultation au clique du bouton annuler de la fenetre de
	 * consultation puis recharge la liste de consultation du patient courant
	 */
	private class CancelButtonFrameConsultationListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			FrameMedecin.getFrameConsultation().dispose();
			FrameMedecin.setFrameConsultation(null);

			/**
			 * Enleve tous les consultations du model de la jlist
			 * Puis la recharge
			 */
			nameListConsultationDefaultModel.removeAllElements();
			nameConsultationList.removeAll(nameConsultationList);
			for (int i = 0; i < currentPatient.getConsultationsFile().size(); i++) {

				/**
				 * Formate le nom du fichier de le consultation
				 */
				String nameConsultation = currentPatient.getConsultationsFile().get(i).getName()
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
			panelPrincipal.revalidate();
			panelPrincipal.repaint();
		}
	}

	/**
	 * Ferme la fenetre d'ajout de patient au clique du bouton annuler de la fenetre
	 * d'ajout de patient puis refresh la liste des patients du medecin
	 */
	private class CancelButtonFrameAddPatientListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			FrameMedecin.getFrameAddPatientWithMedecin().dispose();
			FrameMedecin.setFrameAddPatientWithMedecin(null);
			Hopital.getPatients().removeAll(Hopital.getPatients());

			namePatients.removeAllElements();
			listNamePatient.removeAll(listNamePatient);
			for (int i = 0; i < currentMedecin.getPatients().size(); i++) {
				String namePatientString = currentMedecin.getPatients()
						.get(currentMedecin.getPatients().size() - 1 - i).getLastName().toUpperCase() + " "
						+ currentMedecin.getPatients().get(currentMedecin.getPatients().size() - 1 - i)
								.getFirstName();
				namePatients.addElement(namePatientString);
				listNamePatient.add(namePatientString);
			}
			listPatient = new JList<>(namePatients);
			panelPrincipal.revalidate();
			panelPrincipal.repaint();
		}
	}

	/**
	 * Affiche le panel de l'avis medical
	 */
	private class DisplayAvisMedicalMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			consultationFileCurrentPatient = currentPatient.getConsultationsFile().get(
					currentPatient.getConsultationsFile().size() - 1 - indexConsultationList);
			avismedicalFileCurrentPatient = new File(consultationFileCurrentPatient.toPath() + "/avismedical/"
					+ consultationFileCurrentPatient.getName() + ".txt");
			if (avismedicalFileCurrentPatient.exists()) {
				dataPatientPanel.remove(switchTypeConsultationPanel);
				switchTypeConsultationPanel = setSwitchTypeConsultationPanel(setAvisMedicalPanel(
						avismedicalFileCurrentPatient));
				dataPatientPanel.add(switchTypeConsultationPanel);
				panelPrincipal.revalidate();
				panelPrincipal.repaint();
			} else
				JOptionPane.showMessageDialog(panelPrincipal,
						(String) loadingLanguage.getJsonObject().get("frame_medecin_medical_advice") + " " +
								(String) loadingLanguage.getJsonObject().get("frame_medecin_not_exist"));
		}

	}

	/**
	 * Affiche le panel d'ordonnance
	 */
	private class DisplayPrescriptionMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			consultationFileCurrentPatient = currentPatient.getConsultationsFile().get(
					currentPatient.getConsultationsFile().size() - 1 - indexConsultationList);
			prescriptionFileCurrentPatient = new File(consultationFileCurrentPatient.toPath() + "/ordonnance/"
					+ consultationFileCurrentPatient.getName() + ".txt");
			if (prescriptionFileCurrentPatient.exists()) {
				dataPatientPanel.remove(switchTypeConsultationPanel);
				switchTypeConsultationPanel = setSwitchTypeConsultationPanel(setOrdonnancePanel(
						prescriptionFileCurrentPatient));
				dataPatientPanel.add(switchTypeConsultationPanel);
				panelPrincipal.revalidate();
				panelPrincipal.repaint();
			} else
				JOptionPane.showMessageDialog(panelPrincipal,
						(String) loadingLanguage.getJsonObject().get("frame_medecin_prescription") + " " +
								(String) loadingLanguage.getJsonObject().get("frame_medecin_not_exist"));
		}

	}

	/**
	 * Affiche le panel de diagnostique
	 */
	private class DisplayDiagnosticMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			consultationFileCurrentPatient = currentPatient.getConsultationsFile().get(
					currentPatient.getConsultationsFile().size() - 1 - indexConsultationList);
			diagnosticFileCurrentPatient = new File(consultationFileCurrentPatient.toPath() + "/diagnostic/"
					+ consultationFileCurrentPatient.getName() + ".txt");
			if (diagnosticFileCurrentPatient.exists()) {
				dataPatientPanel.remove(switchTypeConsultationPanel);
				switchTypeConsultationPanel = setSwitchTypeConsultationPanel(setDiagnosticPanel(
						diagnosticFileCurrentPatient));
				dataPatientPanel.add(switchTypeConsultationPanel);
				panelPrincipal.revalidate();
				panelPrincipal.repaint();
			} else
				JOptionPane.showMessageDialog(panelPrincipal,
						(String) loadingLanguage.getJsonObject().get("frame_medecin_diagnostic") + " " +
								(String) loadingLanguage.getJsonObject().get("frame_medecin_not_exist"));
		}
	}

	/**
	 * Affiche le panel de d'appariellage
	 */
	private class DisplayApperiellageMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			consultationFileCurrentPatient = currentPatient.getConsultationsFile()
					.get(currentPatient.getConsultationsFile().size() - 1 - indexConsultationList);
			appariellageFileCurrentPatient = new File(consultationFileCurrentPatient.toPath() + "/appareillage/"
					+ consultationFileCurrentPatient.getName() + ".json");
			if (appariellageFileCurrentPatient.exists()) {
				dataPatientPanel.remove(switchTypeConsultationPanel);
				switchTypeConsultationPanel = setSwitchTypeConsultationPanel(
						setAppareillagePanel(
								appariellageFileCurrentPatient));
				dataPatientPanel.add(switchTypeConsultationPanel);
				panelPrincipal.revalidate();
				panelPrincipal.repaint();
			} else
				JOptionPane.showMessageDialog(panelPrincipal,
						(String) loadingLanguage.getJsonObject().get("frame_medecin_appareillage") + " " +
								(String) loadingLanguage.getJsonObject().get("frame_medecin_not_exist"));
		}

	}

	/**
	 * Permet de supprimer une consultation
	 */
	private class DeleteConsultationMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int input = JOptionPane.showConfirmDialog(null,
					(String) loadingLanguage.getJsonObject().get("frame_medecin_confirm_delete_consultation"));
			if (input == JOptionPane.YES_OPTION) {

				consultationFileCurrentPatient = currentPatient.getConsultationsFile().get(
						currentPatient.getConsultationsFile().size() - 1 - indexConsultationList);

				String[] nameFolder = { "/appareillage/", "/diagnostic/", "/ordonnance/", "/avismedical/" };
				File file;
				File folder;

				/**
				 * Tous les fichier textes et dossiers a supprimer
				 */
				for (int i = 0; i < nameFolder.length; i++) {
					if (!nameFolder[i].equals(nameFolder[0])) {
						file = new File(consultationFileCurrentPatient.toPath() + nameFolder[i]
								+ consultationFileCurrentPatient.getName() + ".txt");
						folder = new File(consultationFileCurrentPatient.toPath() + nameFolder[i]);
					} else {
						file = new File(consultationFileCurrentPatient.toPath() + nameFolder[i]
								+ consultationFileCurrentPatient.getName() + ".json");
						folder = new File(consultationFileCurrentPatient.toPath() + nameFolder[i]);
					}
					if (file.exists())
						file.delete();
					if (folder.exists())
						folder.delete();
				}

				consultationFileCurrentPatient.delete();
				currentPatient.getConsultationsFile()
						.remove(currentPatient.getConsultationsFile().size() - 1 - indexConsultationList);
				setActionOnLeftClickOnListPatient(null);
			}
		}
	}

	/**
	 * -----------------------------------------------
	 * Getters and setters
	 * -----------------------------------------------
	 */

	/**
	 * @return the listOrdonnance
	 */
	public JList<String> getListConsultation() {
		return listConsultationJList;
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
	public static FrameMedecinAddPatient getFrameAddPatientWithMedecin() {
		return frameAddPatientWithMedecin;
	}

	/**
	 * @return currentMedecin
	 */
	public static Medecin getCurrentMedecin() {
		return currentMedecin;
	}

	/**
	 * @param currentMedecin
	 */
	public static void setCurrentMedecin(Medecin currentMedecin) {
		FrameMedecin.currentMedecin = currentMedecin;
	}

	/**
	 * @return currentPatient
	 */
	public static Patient getCurrentPatient() {
		return currentPatient;
	}

	/**
	 * @param currentPatient
	 */
	public static void setCurrentPatient(Patient currentPatient) {
		FrameMedecin.currentPatient = currentPatient;
	}

	/**
	 * @return consultationSwitchFileCurrentPatient
	 */
	public static File getConsultationSwitchFileCurrentPatient() {
		return consultationSwitchFileCurrentPatient;
	}

	/**
	 * @param consultationSwitchFileCurrentPatient
	 */
	public static void setConsultationSwitchFileCurrentPatient(File consultationSwitchFileCurrentPatient) {
		FrameMedecin.consultationSwitchFileCurrentPatient = consultationSwitchFileCurrentPatient;
	}

	/**
	 * @param frameConsultation
	 */
	public static void setFrameConsultation(FrameConsultation frameConsultation) {
		FrameMedecin.frameConsultation = frameConsultation;
	}

	/**
	 * @param frameAddPatientWithMedecin
	 */
	public static void setFrameAddPatientWithMedecin(FrameMedecinAddPatient frameAddPatientWithMedecin) {
		FrameMedecin.frameAddPatientWithMedecin = frameAddPatientWithMedecin;
	}

}