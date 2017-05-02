package restful.context;

/**
 * @author André Schmer
 *
 */
public class RestfulContext {

	private static String token;

	public static void setToken(final String logintoken) {
		token = logintoken;
	}

	public static String getToken() {
		return token;
	}

}
