package windows;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatIntelliJLaf;

public class FrameHopitalStatistics extends JFrame {

    private static final String title = "Statistics Hopital";
    private static final int height = 500;
    private static final int width = 400;
    private static LookAndFeel model = new FlatIntelliJLaf();

    private JPanel contentPane = (JPanel) getContentPane();
    private JPanel dataPanel = new JPanel();

    private String[] stringsData = {
            "Nombre de patients", "Nombre de medecins", "Nombre de techiniciens",
            "Nombre d'administrateur", "Nombre de personnels" };
    private JPanel[] dataPanels = new JPanel[stringsData.length];
    private JLabel[] stringDataLabels = new JLabel[stringsData.length];
    private JLabel[] numberDataLabels = new JLabel[stringsData.length];
    private File[] files = {
            new File("./src/log/patient/patients.txt"), new File("./src/log/medecin/medecins.txt"),
            new File("./src/log/admin/admins.txt"), new File("./src/log/technician/technician.txt")
    };

    private static Font font1 = new Font("SansSerif", Font.BOLD, 20);
    private static Font font2 = new Font("SansSerif", Font.BOLD, 25);

    /**
     * Constructeur de la frame
     */
    public FrameHopitalStatistics() {
        super(title);
        setOptionFrame();
        contentPane.add(setDataPanel());
        pack();
        setVisible(true);
    }

    /**
     * Option de la frame
     */
    private void setOptionFrame() {
        try {
            UIManager.setLookAndFeel(model);
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        setSize(width, height);
        setResizable(true);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    /**
     * Creer panel avec les données de l'hopital
     * 
     * @return
     */
    private JPanel setDataPanel() {
        dataPanel = new JPanel();

        /**
         * Initialise les datas panels et les labels 
         */
        for (int i = 0; i < stringsData.length; i++) {
            dataPanels[i] = new JPanel(new FlowLayout());
            stringDataLabels[i] = new JLabel(stringsData[i]);
            stringDataLabels[i].setFont(font1);
            numberDataLabels[i] = new JLabel();
            numberDataLabels[i].setFont(font2);
            dataPanels[i].add(stringDataLabels[i]);
            dataPanels[i].add(numberDataLabels[i]);
            dataPanel.add(dataPanels[i]);
        }
        /**
         * Affiche le nombre de  
         */
        int totalPersonels = 0;
        for (int i = 0; i < files.length; i++) {
            if (i >= 1)
                totalPersonels += numberLines(files[i]);
            numberDataLabels[i].setText(String.valueOf(numberLines(files[i])));
        }
        numberDataLabels[numberDataLabels.length - 1].setText(String.valueOf(totalPersonels));
        
        /**
         * Update toutes les 5 secondes les données
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int totalPersonels = 0;
                for (int i = 0; i < files.length; i++) {
                    if (i >= 1)
                        totalPersonels += numberLines(files[i]);
                    numberDataLabels[i].setText(String.valueOf(numberLines(files[i])));
                }
                numberDataLabels[numberDataLabels.length - 1].setText(String.valueOf(totalPersonels));
                contentPane.revalidate();
                contentPane.repaint();
            }
        }, 5000, 5000);

        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.PAGE_AXIS));

        return dataPanel;
    }

    /**
     * Renvois le nombre de ligne d'un fichier 
     * 
     * @param file
     * @return number
     */
    private int numberLines(File file) {

        int number = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {

            while (reader.readLine() != null) {
                number++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

}
