package windows.admin;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarculaLaf;

import windows.FrameConnection;

public class FrameAdmin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private static final int width = 1080;
	private static final int height = 720;
	private static final String title = "Admin Gestion";
	private static boolean isVisible = true;
	
	
	/**
	 * 
	 */
	private JPanel contentPanel = (JPanel) getContentPane();

	
	/**
	 * 
	 */
	public FrameAdmin() {
		super(title);
		setOptionFrame(); 
		this.setVisible(isVisible);
	}

	/**
	 *
	 */
	private void setOptionFrame() {
		try {
			UIManager.setLookAndFeel(FrameConnection.getModel());
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}
		setSize(width, height);
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} 
	
}
