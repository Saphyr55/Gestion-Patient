package windows.technician;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import hopital.Consultation;
import hopital.Hopital;
import hopital.loading.dimens.LoadingDimens;
import hopital.loading.language.LoadingLanguage;
import hopital.patient.Patient;
import windows.FrameConnection;

public class FrameTechnician extends JFrame {

    /**
     * ----------------------
     * Attributs et options
     * ----------------------
     */

    /*
     * Charge de quoi charger les differents textes en differente langue dans
     * strings.json
     */
    private static LoadingLanguage loadingLanguage = FrameConnection.getLoadingLanguage();
    private static JSONObject strings = loadingLanguage.getJsonObject();
    private static LoadingDimens dimens = new LoadingDimens();

    public final static String title = (String) strings.get("frame_technician_title");
    private static final int width = (int) ((long) dimens.getJsonObject().get("frame_medecin_width"));
    private static final int height = (int) ((long) dimens.getJsonObject().get("frame_medecin_height"));

    private JPanel contentPane = (JPanel) this.getContentPane();

    /**
     * Fonts
     */
    private static Font font1 = new Font("SansSerif", Font.BOLD, 20);

    /**
     * Composant du JPanel de liste de patient
     */
    private JPanel panelListPatient;
    private JList<String> listPatient;
    private JScrollPane listPatientScrollPane;
    private JTextField foundPatientField;
    private DefaultListModel<String> namePatients = new DefaultListModel<>();
    private ArrayList<String> listNamePatient = new ArrayList<>();

    /**
     * 
     */
    private JPanel centerPanel;
    private JPanel patientDataPanel;
    private JTextField lastnameStringTextField, lastnamePatientTextField;
    private JTextField firstnameStringTextField, firstnamePatientTextField;
    private JTextField birthdayStringTextField;
    private JFormattedTextField birthdayPatientTextField;
    private JTextField secuNumberStringTextField;
    private JFormattedTextField secuNumberPatientTextField;
    private JTextField phoneStringTextField;
    private JFormattedTextField phonePatientTextField;
    private JTextField addressStringTextField, addressPatientTextField;
    private JPanel listRequestAppareillagePanel;
    private JList<CheckBoxList<?>> requestAppareillageList;
    private DefaultListModel<CheckBoxNode> requestAppareillageListModel;
    private JScrollPane appareillageListScrollPane;
    private JButton confirmRequestAppareillageButton;

    private JPanel consultationPanel;
    private JList<String> listConsultationJList;
    private DefaultListModel<String> nameListConsultationDefaultModel = new DefaultListModel<>();
    private ArrayList<String> nameConsultationList = new ArrayList<>();
    private JScrollPane listConsultationScrollPane = new JScrollPane();
    private JTextField foundConsultationField;

    /**
     * Masks
     */
    private static MaskFormatter dateFormatter;
    private static MaskFormatter secuNumbeFormatter;
    private static MaskFormatter phoneNumberFormatter;

    private static Patient currentPatient;
    private static Consultation currentConsultation;
    private static File appareillageFile;
    private static HashMap<String, Boolean> appareillageMap;

    /**
     * ---------------------------------------------------
     */

    /**
     * ----------------------------------------
     * Constucteur de la fenetre de technician
     * ----------------------------------------
     */
    public FrameTechnician() {
        super(title);
        Hopital.loadingPatient();
        currentPatient = Hopital.getPatients().get(0);
        setOptionFrame();
        contentPane.add(setListPatient(), BorderLayout.WEST);
        contentPane.add(setCenterPanel(), BorderLayout.CENTER);
        contentPane.add(setListConsultion(), BorderLayout.EAST);
        setVisible(true);
    }

