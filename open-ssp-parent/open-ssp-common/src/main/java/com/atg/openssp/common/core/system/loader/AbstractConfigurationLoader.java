package com.atg.openssp.common.core.system.loader;

import com.atg.openssp.common.annotation.RuntimeMeta;
import com.atg.openssp.common.annotation.Scope;
import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.watchdog.DynamicLoadable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.ProjectProperty;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author André Schmer
 */
public abstract class AbstractConfigurationLoader implements DynamicLoadable {

    private static final Logger log = LoggerFactory.getLogger(AbstractConfigurationLoader.class);

    private Properties properties;
    private final String propertiesFile;
    private final CountDownLatch latch;

    AbstractConfigurationLoader(final String propertiesFile) {
        this(propertiesFile, null);
    }

    AbstractConfigurationLoader(final String propertiesFile, final CountDownLatch cdl) {
        log.info("using: " + propertiesFile);
        this.propertiesFile = propertiesFile;
        latch = cdl;
    }

    /**
     * Reads the values from the properties file and loads services behind the values, if necessary.
     */
    @Override
    public void readValues() {
        loadProperties();
        final Set<Object> keys = properties.keySet();

        for (final Object object : keys) {
            final String key = (String) object;
            String value = properties.getProperty(key);
            final ContextProperties contextProps = ContextProperties.get(key);
            if (contextProps != null) {
                try {
                    final Field propertyField = ContextProperties.class.getDeclaredField(contextProps.name());
                    final boolean isPrintable = propertyField.getAnnotation(RuntimeMeta.class).printable();
                    if (isPrintable) {
                        final Scope type = propertyField.getAnnotation(RuntimeMeta.class).type();
                        final String typeValue = type != null ? type.getValue() : "";
                        log.info(typeValue + ": " + contextProps + "=" + value);
                    }
                    ContextCache.instance.put(contextProps, value);
                } catch (final NoSuchFieldException | SecurityException e) {
                    log.error(e.getMessage(), e);
                } catch (final Exception e) {
                    log.error(e.getMessage() + ":" + key, e);
                }
                readSpecials(contextProps, value);
            }
        }

        finalWork();
        if (latch != null) {
            latch.countDown();
        }
    }

    @Override
    public String getResourceFilename() {
        return propertiesFile;
    }

    @Override
    public String getResourceLocation() {
        try {
            return ProjectProperty.getPropertiesResourceLocation();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Template method for special loader jobs to implement by subclass.
     *
     * @param key
     * @param value
     */
    protected void readSpecials(final ContextProperties key, final String value) {
    }

    protected abstract void finalWork();

    private void loadProperties() {
        try {
            if (StringUtils.isNotEmpty(propertiesFile)) {
                properties = ProjectProperty.getRuntimeProperties(propertiesFile);
            }
        } catch (final PropertyException e) {
            log.error(e.getMessage());
            throw new RuntimeException("property file could not be found: " + propertiesFile);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("property file could not be loaded: " + propertiesFile);
        }
    }

}
