package windows.medecin;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PanelSwitchTypeConsultation extends JPanel {

	public static enum TypeConsultation {
		ORDONNANCE, AVIS_MEDICAL
	}

	/**
	 * Ordonnance
	 */
	private JPanel switchTypeConsultationPanel;

	private JPanel ordonnancePanel;
	private JLabel ordonnanceStringLabel;
	private JTextArea ordonnanceTextArea;

	public PanelSwitchTypeConsultation() {
		super();
		switchTypeConsultationPanel = null;
		switchTypeConsultationPanel = setOrdonnancePanel();
	}

	public PanelSwitchTypeConsultation(TypeConsultation condition) {
		super();
		switchTypeConsultationPanel = null;
		if (condition == TypeConsultation.ORDONNANCE) {
			switchTypeConsultationPanel = setOrdonnancePanel();
		} else if (condition == TypeConsultation.AVIS_MEDICAL) {
			switchTypeConsultationPanel = setAvisMedicalPanel();
		}
		super.add(switchTypeConsultationPanel);
	}

	/**
	 * Methode pour creer le panel de l'ordonnance
	 * 
	 * @return ordonnancePanel
	 */
	private JPanel setOrdonnancePanel() {
		ordonnancePanel = new JPanel(new BorderLayout());
		ordonnanceStringLabel = new JLabel();
		ordonnanceTextArea = new JTextArea();

		ordonnancePanel.add(ordonnanceStringLabel, BorderLayout.NORTH);
		ordonnancePanel.add(ordonnanceTextArea, BorderLayout.CENTER);

		return ordonnancePanel;
	}

	/**
	 * 
	 * @return
	 */
	private JPanel setAvisMedicalPanel() {

		return null;
	}

}
