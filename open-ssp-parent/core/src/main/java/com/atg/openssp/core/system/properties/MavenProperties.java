package com.atg.openssp.core.system.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.atg.service.LogFacade;

/**
 * @author André Schmer
 *
 */
public class MavenProperties {

	private String version = "";

	/**
	 * Uses the resource path from class to load the pom.properties
	 * of the given groupID and artifactID. {final-name}, {groupId},
	 * {artifactId}
	 */
	public MavenProperties() {
		try {
			final InputStream is = this.getClass().getResourceAsStream("/app.properties");
			final Properties properties = new Properties();
			properties.load(is);
			version = properties.getProperty("version");
		} catch (final IOException e) {
			LogFacade.logException(getClass(), e.getMessage());
		}
	}

	/**
	 * Returns the version number from the Maven generated pom.properies or ""
	 * if not available.
	 * 
	 * @return version as String
	 */
	public String getVersion() {
		return version;
	}

}
