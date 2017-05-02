package restful.exception;

/**
 * @author André Schmer
 *
 */
public class RestException extends Exception {

	private static final long serialVersionUID = -980000547004160218L;

	public RestException(final String message) {
		super(message);
	}

}
