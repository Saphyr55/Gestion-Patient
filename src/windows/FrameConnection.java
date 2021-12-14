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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import org.json.simple.JSONObject;

import hopital.Hopital;
import hopital.expections.ConnexionExeption;
import hopital.loading.LoadingRememberMe;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.Language;
import hopital.loading.language.LoadingLanguage;
import hopital.loading.paths.LoadingPath;
import hopital.personnels.Administrator;
import hopital.personnels.Medecin;
import windows.admin.FrameAdmin;
import windows.medecin.FrameMedecin;

/**
 * Frame de connexion
 * Permet au personnel de l'hopital de ce connecter
 * avec leur identifiant et mot de passe
 * 
 * @author Andy
 *
 */
public class FrameConnection extends JFrame {

	/**
	 * Language du logiciel par default
	 */
	private static Language language;
	private static LoadingLanguage loadingLanguage = new LoadingLanguage(language);
	private static LoadingRememberMe loadingRememberMe = new LoadingRememberMe();
	private static LoadingPath loadingPath = new LoadingPath();

	/**
	 * Charchegement des dimensions
	 */
	private static LoadingDimens loadingDimens = new LoadingDimens();

	/**
	 * Variable option window connexion
	 */
	private final static int height = (int) ((long) loadingDimens.getJsonObject().get("frame_connection_height"));
	private final static int width = (int) ((long) loadingDimens.getJsonObject().get("frame_connection_width"));
	private final static String title = (String) loadingLanguage.getJsonObject().get("frame_connection_title");
	private boolean isVisible = true;
	private boolean isSelectedRemember = (Boolean) loadingRememberMe.getJsonObject().get("remember_me");

	/**
	 * Containers
	 */
	private JPanel panelTop;
	private JPanel contentPane = (JPanel) getContentPane();
	private JPanel connexionPanel;
	private JTextField identifiantField;
	private JPasswordField passwordFeild;
	private JButton connexionButton;
	private JLabel wrongConnexion;
	private JButton passwordForgot;
	private JPanel connexion;
	private JLabel passwordLabel;
	private JPanel passwordPanel;
	private JPanel identifiant;
	private JLabel identifiantLabel;
	private JPanel panelSelectLanguage;
	private JComboBox<String> languageSelection;
	private JPanel rememberMePanel;
	private JLabel rememberMeLabel;
	private JCheckBox rememberMeCheckBox;

	/*
	 * Gestion
	 */
	private static Medecin currentMedecin;
	private static Administrator currentAdmin;

	/*
	 * List de langue en string, et l'enum Language
	 */
	private String[] languagesString = { "English", "Francais" };
	private String[] languagesStringForJComboBox = { "Select Languages", "English", "Francais" };
	private ArrayList<Language> languages = new ArrayList<>();

	/**
	 * 
	 */
	private String password = "";
	private String identifiantText = "";

	public FrameConnection() {
		super(title);
		setOptionFrame();
		contentPane.add(formulaireConnexion());
		contentPane.add(panelSelectLanguage, BorderLayout.SOUTH);
		setVisible(isVisible);
	}

	public FrameConnection(Language language) {
		super(title);
		FrameConnection.language = language;
		FrameConnection.loadingLanguage = new LoadingLanguage(language);
		setOptionFrame();
		contentPane.add(formulaireConnexion(), BorderLayout.CENTER);
		contentPane.add(panelSelectLanguage, BorderLayout.SOUTH);
		setVisible(isVisible);
	}

