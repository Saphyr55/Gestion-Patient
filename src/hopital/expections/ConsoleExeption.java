/**
 * 
 */
package hopital.expections;

/**
 * Class expetion
 * 
 * @author Andy
 */
public class ConsoleExeption extends Exception {

	/**
	 * 
	 */
	public ConsoleExeption() {
	}

	/**
	 * @param message
	 */
	public ConsoleExeption(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConsoleExeption(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConsoleExeption(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ConsoleExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