    /**
     * Option de la frame
     */
    public void setOptionFrame() {
        try {
            UIManager.setLookAndFeel(FrameConnection.getModel());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        this.setSize(width, height);
        this.setLocationRelativeTo(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Permet d'afficher la liste de patient de l'hopital
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
        foundPatientField = new JTextField();

        String namePatientString;
        for (int i = 0; i < Hopital.getPatients().size(); i++) {
            namePatientString = Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getLastName()
                    .toUpperCase() + " "
                    + Hopital.getPatients().get(Hopital.getPatients().size() - 1 - i).getFirstName();
            namePatients.addElement(namePatientString);
            listNamePatient.add(namePatientString);
        }
        listPatient = new JList<>(namePatients);
        listPatientScrollPane = new JScrollPane(listPatient);
        foundPatientField.setPreferredSize(new Dimension(0, 20));

        panelListPatient.setPreferredSize(new Dimension(200, height));
        panelListPatient.setPreferredSize(new Dimension(200, height));
        panelListPatient.setMinimumSize(new Dimension(200, height));
        panelListPatient.setMaximumSize(new Dimension(200, height));
        panelTop.setPreferredSize(new Dimension(0, 40));
        foundPatientField.setPreferredSize(new Dimension(0, 100));
        panelTop.add(foundPatientField, BorderLayout.CENTER);
        panelListPatient.add(panelTop, BorderLayout.NORTH);
        panelListPatient.add(listPatientScrollPane, BorderLayout.CENTER);

        listPatient.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = listPatient.getSelectedIndex();
                currentPatient = Hopital.getPatients().get(Hopital.getPatients().size() - 1 - index);
                lastnamePatientTextField.setText(currentPatient.getLastName());
                firstnamePatientTextField.setText(currentPatient.getFirstName());
                birthdayPatientTextField.setText(currentPatient.getBirthday().format(Hopital.FORMATEUR_LOCALDATE)
                        .replace("-", ""));
                secuNumberPatientTextField.setText(currentPatient.getSecuNumber());
                phonePatientTextField.setText(currentPatient.getPhoneNumber());
                addressPatientTextField.setText(currentPatient.getAddress());
                loadingListConsultation(currentPatient);
                contentPane.revalidate();
                contentPane.repaint();
            }

        });

        foundPatientField.getDocument().addDocumentListener(new DocumentListenerPatientField());

