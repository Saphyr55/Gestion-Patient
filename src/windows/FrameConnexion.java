package windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import hopital.Hopital;
import hopital.expections.ConnexionExeption;
import hopital.loading.language.Language;
import hopital.loading.language.LoadingLanguage;
import hopital.personnels.Administrator;
import hopital.personnels.Medecin;
import windows.admin.FrameAdmin;
import windows.medecin.FrameMedecin;

/**
 * Frame de connexion
 * Permet au personnel de l'hopital de ce connecter
 * avec leur identifiant et motdepasse
 * @author Andy
 *
 */
public class FrameConnexion extends JFrame {
	
	/*
	 * Language du logiciel par default
	 */
	private static Language language;
	private static LoadingLanguage loadingLanguage = new LoadingLanguage(language);
	
	/**
	 * Variable option window connexion
	 */
	private final static int height = 550;
	private final static int width = 400;
	private final static String title = (String) loadingLanguage.getJsonObject().get("frame_connection_title");
	private boolean isVisible = true;
	
	/**
	 * Containers
	 */
	private JPanel contentPanel = (JPanel) getContentPane();
	private JPanel connexionPanel;
	private JTextField identifiantField;
	private JPasswordField passwordFeild;
	private JButton connexionButton;
	private JLabel wrongConnexion;
	private JButton passwordForgot;
	private JPanel connexion;
	private JLabel passwordLabel;
	private JPanel password;
	private JPanel identifiant;
	private JLabel identifiantLabel;
	private JPanel panelSelectLanguage;
	private JComboBox<String> languageSelection;
	
	/*
	 * Gestion
	 */
	private static Medecin currentMedecin;
	private static Administrator currentAdmin;
	
	/*
	 * List de langue en string, et l'enum Language
	 */
	private String[] languagesString = {"English","French"};
	private String[] languagesStringForJComboBox = {"Select Languages","English","French"};
	private ArrayList<Language> languages = new ArrayList<>();
	
	public FrameConnexion() {
		super(title);
		setOptionFrame();
		Hopital.loadingHopitalPersonnel();
		contentPanel.add(formulaireConnexion());
		contentPanel.add(panelSelectLanguage, BorderLayout.SOUTH);
		setVisible(isVisible);
	}
	
	public FrameConnexion(Language language) {
		super(title);
		FrameConnexion.language = language;
		FrameConnexion.loadingLanguage = new LoadingLanguage(language);
		setOptionFrame();
		Hopital.loadingHopitalPersonnel();
		contentPanel.add(formulaireConnexion());
		contentPanel.add(panelSelectLanguage, BorderLayout.SOUTH);
		setVisible(isVisible);
	}
	
