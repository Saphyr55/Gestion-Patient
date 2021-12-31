import windows.FrameConnection;
import windows.FrameHopitalStatistics;

/**
 * Permet de démmarer le logiciel
 * Contient seulement le main et ces les instances de la frame de connexion et
 * la frame de statistique de l'hopital
 * 
 * @author Andy
 */
public class Launch {
	
	public static void main(String[] args) {
		new FrameHopitalStatistics();
		new FrameConnection();
	}
}
