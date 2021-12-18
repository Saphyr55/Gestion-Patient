package windows.medecin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import hopital.Hopital;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import hopital.patient.Patient.PatientTypeCreate;
import hopital.personnels.Medecin;
import windows.FrameConnection;

public class FrameAddPatientWithMedecin extends JFrame {

    private static LoadingDimens loadingDimens = FrameConnection.getLoadingDimens();
    private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();

    private static Medecin currentMedecin = FrameConnection.getCurrentMedecin();
    private static Patient currentPatient;
    private static String lastnameCurrentPatient = "";
    private static String firstnameCurrentPatient = "";
    private static String birthdayCurrentPatient = "00/00/0000";
    private static String secuNumberCurrentPatient = "000 000 000 000 000 000";

    private JPanel contentPane = (JPanel) getContentPane();

    /**
     * 
     */
    private static final String frame_medecin_add_patient_lastname_patient = (String) loadingLanguage.getJsonObject()
            .get("frame_medecin_add_patient_lastname_patient");
    private static final String frame_medecin_add_patient_firstname_patient = (String) loadingLanguage.getJsonObject()
            .get("frame_medecin_add_patient_firstname_patient");
    private static final String frame_medecin_add_patient_birthday_patient = (String) loadingLanguage.getJsonObject()
            .get("frame_medecin_add_patient_birthday_patient");
    private static final String frame_medecin_add_patient_secu_number_patient = (String) loadingLanguage.getJsonObject()
            .get("frame_medecin_add_patient_secu_number_patient");
    private static final String frame_medecin_add_patient_add = (String) loadingLanguage.getJsonObject()
            .get("frame_medecin_add_patient_add");
    private static final String frame_medecin_add_patient_cancel = (String) loadingLanguage.getJsonObject()
            .get("frame_medecin_add_patient_cancel");

    /**
     * Partie recherche d'un patient
     */
    private JPanel researchPatientPanel;
    private JList<String> listPatientHopitalWithoutListPatientCurrentMedecin;
    private ArrayList<String> listPatientName = new ArrayList<>();
    private DefaultListModel<String> listPatientModel = new DefaultListModel<>();
    private JScrollPane listPatientHopitalWithoutListPatientCurrentMedecinScrollPane = new JScrollPane();
    private JTextField researchPatientTextField;

    /**
     * 
     */
    private JPanel centerPanel, dataPatientPanel, confirmAddPatientPanel;
    private JPanel lastnamePanel, firstnamePanel, birthdayPanel, secuNumberPanel;
    private JLabel lastnameLabel, firstnameLabel, birthdayLabel, secuNumberLabel;
    private JLabel lastnamePatient, firstnamePatient, birthdayPatient, secuNumberPatient;
    private JButton addButton, cancelButton;

    /**
     * Variables d'options pour la fenetre de gestion
     */
    private static final String title = (String) loadingLanguage.getJsonObject()
            .get("frame_medecin_add_patient_title");
    private final static int width = (int) ((long) loadingDimens.getJsonObject()
            .get("frame_medecin_add_patient_width"));
    private final static int height = (int) ((long) loadingDimens.getJsonObject()
            .get("frame_medecin_add_patient_width"));

