package windows.technicien;

import javax.swing.JFrame;
import javax.swing.UIManager;

import hopital.loading.language.Language;
import windows.FrameConnection;

public class FrameTechnician extends JFrame {

    public final static String title = "Technician - Univ Tours";
    public final static int height = 700;
    public final static int width = 1000;

    public FrameTechnician() {
        super(title);
        setOptionFrame();
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
}