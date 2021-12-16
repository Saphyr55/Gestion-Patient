package windows.admin;

import java.util.ArrayList;

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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
import javax.swing.plaf.DimensionUIResource;

import components.ListPatientPanel;
import hopital.Hopital;
import hopital.patient.Patient;
import windows.FrameConnection;

public class FrameAdmin extends JFrame {

	/**
	 * 
	 */
	private static final int width = 780;
	private static final int height = 720;
	private static final String title = "Admin Gestion";
	private static boolean isVisible = true;

	/**
	 * 
	 */
	private JPanel contentPanel = (JPanel) getContentPane();

	/**
	 * Composant du JPanel de liste de patient
	 */
	private ListPatientPanel panelListPatient;
	private JPopupMenu popupMenuListPatient;
	private JMenuItem menuItemAddPatient, menuItemSupprPatient, menuItemAddConsultation;
	private ArrayList<String> listNamePatient = new ArrayList<>();
	private JPanel dataPatientPanel, panelTop, panelCenter, panelBottom;
	private JTextField[] dataPatientTextFields;
	private JButton switchLectureModifDataPatientButton;
	private JButton confirmModifButton;

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
		setResizable(false);
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
		panelListPatient = new ListPatientPanel(height, Hopital.getPatients());
		/*
		 * Actionne l'affichage de la liste d'ordonnances
		 * et les données du patient selectionner
		 */
		panelListPatient.getListPatient().addMouseListener(new MouseAdapter() {
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
							panelListPatient.getMenuItemSupprPatient().addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									int input = JOptionPane.showConfirmDialog(null,
											"frame_medecin_confirm_delete_patient");

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
							panelListPatient.getMenuItemAddPatient().addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {

								}
							});

							panelListPatient.getMenuItemAddConsultation().addActionListener(new ActionListener() {
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
		 * Affichage de la frame pour ajouter un patient lors du clique du bouton '+'
		 */
		panelListPatient.getAddPatient().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}

		});

		return panelListPatient;

	}

	private JPanel setDataPatient() {
		dataPatientPanel = new JPanel(new BorderLayout());
		panelTop = new JPanel();
		switchLectureModifDataPatientButton = new JButton();
		dataPatientTextFields = new JTextField[6];
		for (int i = 0; i < dataPatientTextFields.length; i++) {
			dataPatientTextFields[i] = new JTextField();
			dataPatientTextFields[i].setEditable(false);
			dataPatientTextFields[i].setPreferredSize(new Dimension(300, 30));
		}

		return dataPatientPanel;
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
		menuItemAddPatient = new JMenuItem("frame_medecin_popup_add");
		menuItemSupprPatient = new JMenuItem("frame_medecin_popup_delete");
		menuItemAddConsultation = new JMenuItem("frame_medecin_popup_add_consultation");
		popupMenuListPatient.add(menuItemAddPatient);
		popupMenuListPatient.add(menuItemSupprPatient);
		popupMenuListPatient.add(menuItemAddConsultation);
		return popupMenuListPatient;
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
			String filter = panelListPatient.getFoundPatientField().getText();
			filterModel((DefaultListModel<String>) panelListPatient.getListPatient().getModel(), filter);
		}
	}

}