    /**
     * Construteur de la frame
     */
    public FrameAddPatientWithMedecin() {
        super(title);
        Hopital.loadingPatient();
        setOptionFrame();

        contentPane.setLayout(new BorderLayout());
        contentPane.add(setPanelResearchPatient(), BorderLayout.WEST);
        contentPane.add(setCenterPanel(), BorderLayout.CENTER);

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
        this.setSize(width, height);
        this.setResizable(false);
        this.setLocationRelativeTo(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * @return researchPatientPanel le panel de panel recherche d'un patient
     */
    private JPanel setPanelResearchPatient() {

        researchPatientPanel = new JPanel(new BorderLayout());
        researchPatientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        researchPatientPanel.setPreferredSize(new Dimension(220, height - 10));

        researchPatientTextField = new JTextField();

        String namePatientString;
        for (int i = 0; i < Hopital.getPatients().size(); i++) {
            namePatientString = Hopital.getPatients().get(i).getLastName().toUpperCase() + " " +
                    Hopital.getPatients().get(i).getFirstName();
            listPatientModel.addElement(namePatientString);
            listPatientName.add(namePatientString);
        }

        listPatientHopitalWithoutListPatientCurrentMedecin = new JList<>(listPatientModel);
        listPatientHopitalWithoutListPatientCurrentMedecinScrollPane = new JScrollPane(
                listPatientHopitalWithoutListPatientCurrentMedecin);

        researchPatientPanel.add(listPatientHopitalWithoutListPatientCurrentMedecinScrollPane, BorderLayout.CENTER);
        researchPatientPanel.add(researchPatientTextField, BorderLayout.NORTH);

        /**
         * Change les donnée en recuperant l'index de liste de patient de l'hopital
         * et applique ces données
         */
        listPatientHopitalWithoutListPatientCurrentMedecin.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = listPatientHopitalWithoutListPatientCurrentMedecin.getSelectedIndex();
                for (int i = 0; i < Hopital.getPatients().size(); i++) {
                    if (i == index) {
                        currentPatient = Hopital.getPatients().get(i);
                        lastnameCurrentPatient = currentPatient.getLastName();
                        firstnameCurrentPatient = currentPatient.getFirstName();
                        birthdayCurrentPatient = currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE);
                        secuNumberCurrentPatient = currentPatient.getSecuNumber().replaceAll("(.{" + "3" + "})", "$1 ")
                                .trim();
                        break;
                    }
                }
                lastnamePatient.setText(lastnameCurrentPatient);
                firstnamePatient.setText(firstnameCurrentPatient);
                birthdayPatient.setText(birthdayCurrentPatient);
                secuNumberPatient.setText(secuNumberCurrentPatient);
                contentPane.revalidate();
            }
        });

        /**
         * Filtre la liste de patients de l'hopital avec le texte ecrit
         * dans la bar de recherche
         */
        researchPatientTextField.getDocument().addDocumentListener(new DocumentListener() {

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
                String filter = researchPatientTextField.getText();
                filterModel((DefaultListModel<String>) listPatientHopitalWithoutListPatientCurrentMedecin.getModel(),
                        filter);
            }
        });

        return researchPatientPanel;
    }

    /**
     * Filtre de menu de recherche d'un patient
     * 
     * @param model
     * @param filter
     */
    private void filterModel(DefaultListModel<String> model, String filter) {
        for (String patientName : listPatientName) {
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
     * Return le panel central de la fenetre
     * Permet d'afficher certaines données du patient
     * 
     * @return centerPanel
     */
    private JPanel setCenterPanel() {

        /**
         * Nom panel
         */
        lastnamePanel = new JPanel();
        lastnameLabel = new JLabel(frame_medecin_add_patient_lastname_patient);
        lastnamePatient = new JLabel(lastnameCurrentPatient);
        lastnamePanel.add(lastnameLabel);
        lastnamePanel.add(lastnamePatient);
        lastnamePanel.setLayout(new BoxLayout(lastnamePanel, BoxLayout.Y_AXIS));

        /**
         * Prenom panel
         */
        firstnamePanel = new JPanel();
        firstnameLabel = new JLabel(frame_medecin_add_patient_firstname_patient);
        firstnamePatient = new JLabel(firstnameCurrentPatient);
        firstnamePanel.add(firstnameLabel);
        firstnamePanel.add(firstnamePatient);
        firstnamePanel.setLayout(new BoxLayout(firstnamePanel, BoxLayout.Y_AXIS));
        firstnamePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        /**
         * Anniverssaire panel
         */
        birthdayPanel = new JPanel();
        birthdayLabel = new JLabel(frame_medecin_add_patient_birthday_patient);
        birthdayPatient = new JLabel(birthdayCurrentPatient);
        birthdayPanel.add(birthdayLabel);
        birthdayPanel.add(birthdayPatient);
        birthdayPanel.setLayout(new BoxLayout(birthdayPanel, BoxLayout.Y_AXIS));
        birthdayPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        /**
         * Secu number panel
         */
        secuNumberPanel = new JPanel();
        secuNumberLabel = new JLabel(frame_medecin_add_patient_secu_number_patient);
        secuNumberPatient = new JLabel(secuNumberCurrentPatient);
        secuNumberPanel.add(secuNumberLabel);
        secuNumberPanel.add(secuNumberPatient);
        secuNumberPanel.setLayout(new BoxLayout(secuNumberPanel, BoxLayout.Y_AXIS));
        secuNumberPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        /**
         * data patient panel
         */
        dataPatientPanel = new JPanel();
        dataPatientPanel.add(lastnamePanel);
        dataPatientPanel.add(firstnamePanel);
        dataPatientPanel.add(birthdayPanel);
        dataPatientPanel.add(secuNumberPanel);
        dataPatientPanel.setLayout(new BoxLayout(dataPatientPanel, BoxLayout.PAGE_AXIS));
        dataPatientPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));

        /**
         * panel to confirm or to cancel
         */
        confirmAddPatientPanel = new JPanel();

        addButton = new JButton(frame_medecin_add_patient_add);
        addButton.addActionListener(new AddButtonListener());
        cancelButton = new JButton(frame_medecin_add_patient_cancel);
        cancelButton.addActionListener(new FrameMedecin.CancelButtonFrameAddPatientListener());

        confirmAddPatientPanel.add(addButton);
        confirmAddPatientPanel.add(cancelButton);

        /**
         * panel centrale
         */
        centerPanel = new JPanel(new BorderLayout());

        centerPanel.add(dataPatientPanel, BorderLayout.CENTER);
        centerPanel.add(confirmAddPatientPanel, BorderLayout.SOUTH);

        return centerPanel;
    }

    /**
     * Action lors du clique du button ajouter
     */
    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int j = 0;
            for (int i = 0; i < currentMedecin.getPatients().size(); i++) {
                // condition si le patient n'existe pas dans la liste de medecin
                if (!currentPatient.getFirstName().equals(currentMedecin.getPatients().get(i).getFirstName()) &&
                        !currentPatient.getLastName().equals(currentMedecin.getPatients().get(i).getLastName()) &&
                        currentPatient.getIdentifiant() != currentMedecin.getPatients().get(i).getIdentifiant())
                    j++; // on incremente j et si j est egale a la taille de liste de patient du medecin
                         // alors on ajoute le patient
            }
            if (j == currentMedecin.getPatients().size()) {
                new Patient(currentPatient.getIdentifiant(), currentMedecin,
                        currentPatient.getFirstName(), currentPatient.getLastName(),
                        currentPatient.getBirthday(), currentPatient.getSecuNumber(),
                        currentPatient.getPhoneNumber(), currentPatient.getAddress(),
                        PatientTypeCreate.CREATE_PATIENT_WITH_MEDECIN);
                JOptionPane.showMessageDialog(contentPane, "Le patient à été bien ajouté");
            } else
                JOptionPane.showMessageDialog(contentPane, "Le patient existe déjà dans votre liste");

        }

    }

}
