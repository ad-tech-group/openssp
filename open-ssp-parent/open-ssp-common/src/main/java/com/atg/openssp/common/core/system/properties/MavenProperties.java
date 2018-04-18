package com.atg.openssp.common.core.system.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author André Schmer
 *
 */
public class MavenProperties {

	private static final Logger log = LoggerFactory.getLogger(MavenProperties.class);

	private String version = "";

	/**
	 * Uses the resource path from class to load the pom.properties of the given groupID and artifactID. {final-name}, {groupId}, {artifactId}
	 */
	public MavenProperties() {
		try {
			final InputStream is = this.getClass().getResourceAsStream("/app.properties");
			final Properties properties = new Properties();
			properties.load(is);
			version = properties.getProperty("version");
		} catch (final IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Returns the version number from the Maven generated pom.properies or "" if not available.
	 * 
	 * @return version as String
	 */
	public String getVersion() {
		return version;
	}

}
