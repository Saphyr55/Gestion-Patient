/**
 * 
 */
package windows.medecin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Period;

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

import com.formdev.flatlaf.FlatDarculaLaf;

import hopital.Consultation;
import hopital.Hopital;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnexion;

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
	 * 
	 */
	//private static Language language = FrameConnexion.getLanguage();
	private static LoadingLanguage loadingLanguage = FrameConnexion.getLoadingLanguage();
	
	/**
	 * Variables d'options pour la fenetre de gestion
	 */
	private static final int width = 1080;
	private static final int height = 720;
	private static final String title = (String) loadingLanguage.getJsonObject().get("frame_medecin_title");
	private static boolean isVisible = true;
	
	/*
	 * Tous les textes a charger
	 */
	private static String frame_medecin_confirm_delete_patient = (String) loadingLanguage.getJsonObject().get("frame_medecin_title");
	private static String frame_medecin_lastname = (String) loadingLanguage.getJsonObject().get("frame_medecin_lastname");
	private static String frame_medecin_firstname = (String) loadingLanguage.getJsonObject().get("frame_medecin_firstname");
	private static String frame_medecin_birthday = (String) loadingLanguage.getJsonObject().get("frame_medecin_birthday");
	private static String frame_medecin_age = (String) loadingLanguage.getJsonObject().get("frame_medecin_age");
	private static String frame_medecin_delete = (String) loadingLanguage.getJsonObject().get("frame_medecin_delete");
	private static String frame_medecin_new_consultation = (String) loadingLanguage.getJsonObject().get("frame_medecin_new_consultation");
	private static String frame_medecin_popup_add = (String) loadingLanguage.getJsonObject().get("frame_medecin_popup_add");
	private static String frame_medecin_popup_delete = (String) loadingLanguage.getJsonObject().get("frame_medecin_popup_delete");

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
	private JMenuItem menuItemAddPatient, menuItemSupprPatient;
	
	/**
	 * Composant des données 
	 */
	private JPanel panelPatient, panelTop, panelBottom, panelData, panelOrdonnance ;
	private JLabel lastNamePatient, firstNamePatient, birthdayPatient ,agePatient;
	private JList<String> listOrdonnanceJList;
	private DefaultListModel<String> nameListOrdonnanceDefaultModel = new DefaultListModel<>();
	private JScrollPane listOrdonnanceScrollPane = new JScrollPane();
	private JTextArea ordonnanceText;
	private JButton suppr, ajoutOrdonnance;
	
	/*
	 * Gestion donnée frame
	 */
	private static Medecin currentMedecin = FrameConnexion.getCurrentMedecin();
	private static Patient currentPatient;
	
	/**
	 * Constructeur
	 */
	public FrameMedecin() {
		super(title);
		setOptionFrame();
		panelPrincipal.add(setListPatient(), BorderLayout.WEST);
		panelPrincipal.add(setPatient(new Patient()) , BorderLayout.CENTER);
		setVisible(isVisible);
	}
	
	/**
	 * Option de la frame 
	 */
	private void setOptionFrame() {
		try {
			UIManager.setLookAndFeel( new FlatDarculaLaf() );
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}
		this.setSize(width, height);
		this.setLocationRelativeTo(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Permet d'afficher la liste de patient,
	 * de pouvoir de rajouter des patients,
	 * de chercher un patient dans la liste
	 * @return liste de patient
	 */
	private JPanel setListPatient() {
		/**
		 *  Inititalisation des attribus pour la liste de patient  
		 */
		panelListPatient = new JPanel(new BorderLayout());
		JPanel panelTop = new JPanel(new BorderLayout());
		addPatient = new JButton("+");
		foundPatientField = new JTextField();
		
		for (int i = 0; i < currentMedecin.getPatients().size(); i++) {
			namePatients.addElement(currentMedecin.getPatients().get(i).getFirstName() + " " 
								  + currentMedecin.getPatients().get(i).getLastName() );
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
		 * Ajout du button d'ajout de patient et de recherche de patient dans le panelTop
		 * Et ajout du panelTop et de la listPatient dans le panelListPatient
		 */
		panelTop.add(addPatient , BorderLayout.WEST);
		panelTop.add(foundPatientField, BorderLayout.CENTER);
		panelListPatient.add(panelTop, BorderLayout.NORTH);
		panelListPatient.add(listPatientScrollPane, BorderLayout.CENTER);
		
		/*
		 * Actionne l'affichage de la liste d'ordonnances 
		 * et les données du patient selectionner
		 */
		listPatient.addMouseListener(new MouseAdapter() 
		{	
			@Override
			public void mouseClicked(MouseEvent event) 
			{	
				/**
				 * Recupere index de selection si on clique une fois
				 */
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) event.getSource();    
				int index = list.locationToIndex(event.getPoint());
				if ( event.getClickCount() == 1 ) 
		        {
					// le patient courant sur l'index recuperer lors du clique
		            currentPatient = currentMedecin.getPatients().get(index);
		            
		            /**
		             * Le clique gauche declenche la liste la d'ordonnance du patient selectionner
		             */
					if(SwingUtilities.isLeftMouseButton(event)) 
					{
			            panelPrincipal.remove(panelPatient);
			            loadingListOrdonnance(currentPatient);
			            
			    		if(panelOrdonnance != null) 
			            	panelPrincipal.remove(panelOrdonnance);
			            
			    		panelPrincipal.add(setPatient(currentPatient));
			            panelPrincipal.revalidate();
					}
					
					/**
					 * Le clique droit declenche une popup permetant de supprimer ou ajouter un patient
					 */
					else if(SwingUtilities.isRightMouseButton(event))
					{
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
								public void actionPerformed(ActionEvent e) 
								{	
									int input = JOptionPane.showConfirmDialog(null,frame_medecin_confirm_delete_patient);
									
									/**
									 * 
									 */
									if(input == JOptionPane.YES_OPTION) 
									{
										System.out.println(currentPatient.getLastName()+" a ete supprimer");
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
						}catch (IndexOutOfBoundsException e) { 
							e.printStackTrace();
						}
						
					}
		        } 
		    }
		});
		
		return panelListPatient;
	}
	
	/**
	 * Charge toutes les ordonnances du dossier du patient en parametre
	 * Puis ajoute le nom des fichier au x
	 * @param patient
	 */
	private void loadingListOrdonnance(Patient patient) 
	{
        Hopital.loadingOrdonnancesPatient(patient);
        if(nameListOrdonnanceDefaultModel.isEmpty())
        {
	        for (int i = 0; i < patient.getOrdonnancesFile().size(); i++) 
	        {
	        	if(!nameListOrdonnanceDefaultModel.contains(
	        	patient.getOrdonnancesFile().get(i).getName()
	        	.replace("&", " ").replace(".txt", ""))) 
	        	{
	        		nameListOrdonnanceDefaultModel.addElement(
	        				patient.getOrdonnancesFile().get(i).getName()
	        				.replace("&", " ").replace(".txt", ""));
	        	}
	        }
	        listOrdonnanceJList = new JList<>(nameListOrdonnanceDefaultModel);
	        listOrdonnanceScrollPane = new JScrollPane(listOrdonnanceJList);
        }
        else
        {
        	nameListOrdonnanceDefaultModel.removeAllElements();
        	
        	listOrdonnanceJList = null;
        	listOrdonnanceJList = new JList<>(nameListOrdonnanceDefaultModel);
        	
        	listOrdonnanceScrollPane = null;
        	listOrdonnanceScrollPane = new JScrollPane(listOrdonnanceJList);
        	
        	loadingListOrdonnance(patient);
        }
	}
	
	/**
	 * Permet d'afficher les metadonnées du patient
	 * selectionné lors de la liste de patient
	 * @param patient
	 * @return panel patient avec data
	 */
	private JPanel setPatient(Patient patient) {
	
		/**
		 *  Initialisation des composants
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
		panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT , 60, 10));
		lastNamePatient = new JLabel(frame_medecin_lastname+" : "+patient.getLastName());
		firstNamePatient = new JLabel(frame_medecin_firstname+" : "+patient.getFirstName());
		birthdayPatient = new JLabel(frame_medecin_birthday+" : "+patient.getBirthday());
		agePatient = new JLabel(frame_medecin_age+" : "+agePatientInteger);
		suppr = new JButton(frame_medecin_delete);	
		ajoutOrdonnance = new JButton(frame_medecin_new_consultation);
		
		/*
		 * panel bottom
		 */
		panelBottom = new JPanel(new BorderLayout());
		panelOrdonnance = new JPanel(new BorderLayout());
		ordonnanceText = new JTextArea();
		panelData = new JPanel();

		/*
		 * Option generales sur la liste d'ordonnance, le button supprimer un patient,
		 * le button ajouter un patient et nom, prenom, date de naissance du patient courant.
		 */
		ordonnanceText.setEditable(false);
		listOrdonnanceScrollPane.setPreferredSize(new Dimension(200, 0));
		ajoutOrdonnance.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 15));	
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
		 *  Ajout des composants du panel du bas
		 */
		panelOrdonnance.add(ajoutOrdonnance ,BorderLayout.NORTH);
		panelOrdonnance.add(listOrdonnanceScrollPane, BorderLayout.CENTER);
		panelBottom.add(panelOrdonnance, BorderLayout.WEST);
		panelBottom.add(ordonnanceText , BorderLayout.CENTER);
		panelBottom.add(panelData, BorderLayout.EAST);
		
		/*
		 * Ajout JPanel top et bottom au JPanel panelPatient
		 */
		panelPatient.add(panelTop, BorderLayout.NORTH);
		panelPatient.add(panelBottom, BorderLayout.CENTER);
		
		/*
		 * Action sur la liste d'ordonnance
		 */
		if(listOrdonnanceJList != null) 
		{
			listOrdonnanceJList.addMouseListener(new MouseAdapter() 
			{
					@Override
					public void mousePressed(MouseEvent event) 
					{
						File ordonnance;
						if(event.getClickCount() == 1) 
						{	
							@SuppressWarnings("unchecked")
							JList<String> list = (JList<String>) event.getSource();
							
							if(SwingUtilities.isLeftMouseButton(event)) 
							{
								int index = list.locationToIndex(event.getPoint());
						        ordonnance = patient.getOrdonnancesFile().get(index);
						        ordonnanceText.setText("");
						   		readOrdonnance(ordonnance);
						   		panelPrincipal.revalidate();
							}
							else if(SwingUtilities.isRightMouseButton(event)) 
							{
								System.out.println("Click Right");
							}
						}
					}
			});
		}
		
		/**
		 * Lors du clique du button nouvelle consultation
		 * Affiche une fenetre avec les options de creation de consultation 
		 */
		ajoutOrdonnance.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				new FrameCreateConsultation();
			}
		});
		
		return panelPatient;
	}
	
	/**
	 * PopupMenu lors du click droit sur la liste de patient
	 * @return popupMenu 
	 */
	private JPopupMenu setPopupMenuOnRigthClickListPatient()
	{
		popupMenuListPatient = new JPopupMenu();
		menuItemAddPatient= new JMenuItem(frame_medecin_popup_add);
		menuItemSupprPatient= new JMenuItem(frame_medecin_popup_delete);
		popupMenuListPatient.add(menuItemAddPatient);
		popupMenuListPatient.add(menuItemSupprPatient);
		return popupMenuListPatient;
	}
	
    /**
     * Lis une ordonnance et l'affiche au clique de l'ordonnance
     * @param file adresse du fichier
     * @return chaine de caractère
     */
    private void readOrdonnance(File ordonnance)
    {
    	BufferedReader reader;
    	FileReader in;
    	String line;
    	Consultation ordo;
    	try
        {	
    		if(ordonnance == null) 
    		{
    			ordo = new Consultation();
    			in = new FileReader(Hopital.pathOrdonnances+ordo.getName()+".txt");
    		}
    		else 
    		{    			
    			in = new FileReader(ordonnance.getAbsolutePath());
    		}
            reader = new BufferedReader(in);
            do 
            {
				line=reader.readLine();
				if(line!=null)
					this.ordonnanceText.setText(this.ordonnanceText.getText()+line+"\n");
			} while (line!=null);
        }
        catch( Exception e )
        {
        	e.printStackTrace();
        }   
    }

	/**
	 * @return the listOrdonnance
	 */
	public JList<String> getListOrdonnance() {
		return listOrdonnanceJList;
	}

	/**
	 * @param listOrdonnance the listOrdonnance to set
	 */
	public void setListOrdonnance(JList<String> listOrdonnance) {
		this.listOrdonnanceJList = listOrdonnance;
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
	 * @return the nameListOrdonnance
	 */
	public DefaultListModel<String> getNameListOrdonnance() {
		return nameListOrdonnanceDefaultModel;
	}

	/**
	 * @param nameListOrdonnance the nameListOrdonnance to set
	 */
	public void setNameListOrdonnance(DefaultListModel<String> nameListOrdonnance) {
		this.nameListOrdonnanceDefaultModel = nameListOrdonnance;
	}
	
	/**
	 * 
	 * @return 
	 */
	public static Patient getSelectedPatient() {
		return currentPatient;
	}
}
