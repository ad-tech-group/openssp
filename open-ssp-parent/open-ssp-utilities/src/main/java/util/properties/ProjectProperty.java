package util.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import javax.xml.bind.PropertyException;

/**
 * @author André Schmer
 *
 */
public class ProjectProperty {

	/**
	 * Returns user properties from path under ${catalina.home}. The user Properties must comply with standard properties rules.
	 *
	 * @param propertiesPath
	 *            the path to user property
	 * @return Properties {@Link Properties}
	 * @throws PropertyException
	 *
	 */
	// TODO: resolve dependencies to catalina.home - make more generic
	public static Properties loadProperties(final String propertiesPath) throws PropertyException {
		if (propertiesPath == null || propertiesPath.length() == 0) {
			throw new PropertyException("propertiesPath may not be empty or null");
		}
		final String catalinaHome = System.getProperty("catalina.home", System.getProperty("user.dir"));
		String propPath = propertiesPath.replace("\\", "/").replaceAll("/+", "/");
		if (propPath.indexOf("/") == -1) {
			propPath = "/" + propPath;
		}

		final String propertyPath = catalinaHome + propPath;
		final Properties properties = new Properties();
		try (FileInputStream fi = new FileInputStream(new File(propertyPath));) {
			properties.load(fi);
		} catch (final IOException e) {
			throw new PropertyException("properties not found at this location");
		}
		return properties;
	}

	/**
	 * Return runtime properties of given {@code runtimeProperties}. Looks first, if application is running on a dev machine and returns in this case the
	 * properties from the resource context. Otherwise a lookup in properties folder in webapps context is fullfilled.
	 *
	 * @param runtimeProperties
	 * @return properties {@link Properties}
	 * @throws PropertyException
	 */
	public static Properties getRuntimeProperties(final String runtimeProperties) throws PropertyException {
		if (runtimeProperties == null || runtimeProperties.length() == 0) {
			throw new PropertyException("path may not be empty or null");
		}

		final Properties properties = new Properties();
		final String fileType = runtimeProperties.substring(runtimeProperties.lastIndexOf(".") + 1, runtimeProperties.length());
		final File propFile = readFile(runtimeProperties);
		try (FileInputStream fi = new FileInputStream(propFile);) {
			if ("xml".equals(fileType)) {
				properties.loadFromXML(fi);
			} else {
				properties.load(fi);
			}
		} catch (final IOException e) {
			System.err.println(ProjectProperty.class.getSimpleName() + " IOException for " + runtimeProperties + " " + e.getMessage());
		}
		return properties;
	}

	public static File readFile(final String file) throws PropertyException {
		File propFile = null;
		try {
			String propPath = file.replace("\\", "/").replaceAll("/+", "/");
			if (propPath.indexOf("/") == -1) {
				propPath = "/" + propPath;
			}
			String toDecode = null;

			final String catalinaHome = System.getProperty("catalina.home", System.getProperty("user.dir"));
			final File f = new File(catalinaHome + "/properties" + propPath);
			if (!f.exists()) {
				toDecode = Thread.currentThread().getContextClassLoader().getResource("").getFile() + propPath;
				// if (StringUtils.isEmpty(catalinaHome) || catalinaHome.contains("workspace") || catalinaHome.contains("git")) {

			} else {
				toDecode = catalinaHome + "/properties" + propPath;
			}
			propFile = new File(URLDecoder.decode(toDecode, "UTF-8"));
			if (!propFile.exists()) {
				throw new FileNotFoundException();
			}
		} catch (final IOException e) {
			throw new PropertyException(e.getMessage());
		}
		return propFile;
	}

	// /**
	// * Reads an alternative properties file in case of original cannot be found
	// * e.g. for running app at local machine.
	// *
	// * @param clazz,
	// * prop
	// * @return properties {@link Properties}
	// * @throws PropertyException
	// */
	// public static Properties loadConnectionPropertyAlternate(final String prop) throws PropertyException {
	// // final String prop = "/connection.properties";
	// Properties properties = null;
	// try (FileInputStream stream = new FileInputStream(prop)) {
	// properties = new Properties();
	// properties.load(stream);
	// } catch (final IOException e) {
	// throw new PropertyException(e.getMessage());
	// }
	// return properties;
	// }

	/**
	 *
	 * @return Location of the application properties resource repository as URL
	 * @throws PropertyException
	 */
	public static String getPropertiesResourceLocation() throws PropertyException {
		return getResourceURL("properties");
	}

	/**
	 *
	 * @return Location of the application security resource repository as URL
	 * @throws PropertyException
	 */
	public static String getSecurityResourceLocation() throws PropertyException {
		return getResourceURL("security");
	}

	private static String getResourceURL(final String type) throws PropertyException {
		final String catalinaHome = System.getProperty("catalina.home", System.getProperty("user.dir"));
		if (catalinaHome.contains("workspace") || catalinaHome.contains("git")) {
			return catalinaHome+"/properties".replace("/C:", "C:");
		} else {
			try {
				return new URL(URLDecoder.decode("file://" + catalinaHome + "/" + type, "UTF-8")).getPath() + "/";
			} catch (final MalformedURLException | UnsupportedEncodingException e) {
				throw new PropertyException(e.getMessage());
			}
		}
	}

	/**
	 *
	 * @param property
	 * @return
	 * @throws PropertyException
	 */
	public static Properties loadRuntimeProperties(final String property) throws PropertyException {
		final String fileType = property.substring(property.lastIndexOf(".") + 1, property.length());
		final String path = Thread.currentThread().getContextClassLoader().getResource(property).getPath();
		File propFile;
		try {
			propFile = new File(URLDecoder.decode(path, "UTF-8"));
		} catch (final UnsupportedEncodingException e1) {
			throw new PropertyException(e1.getMessage());
		}
		final Properties properties = new Properties();
		try (FileInputStream fi = new FileInputStream(propFile);) {
			if ("xml".equals(fileType)) {
				properties.loadFromXML(fi);
			} else {
				properties.load(fi);
			}
		} catch (final IOException e) {
			System.err.println(ProjectProperty.class.getSimpleName() + " IOException for " + property + " " + e.getMessage());
		}

		return properties;
	}
}
