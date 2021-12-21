package windows.technicien;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultMutableTreeNode;

import org.json.simple.JSONArray;
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
    private static LoadingDimens dimens = new LoadingDimens();

    public final static String title = "Technician - Univ Tours";
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
    private List<String> appareillageKeysList;
    private List<Boolean> appareillageValuesList;
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
            namePatientString = Hopital.getPatients().get(i).getLastName().toUpperCase() + " "
                    + Hopital.getPatients().get(i).getFirstName();
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

        return panelListPatient;
    }

    /**
     * 
     * @return centerPanel
     */
    private JPanel setCenterPanel() {
        centerPanel = new JPanel(new GridLayout(1, 2));

        centerPanel.add(setPanelData());
        centerPanel.add(setListConsultationRequest());

        return centerPanel;
    }

    private JPanel setListConsultationRequest() {

        listRequestAppareillagePanel = new JPanel();
        confirmRequestAppareillageButton = new JButton("Confimation des requette");
        currentPatient = Hopital.getPatients().get(0);
        Hopital.loadingConsultationPatient(currentPatient);

        File currentConsultationFile = Hopital.getPatients().get(0).getConsultationsFile().get(0);
        appareillageFile = new File("./src/log/patient/" + currentPatient.getFirstName().toLowerCase()
                + currentPatient.getLastName().toLowerCase() + "/" + currentConsultationFile.getName()
                + "/appareillage/" + currentConsultationFile.getName() + ".json");

        appareillageMap = (HashMap<String, Boolean>) appareilllageFileToHashMap(appareillageFile);
        requestAppareillageListModel = new DefaultListModel<>();
        requestAppareillageList = new CheckBoxList(requestAppareillageListModel);

        for (Entry<String, Boolean> element : appareillageMap.entrySet()) {
            requestAppareillageListModel.addElement(new CheckBoxNode(element.getKey(), element.getValue()));
        }

        appareillageListScrollPane = new JScrollPane(requestAppareillageList);
        listRequestAppareillagePanel.add(appareillageListScrollPane);
        listRequestAppareillagePanel.add(confirmRequestAppareillageButton);
        listRequestAppareillagePanel.setPreferredSize(new Dimension((width / 4), height - 50));

        confirmRequestAppareillageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (appareillageFile.delete()) {
                        System.out.println("Suppression reussi");
                    }
                    appareillageFile.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(appareillageFile, true),
                        StandardCharsets.UTF_8)) {

                    writer.write(CheckBoxList.getJsonObjectAppareillage().toJSONString());
                    writer.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });

        return listRequestAppareillagePanel;
    }

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
     * 
     * @return
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
        secuNumberStringTextField = new JTextField("Secu number");
        secuNumberPatientTextField = new JFormattedTextField(secuNumbeFormatter);
        secuNumberStringTextField.setFont(font1);
        secuNumberPatientTextField.setFont(font1);

        // numero de telephone
        phoneStringTextField = new JTextField("Phone number");
        phonePatientTextField = new JFormattedTextField(phoneNumberFormatter);
        phoneStringTextField.setFont(font1);
        phonePatientTextField.setFont(font1);

        // adresse
        addressStringTextField = new JTextField("Address");
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

        listConsultationJList = new JList<>(nameListConsultationDefaultModel);
        listConsultationScrollPane = new JScrollPane(listConsultationJList);

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

        /**
         * Ajout du button d'ajout de patient et de recherche de patient dans le
         * panelTop
         * Et ajout du panelTop et de la listPatient dans le panelListPatient
         */
        panelTop.add(foundConsultationField, BorderLayout.CENTER);
        consultationPanel.add(panelTop, BorderLayout.NORTH);
        consultationPanel.add(listConsultationScrollPane, BorderLayout.CENTER);

        return consultationPanel;
    }

    public static Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
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