package parse;

/**
 * Thrown when the parser encounters invalid data.
 * @author Sara Tari & Adrien Droguet
 *
 */
public class ParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2773964788962780478L;

	public ParserException(String message) {
		super(message);
	}
}
