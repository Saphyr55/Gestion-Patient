package windows.admin;

import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.MaskFormatter;

import hopital.Hopital;
import windows.FrameConnection;

/**
 * FrameAdminAddPatient
 */
public class FrameAdminAddPatient extends JFrame {

    public static final int width = 700;
    public static final int height = 450;
    public static final String title = "Gestion Admin - ajouter patient - Univ-Tours";

    private JPanel contentPane = (JPanel) getContentPane();

    private JPanel formPanel, confirmPanel;
    private JPanel stringsTextFieldsPanel, patientInputPanel;
    private String[] namesStringsForTextFields = { "Nom", "Prénom", "Date de naissance", "Numero de securité social",
            "Telephone", "Addresse" };
    private JTextField[] stringsTextFields;
    private JTextField patientLastnameInputTextField;
    private JTextField patientFirstnameInpuTextField;
    private JFormattedTextField patientDateInputFormattedTextField;
    private JFormattedTextField patientSecuNumberInputFormattedTextField;
    private JFormattedTextField patientNumberPhoneInputFormFormattedTextField;
    private JTextField patientAddressInputTextField;
    private JLabel signLabel;
    private JCheckBox signCheckBox;
    private JButton cancelButton, confirmButton, recupDataCarteVital;

    private static MaskFormatter dateFormatter;
    private static MaskFormatter secuNumbeFormatter;
    private static MaskFormatter phoneNumberFormatter;
    private static Font font1 = new Font("SansSerif", Font.BOLD, 20);

    /**
     * 
     */
    public FrameAdminAddPatient() {
        super(title);
        setOptionFrame();
        contentPane.add(setFormPanel(), BorderLayout.CENTER);
        contentPane.add(setConfirmPanel(), BorderLayout.SOUTH);
        setVisible(true);
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
        this.setResizable(false);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * 
     * @return formPanel
     */
    private JPanel setFormPanel() {
        try {
            dateFormatter = new MaskFormatter("##/##/####");
            secuNumbeFormatter = new MaskFormatter("### ### ### ### ###");
            phoneNumberFormatter = new MaskFormatter("## ## ## ## ##");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        formPanel = new JPanel(new BorderLayout());
        stringsTextFieldsPanel = new JPanel();
        patientInputPanel = new JPanel();
        stringsTextFields = new JTextField[namesStringsForTextFields.length];
        for (int i = 0; i < namesStringsForTextFields.length; i++) {
            stringsTextFields[i] = new JTextField(namesStringsForTextFields[i]);
            stringsTextFields[i].setEditable(false);
            stringsTextFields[i].setFont(font1);
            stringsTextFieldsPanel.add(stringsTextFields[i]);
        }

        patientLastnameInputTextField = new JTextField();
        patientLastnameInputTextField.setFont(font1);
        patientFirstnameInpuTextField = new JTextField();
        patientFirstnameInpuTextField.setFont(font1);
        patientDateInputFormattedTextField = new JFormattedTextField(dateFormatter);
        patientDateInputFormattedTextField.setFont(font1);
        patientSecuNumberInputFormattedTextField = new JFormattedTextField(secuNumbeFormatter);
        patientSecuNumberInputFormattedTextField.setFont(font1);
        patientNumberPhoneInputFormFormattedTextField = new JFormattedTextField(phoneNumberFormatter);
        patientNumberPhoneInputFormFormattedTextField.setFont(font1);
        patientAddressInputTextField = new JTextField();
        patientAddressInputTextField.setFont(font1);

        patientInputPanel.add(patientLastnameInputTextField);
        patientInputPanel.add(patientFirstnameInpuTextField);
        patientInputPanel.add(patientDateInputFormattedTextField);
        patientInputPanel.add(patientSecuNumberInputFormattedTextField);
        patientInputPanel.add(patientNumberPhoneInputFormFormattedTextField);
        patientInputPanel.add(patientAddressInputTextField);

        stringsTextFieldsPanel.setLayout(new BoxLayout(stringsTextFieldsPanel, BoxLayout.PAGE_AXIS));
        patientInputPanel.setLayout(new BoxLayout(patientInputPanel, BoxLayout.PAGE_AXIS));

        formPanel.add(stringsTextFieldsPanel, BorderLayout.WEST);
        formPanel.add(patientInputPanel, BorderLayout.CENTER);
        
        return formPanel;
    }

    /**
     * 
     * @return confirmPanel
     */
    private JPanel setConfirmPanel() {
        confirmPanel = new JPanel(new BorderLayout());
        recupDataCarteVital = new JButton("Chargement par carte vital");
        JPanel panelLeft = new JPanel(new FlowLayout());
        JPanel panelRight = new JPanel(new FlowLayout());
        signLabel = new JLabel("Signature");
        signCheckBox = new JCheckBox();
        confirmButton = new JButton("Ajouter");
        confirmButton.setEnabled(false);
        cancelButton = new JButton("Annuler");

        panelLeft.add(recupDataCarteVital);
        panelRight.add(signLabel);
        panelRight.add(signCheckBox);
        panelRight.add(confirmButton);
        panelRight.add(cancelButton);

        confirmPanel.add(panelLeft, BorderLayout.WEST);
        confirmPanel.add(panelRight, BorderLayout.EAST);

        signCheckBox.addActionListener(new ActionListenerSignBox());

        cancelButton.addActionListener(new FrameAdmin.CancelButtonFrameAddPatientListener());

        return confirmPanel;
    }

    /**
     * Active le bouton ajouter si le medecin signe
     */
    private class ActionListenerSignBox implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (signCheckBox.isSelected()) {
                confirmButton.setEnabled(true);
            } else if (!signCheckBox.isSelected()) {
                confirmButton.setEnabled(false);
            }
        }
    }

}