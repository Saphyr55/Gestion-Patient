/**
 * 
 */
package windows.medecin;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarculaLaf;

import hopital.patient.Patient;
import hopital.personnels.Medecin;
import windows.FrameConnexion;

/**
 * @author Andy
 *
 */
public class FrameCreateConsultation extends JFrame{

	private static final int width = 300;
	private static final int height = 500;
	private static final String title = "Creation ordonnance";
	private static boolean isVisible = true;
	
	/*
	 * 
	 */
	private JPanel contentPanel = (JPanel) getContentPane();
	private JPanel panelTop, panelCenter, panelBottom;
	private JLabel lastNamePatient, firstNamePatient, lastNameMedecin, firstNameMedecin;
	private JTextField avisMedecin, medicamentsPrescrits;
	private JCheckBox signatureMedecin;
	//private JButton 
	
	/*
	 * 
	 */
	private Medecin currentMedecin = FrameConnexion.getCurrentMedecin();
	private Patient currentPatient = FrameMedecin.getSelectedPatient();
	
	/**
	 * Constructeur de la fenetre de la creation d'ordonnance 
	 */
	public FrameCreateConsultation() {
		setOptionWindow();
		new JLabel(currentPatient.getFirstName());
		setVisible(isVisible);
	}
	
	private void setOptionWindow() {
		try {
			UIManager.setLookAndFeel( new FlatDarculaLaf() );
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}
		setSize(width, height);
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	
	
}