	/**
	 * Option de la frame de connexion
	 */
	private void setOptionFrame() {
		try {
			UIManager.setLookAndFeel(new FlatIntelliJLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
		for (Language language : Language.values()) {
			languages.add(language);
		}
		this.setSize(width, height);
		this.setResizable(false);
		this.setLocationRelativeTo(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String line;
		String string;
		String[] strings;
		if (Hopital.fileRememberme.exists() && isSelectedRemember == true) {
			BufferedReader reader = new BufferedReader(Hopital.getRemembermeReaderFile());
			try {
				while ((line = reader.readLine()) != null) {
					string = line;
					strings = string.split("&");
					identifiantText = strings[0];
					password = strings[1];
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private JPanel setTopPanel() {
		panelTop = new JPanel();

		panelTop.add(rememberMePanel);

		return panelTop;
	}

	/**
	 * Representation du formulaire de connexion
	 */
	private JPanel formulaireConnexion() {
		connexionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));

		/*
		 * Partie de l'indentification
		 */
		identifiant = new JPanel(new GridLayout(2, 1));
		identifiantLabel = new JLabel((String) loadingLanguage.getJsonObject().get("frame_connection_id"));
		identifiantField = new JTextField();
		identifiantField.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		identifiantField.setText(identifiantText);
		identifiantField.setPreferredSize(new Dimension(300, 50));
		identifiantLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 30));
		identifiant.add(identifiantLabel);
		identifiant.add(identifiantField);

		/*
		 * Partie du mot de passe
		 */
		passwordPanel = new JPanel(new GridLayout(2, 1));
		passwordLabel = new JLabel((String) loadingLanguage.getJsonObject().get("frame_connection_password"));
		passwordFeild = new JPasswordField();
		passwordFeild.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 20));
		passwordFeild.setText(password);
		passwordLabel.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 30));
		passwordPanel.setPreferredSize(new Dimension(300, 100));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordFeild);

		/*
		 * Partie de connexion
		 */
		connexion = new JPanel(new GridLayout(3, 1));
		connexionButton = new JButton((String) loadingLanguage.getJsonObject().get("frame_connection_login"));
		connexionButton.setPreferredSize(new Dimension(200, 40));

		/*
		 * Partie du mot de passe oublié et affichage d'un message erreur en rouge
		 * disant mauvais mot de passe ou identifiant
		 */
		passwordForgot = new JButton((String) loadingLanguage.getJsonObject().get("frame_connection_password_forgot"));
		wrongConnexion = new JLabel("<html><font color='red'></font></html>");
		passwordForgot.setPreferredSize(new Dimension(100, 20));
		passwordForgot.setContentAreaFilled(false);
		wrongConnexion.setPreferredSize(new Dimension(100, 20));
		wrongConnexion.setFont(new Font("Sans-Serif", Font.PLAIN, 15));

		/**
		 * Ajouter du button de connexion,
		 * du button mot ou oublié et
		 * message du mauvais mot de passe
		 */
		connexion.add(connexionButton);
		connexion.add(passwordForgot);
		connexion.add(wrongConnexion);
		/**
		 * Panel se souvenir de moi
		 */
		rememberMePanel = new JPanel(new FlowLayout());
		rememberMeCheckBox = new JCheckBox();
		rememberMeLabel = new JLabel("Remember Me");
		rememberMePanel.add(rememberMeCheckBox);
		rememberMePanel.add(rememberMeLabel);
		rememberMeCheckBox.setSelected(isSelectedRemember);
		/*
		 * Le panel de selection langue
		 */
		panelSelectLanguage = new JPanel(new FlowLayout());
		languageSelection = new JComboBox<>(languagesStringForJComboBox);
		panelSelectLanguage.add(languageSelection);
		panelSelectLanguage.add(rememberMePanel);

		/*
		 * Ajout des panels au panel de connexion
		 */
		connexionPanel.add(identifiant);
		connexionPanel.add(passwordPanel);
		connexionPanel.add(connexion);

		/*
		 * Permetant de se connecter a une Frame destiné a un certains personnel
		 * un appuyant sur le button se connecter
		 */
		connexionButton.addActionListener(new ActionListenerConnectionButton());

		/**
		 * Applique la langue selectionner
		 */
		languageSelection.addItemListener(new ItemListenerLanguageSelection());

		return connexionPanel;
	}

	/**
	 * Permetant de se connecter a une Frame destiné a un certains personnel
	 * un appuyant sur le button se connecter
	 */
	public class ActionListenerConnectionButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			/*
			 * Variables
			 */
			identifiantText = identifiantField.getText();
			password = String.valueOf(passwordFeild.getPassword());
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
				 * ligne que nos entrées sont le bon identifiant et mot de passe
				 * Et attribue l'admin courrant a l'admin de la connexion
				 * permettant de capturer les données du admin pour la FrameAdmin
				 */
				while ((line = readerAdmins.readLine()) != null) {
					string = line;
					strings = string.split("&");
					nLine++;

					if (strings[1].equals(identifiantText) && strings[strings.length - 1].equals(password)) {
						Hopital.loadingAdmin();
						for (int i = 0; i < Hopital.getAdmins().size(); i++) {
							if (Hopital.getAdmins().get(i).getIdentifiant() == Integer.parseInt(strings[1])) {
								currentAdmin = Hopital.getAdmins().get(i);
							}
						}
						setRemembermeFile();
						new FrameAdmin();
						dispose();
					} else
						j++;
				}
				readerAdmins.close();

				/*
				 * Cas pour le fichier medecins
				 * Lis le fichier medecins puis verifie pour chaque
				 * ligne que nos entrées sont le bon identifiant et mot de passe
				 * Et attribue le medecin courant au medecin de la connexion
				 * permettant de capturer les données du medecin pour la FrameMedecin
				 */
				while ((line = readerMedecins.readLine()) != null) {
					string = line;
					strings = string.split("&");
					nLine++;
					if (strings[1].equals(identifiantText) && strings[strings.length - 1].equals(password)) {
						Hopital.loadingMedecin();
						for (int i = 0; i < Hopital.getMedecins().size(); i++) {
							if (Hopital.getMedecins().get(i).getIdentifiant() == Integer
									.parseInt(identifiantText)) {
								currentMedecin = Hopital.getMedecins().get(i);
							}
						}
						setRemembermeFile();
						new FrameMedecin();
						dispose();
					} else
						j++;
				}
				readerMedecins.close();

				/*
				 * Mauvaise connexion
				 */
				if (j == nLine)
					throw new ConnexionExeption();

			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ConnexionExeption e1) {

				/*
				 * Affiche Mauvais mot de passe ou identifiant en rouge
				 * avec une durrée de 10 secondes
				 */
				wrongConnexion.setText(
						"<html><font color='red'>" +
								(String) loadingLanguage.getJsonObject().get("frame_connection_password_id_wrong") +
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
	}

	/**
	 * Stock le mot de passe et l'indentifiant dans un fichier
	 * quand la case se souvenir est cocher
	 * Et enregistre la case se se souvenir au prochain lancement
	 * 
	 */
	private void setRemembermeFile() {
		if (rememberMeCheckBox.isSelected()) {
			try {
				Hopital.fileRememberme.exists();
				Hopital.fileRememberme.delete();
				Hopital.createFileRememberme();
				JSONObject rememberSelected = new JSONObject();
				rememberSelected.put("remember_me", true);
				try (FileWriter fileJson = new FileWriter(
						(String) loadingPath.getJsonObject().get("path_rememberme"))) {
					fileJson.write(rememberSelected.toJSONString());
				}
				BufferedWriter writerRememberme = new BufferedWriter(Hopital.getRemembermeFileWriter());
				writerRememberme.write(identifiantText + "&" + password);
				writerRememberme.newLine();
				writerRememberme.close();
				rememberSelected.clear();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				Hopital.fileRememberme.exists();
				Hopital.fileRememberme.delete();
				Hopital.fileRememberme.deleteOnExit();
				JSONObject rememberSelected = new JSONObject();
				rememberSelected.put("remember_me", false);
				try (FileWriter fileJson = new FileWriter(
						(String) loadingPath.getJsonObject().get("path_rememberme"))) {
					fileJson.write(rememberSelected.toJSONString());
				}
				rememberSelected.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Applique la langue selectionner
	 */
	public class ItemListenerLanguageSelection implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				String item = (String) event.getItem();
				int index = 0;
				for (int i = 0; i < languagesString.length; i++) {
					if (languagesString[i].equals(item)) {
						index = i;
						break;
					}
				}

				for (int i = 0; i < languages.size(); i++) {
					if (i == index) {
						if (passwordFeild.getPassword() != null && identifiantField.getText() != null) {
							password = String.valueOf(passwordFeild.getPassword());
							identifiantText = identifiantField.getText();
							language = languages.get(index);
						}
						dispose();
						FrameConnection frame = new FrameConnection(language);
						frame.identifiantField.setText(identifiantText);
						frame.passwordFeild.setText(password);
						break;
					}
				}
			}
		}
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

	/**
	 * 
	 * @return loading language
	 */
	public static LoadingLanguage getLoadingLanguage() {
		return loadingLanguage;
	}

	/**
	 * 
	 * @return loading dimens
	 */
	public static LoadingDimens getLoadingDimens() {
		return loadingDimens;
	}

}
