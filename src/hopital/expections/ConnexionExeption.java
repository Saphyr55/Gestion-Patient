package hopital.expections;

public class ConnexionExeption extends RuntimeException {

	public ConnexionExeption() {
		
	}
	
	public ConnexionExeption(String string) {
		super(string);
	}
	
}
