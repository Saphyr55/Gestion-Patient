package windows.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

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

import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import windows.FrameConnection;

/**
 * FrameAdminAddPatient
 * Permet de creer un patient via un formulaire
 * 
 * @author Andy
 */
public class FrameAdminAddPatient extends JFrame {

    // ---------------------
    // Fields
    // ---------------------

    private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();
    private static LoadingDimens dimens = new LoadingDimens();

    private static final int width = (int) ((long) dimens.getJsonObject().get("frame_admin_add_patient_width"));
    private static final int height = (int) ((long) dimens.getJsonObject().get("frame_admin_add_patient_height"));
    public static final String title = (String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_title");

    private JPanel contentPane = (JPanel) getContentPane();

    private JPanel formPanel, confirmPanel;
    private JPanel stringsTextFieldsPanel, patientInputPanel;
    private String[] namesStringsForTextFields = {
            (String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_lastname"),
            (String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_firstname"),
            (String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_birthday"),
            (String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_secu_number"),
            (String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_phone"),
            (String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_adresse") };
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

    // -----------------------
    // Constructeurs
    // -----------------------

    /**
     * Constructeur de la frame
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
     * Permet de creer le panel du formulaire d'ajout de patient
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
     * Permet de creer panel confirmation
     * 
     * @return confirmPanel
     */
    private JPanel setConfirmPanel() {
        confirmPanel = new JPanel(new BorderLayout());
        recupDataCarteVital = new JButton(
                (String) loadingLanguage.getJsonObject()
                        .get("frame_admin_add_patient_loading_by_card_vital"));
        JPanel panelLeft = new JPanel(new FlowLayout());
        JPanel panelRight = new JPanel(new FlowLayout());
        signLabel = new JLabel((String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_sign"));
        signCheckBox = new JCheckBox();
        confirmButton = new JButton((String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_confirm"));
        confirmButton.setEnabled(false);
        cancelButton = new JButton((String) loadingLanguage.getJsonObject().get("frame_admin_add_patient_cancel"));

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
     * --------------------------
     * Listeners
     * --------------------------
     */

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

    /**
     * ---------------------------
     * Getters and setters
     * ---------------------------
     */

    /**
     * @return patientLastnameInputTextField
     */
    public JTextField getPatientLastnameInputTextField() {
        return patientLastnameInputTextField;
    }

    /**
     * @param patientLastnameInputTextField
     */
    public void setPatientLastnameInputTextField(JTextField patientLastnameInputTextField) {
        this.patientLastnameInputTextField = patientLastnameInputTextField;
    }

    /**
     * @return patientFirstnameInpuTextField
     */
    public JTextField getPatientFirstnameInpuTextField() {
        return patientFirstnameInpuTextField;
    }

    /**
     * @param patientFirstnameInpuTextField
     */
    public void setPatientFirstnameInpuTextField(JTextField patientFirstnameInpuTextField) {
        this.patientFirstnameInpuTextField = patientFirstnameInpuTextField;
    }

    /**
     * @return patientDateInputFormattedTextField
     */
    public JFormattedTextField getPatientDateInputFormattedTextField() {
        return patientDateInputFormattedTextField;
    }

    /**
     * @param patientDateInputFormattedTextField
     */
    public void setPatientDateInputFormattedTextField(JFormattedTextField patientDateInputFormattedTextField) {
        this.patientDateInputFormattedTextField = patientDateInputFormattedTextField;
    }

    /**
     * @return patientSecuNumberInputFormattedTextField
     */
    public JFormattedTextField getPatientSecuNumberInputFormattedTextField() {
        return patientSecuNumberInputFormattedTextField;
    }

    /**
     * @param patientSecuNumberInputFormattedTextField
     */
    public void setPatientSecuNumberInputFormattedTextField(
            JFormattedTextField patientSecuNumberInputFormattedTextField) {
        this.patientSecuNumberInputFormattedTextField = patientSecuNumberInputFormattedTextField;
    }

    /**
     * @return patientNumberPhoneInputFormFormattedTextField
     */
    public JFormattedTextField getPatientNumberPhoneInputFormFormattedTextField() {
        return patientNumberPhoneInputFormFormattedTextField;
    }

    /**
     * @param patientNumberPhoneInputFormFormattedTextField
     */
    public void setPatientNumberPhoneInputFormFormattedTextField(
            JFormattedTextField patientNumberPhoneInputFormFormattedTextField) {
        this.patientNumberPhoneInputFormFormattedTextField = patientNumberPhoneInputFormFormattedTextField;
    }

    /**
     * @return patientAddressInputTextField
     */
    public JTextField getPatientAddressInputTextField() {
        return patientAddressInputTextField;
    }

    /**
     * @param patientAddressInputTextField
     */
    public void setPatientAddressInputTextField(JTextField patientAddressInputTextField) {
        this.patientAddressInputTextField = patientAddressInputTextField;
    }

    /**
     * @return confirmButton
     */
    public JButton getConfirmButton() {
        return confirmButton;
    }

    /**
     * @param confirmButton
     */
    public void setConfirmButton(JButton confirmButton) {
        this.confirmButton = confirmButton;
    }
}