	/**
	 * Option de la frame de connexion
	 */
	private void setOptionFrame() {
		for (Language language : Language.values())
		{
			languages.add(language);
		}
		try {
			UIManager.setLookAndFeel( new FlatDarkLaf() );
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}
		this.setSize(width, height);
		this.setResizable(false);
		this.setLocationRelativeTo(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Representation du formulaire de connexion
	 */
	private JPanel formulaireConnexion() {
		connexionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
		
		/*
		 * Partie de l'indentification
		 */
		identifiant = new JPanel(new GridLayout(2,1));
		identifiantLabel = new JLabel((String) loadingLanguage.getJsonObject().get("frame_connection_id"));
		identifiantField = new JTextField();
		identifiantField.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		identifiantField.setPreferredSize(new Dimension(300, 50));
		identifiantLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 30));
		identifiant.add(identifiantLabel);
		identifiant.add(identifiantField);
		
		/*
		 * Partie du mot de passe
		 */
		password = new JPanel(new GridLayout(2,1));
		passwordLabel = new JLabel((String) loadingLanguage.getJsonObject().get("frame_connection_password"));
		passwordFeild = new JPasswordField();
		passwordFeild.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		passwordLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 30));
		password.setPreferredSize(new Dimension(300, 100));
		password.add(passwordLabel);
		password.add(passwordFeild);
		
		/*
		 * Partie de connexion
		 */
		connexion = new JPanel(new GridLayout(3,1));
		connexionButton = new JButton((String) loadingLanguage.getJsonObject().get("frame_connection_login"));
		connexionButton.setPreferredSize(new Dimension(200, 40));
		
		/*
		 * Partie du mot de passe oubli§ et affichage d'un message erreur en rouge disant mauvais mot de passe ou identifiant 
		 */
		passwordForgot = new JButton((String) loadingLanguage.getJsonObject().get("frame_connection_password_forgot"));
		wrongConnexion = new JLabel("<html><font color='red'></font></html>");
		passwordForgot.setPreferredSize(new Dimension(100, 20));
		passwordForgot.setContentAreaFilled(false);
		wrongConnexion.setPreferredSize(new Dimension(100, 20));
		wrongConnexion.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
		connexion.add(connexionButton);
		connexion.add(passwordForgot);
		connexion.add(wrongConnexion);
		
		/*
		 * Le panel de selection langue
		 */
		panelSelectLanguage = new JPanel();
		languageSelection = new JComboBox<>(languagesStringForJComboBox);
		panelSelectLanguage.add(languageSelection);
		
		/*
		 * Ajout des panels au panel de connexion
		 */
		connexionPanel.add(identifiant);
		connexionPanel.add(password);
		connexionPanel.add(connexion);
		
		/*
		 * Permetant de se connecter a une Frame destin§ a un certains personnel
		 * un appuyant sur le button se connecter
		 */
		connexionButton.addActionListener(new ActionListener() {
			@Override

			public void actionPerformed(ActionEvent e) {
				
				/*
				 * Variables
				 */
				String password = String.valueOf(passwordFeild.getPassword());
				String identifiantText = identifiantField.getText();
				String line;
				String string;	
				String[] strings;
				int nLine = 0;
				int j = 0;
				
				/*
				 * Lecture et verfication de l'indentifiant et du mot de passe
				 */
				BufferedReader readerAdmins = new BufferedReader(Hopital.getAdminsReaderFile());
				BufferedReader readerMedecins = new BufferedReader(Hopital.getMedecinReaderFile());
				try {
					
					/*
					 * Cas pour le fichier admins
					 * Lis le fichier admins puis verifie pour chaque
					 * ligne le bon identifiant et mot de passe
					 * Et attribue l'admin courrant a l'admin de la connexion
					 * permettant de capturer les donn§e du admin pour la FrameAdmin
					 */
					while((line = readerAdmins.readLine()) != null) 
					{
						string = line;
						strings = string.split("&");
						
						nLine++;
						if( strings[1].equals(identifiantText) && strings[strings.length - 1].equals(password) ) 
						{
							for(int i = 0; i < Hopital.getAdmins().size(); i++) 
							{
								if(Hopital.getAdmins().get(i).getIdentifiant() == Integer.parseInt(strings[1])) 
								{
									currentAdmin = Hopital.getAdmins().get(i);
								}
							}
						 	new FrameAdmin();
							dispose();
						} else j++;
					}
					readerAdmins.close();
					
					/*
					 * Cas pour le fichier medecins
					 * Lis le fichier medecins puis verifie pour chaque
					 * ligne est le bon identifiant et mot de passe
					 * Et attribue le medecin courant au medecin de la connexion
					 * permettant de capturer les donn§e du medecin pour la FrameMedecin
					 */
					while((line = readerMedecins.readLine()) != null ) {
						string = line;
						strings = string.split("&");
						nLine++;
						if(strings[1].equals(identifiantText) && strings[strings.length - 1].equals(password)) {
							for(int i = 0 ; i < Hopital.getMedecins().size() ; i++) {
								if(Hopital.getMedecins().get(i).getIdentifiant() == Integer.parseInt(identifiantText)) {
									currentMedecin = Hopital.getMedecins().get(i);
								}
							}
							new FrameMedecin();
							dispose();
						} else j++;
					}
					readerMedecins.close();
					
					/*
					 * Mauvaise connexion
					 */
					if(j == nLine) throw new ConnexionExeption();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ConnexionExeption e1) {
					
					/*
					 *  Affiche Mauvais mot de passe ou identifiant en rouge
					 *  avec une durrée de 10 secondes
					 */
					wrongConnexion.setText(
							"<html><font color='red'>"+
							(String) loadingLanguage.getJsonObject().get("frame_connection_password_id_wrong")+
							"</font></html>");						
					Timer chrono = new Timer();
					chrono.schedule(new TimerTask() {
						@Override
						public void run() {
							wrongConnexion.setText("<html><font color='red'></font></html>");
						}
					}, 10000);
				}
			}
		});
		
		languageSelection.addItemListener(new ItemListener() 
		{
			@Override
			public void itemStateChanged(ItemEvent event) 
			{
				if (event.getStateChange() == ItemEvent.SELECTED) 
				{
					String item = (String) event.getItem();
					int index = 0;
					for (int i = 0; i < languagesString.length; i++) 
					{
						if(languagesString[i].equals(item)) 
						{
							index = i;
							break;
						}
			        }
					
					for (int i = 0; i < languages.size(); i++) {
						if(i == index)
						{
							language = languages.get(index);
							System.out.println(language);
							dispose();
							new FrameConnexion(language);
							break;
						}
					}
			     }
			}
		});
		return connexionPanel;
	}

	/**
	 * @return the currentMedecin
	 */
	public static Medecin getCurrentMedecin() {
		return currentMedecin;
	}

	/**
	 * @param currentMedecin the currentMedecin to set
	 */
	public static void setCurrentMedecin(Medecin currentMedecin1) {
		currentMedecin = currentMedecin1;
	}

	/**
	 * @return the currentAdmin
	 */
	public static Administrator getCurrentAdmin() {
		return currentAdmin;
	}

	/**
	 * @param currentAdmin the currentAdmin to set
	 */
	public static void setCurrentAdmin(Administrator currentAdmin1) {
		currentAdmin = currentAdmin1;
	}
	
	/**
	 * @return language
	 */
	public static Language getLanguage() {
		return language;
	}
	
	public static LoadingLanguage getLoadingLanguage() {
		return loadingLanguage;
	}
	
	
}