        return panelListPatient;
    }

    /**
     * Charge toutes les ordonnances du dossier du patient en parametre
     * Puis ajoute le nom des fichier formatter
     * 
     * @param patient
     */
    private void loadingListConsultation(Patient patient) {

        /**
         * Si la liste est vide on la remplie
         */
        if (nameListConsultationDefaultModel.isEmpty()) {

            /**
             * Recuperation de toutes les consultations du patient
             */
            Hopital.loadingConsultationPatient(patient);

            for (int i = 0; i < patient.getConsultationsFile().size(); i++) {

                /**
                 * Formate le nom du fichier de le consultation
                 */
                String nameConsultation = patient.getConsultationsFile()
                        .get(patient.getConsultationsFile().size() - 1 - i)
                        .getName()
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

            loadingListConsultation(patient);
        }
    }

    /**
     * Permet d'afficher le panel central contenant le panel des données du patient
     * et ses demandes de d'appareillages de la consultation courante
     * 
     * @return centerPanel
     */
    private JPanel setCenterPanel() {
        centerPanel = new JPanel(new GridLayout(1, 2));

        centerPanel.add(setPanelData());
        centerPanel.add(setConsultationRequestPanel());

        return centerPanel;
    }

    /**
     * Affiche le panel de requete d'appareillage
     * Ce panel permet de cocher les appareillages de la consultation
     * et confirme les requette en appuyant sur le bouton confirmer qui vas modifier
     * les valeurs du fichier json contenant les reqettes appareillage
     * 
     * @return listRequestAppareillagePanel
     */
    private JPanel setConsultationRequestPanel() {

        listRequestAppareillagePanel = new JPanel(new BorderLayout());
        confirmRequestAppareillageButton = new JButton((String) strings.get("frame_technician_confirm_request"));

        if (requestAppareillageList != null) {
            appareillageListScrollPane = new JScrollPane(requestAppareillageList);
            listRequestAppareillagePanel.add(appareillageListScrollPane, BorderLayout.CENTER);
            listRequestAppareillagePanel.add(confirmRequestAppareillageButton, BorderLayout.SOUTH);
            listRequestAppareillagePanel.setPreferredSize(new Dimension((width / 4), height - 50));

            confirmRequestAppareillageButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        appareillageFile.delete();
                        appareillageFile.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try (OutputStreamWriter writer = new OutputStreamWriter(
                            new FileOutputStream(appareillageFile, true),
                            StandardCharsets.UTF_8)) {

                        writer.write(CheckBoxList.getJsonObjectAppareillage().toJSONString());
                        writer.close();
                        System.out.println("Appareillage confirmer");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
        }

        return listRequestAppareillagePanel;
    }

    /**
     * Convertie un fichier json comme clés des strings et comme valeurs des
     * booleans en map
     * 
     * @param file
     * @return map
     */
    private static Map<String, Boolean> appareilllageFileToHashMap(File file) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            jsonObject = (JSONObject) parser.parse(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        Map<String, Boolean> map = (Map<String, Boolean>) jsonObject.clone();
        return map;
    }

    /**
     * Permet d'afficher le panel des données du patient courant
     * 
     * @return patientDataPanel
     */
    private JPanel setPanelData() {

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

        patientDataPanel = new JPanel();
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
        secuNumberStringTextField = new JTextField((String) loadingLanguage.getJsonObject()
                .get("frame_medecin_secu_number"));
        secuNumberPatientTextField = new JFormattedTextField(secuNumbeFormatter);
        secuNumberStringTextField.setFont(font1);
        secuNumberPatientTextField.setFont(font1);

        // numero de telephone
        phoneStringTextField = new JTextField((String) strings.get("frame_medecin_phone_number"));
        phonePatientTextField = new JFormattedTextField(phoneNumberFormatter);
        phoneStringTextField.setFont(font1);
        phonePatientTextField.setFont(font1);

        // adresse
        addressStringTextField = new JTextField((String) strings.get("frame_medecin_address"));
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

        patientDataPanel.add(lastnameStringTextField);
        patientDataPanel.add(lastnamePatientTextField);

        patientDataPanel.add(firstnameStringTextField);
        patientDataPanel.add(firstnamePatientTextField);

        patientDataPanel.add(birthdayStringTextField);
        patientDataPanel.add(birthdayPatientTextField);

        patientDataPanel.add(secuNumberStringTextField);
        patientDataPanel.add(secuNumberPatientTextField);

        patientDataPanel.add(phoneStringTextField);
        patientDataPanel.add(phonePatientTextField);

        patientDataPanel.add(addressStringTextField);
        patientDataPanel.add(addressPatientTextField);

        patientDataPanel.setLayout(new BoxLayout(patientDataPanel, BoxLayout.PAGE_AXIS));
        patientDataPanel.setPreferredSize(new Dimension(width / 3, height - 50));

        return patientDataPanel;
    }

    /**
     * Affiche la liste de consultation et le texte pour rechercher une
     * consultation.
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
        foundConsultationField = new JTextField();

        /**
         * Option text sur le button et autre
         */
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

        listConsultationJList = new JList<>(nameListConsultationDefaultModel);
        listConsultationScrollPane = new JScrollPane(listConsultationJList);

        /**
         * Ajout du button d'ajout de patient et de recherche de patient dans le
         * panelTop
         * Et ajout du panelTop et de la listPatient dans le panelListPatient
         */
        panelTop.add(foundConsultationField, BorderLayout.CENTER);
        consultationPanel.add(panelTop, BorderLayout.NORTH);
        consultationPanel.add(listConsultationScrollPane, BorderLayout.CENTER);

        if (listConsultationJList != null) {
            listConsultationJList.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    JList<String> list = (JList<String>) event.getSource();
                    int index = list.locationToIndex(event.getPoint());
                    if (index >= 0) {
                        File currentConsultationFile = currentPatient.getConsultationsFile()
                                .get(currentPatient.getConsultationsFile().size() - 1 - index);
                        appareillageFile = new File("./src/log/patient/" +
                                currentPatient.getFirstName().toLowerCase()
                                + currentPatient.getLastName().toLowerCase() + "/" +
                                currentConsultationFile.getName()
                                + "/appareillage/" + currentConsultationFile.getName() + ".json");
                        appareillageMap = (HashMap<String, Boolean>) appareilllageFileToHashMap(appareillageFile);
                        requestAppareillageListModel = new DefaultListModel<>();
                        requestAppareillageList = new CheckBoxList(requestAppareillageListModel);
                        for (Entry<String, Boolean> element : appareillageMap.entrySet()) {
                            requestAppareillageListModel.addElement(new CheckBoxNode(element.getKey(),
                                    element.getValue()));
                        }
                        centerPanel.remove(listRequestAppareillagePanel);
                        centerPanel.add(setConsultationRequestPanel());
                        contentPane.revalidate();
                        contentPane.repaint();
                    }
                }
            });
        }

        foundConsultationField.getDocument().addDocumentListener(new DocumentListenerConsultationField());

        return consultationPanel;

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
     * --------------------
     * Getters and setters
     * --------------------
     */

    public static Patient getCurrentPatient() {
        return currentPatient;
    }

    public static void setCurrentPatient(Patient currentPatient) {
        FrameTechnician.currentPatient = currentPatient;
    }

    public static Consultation getCurrentConsultation() {
        return currentConsultation;
    }

    public void setCurrentConsultation(Consultation currentConsultation) {
        FrameTechnician.currentConsultation = currentConsultation;
    }

    public static File getAppareillageFile() {
        return appareillageFile;
    }

    public static void setAppareillageFile(File appareillageFile) {
        FrameTechnician.appareillageFile = appareillageFile;
    }

    public static HashMap<String, Boolean> getAppareillageMap() {
        return appareillageMap;
    }

    public static void setAppareillageMap(HashMap<String, Boolean> appareillageMap) {
        FrameTechnician.appareillageMap = appareillageMap;
    }

}