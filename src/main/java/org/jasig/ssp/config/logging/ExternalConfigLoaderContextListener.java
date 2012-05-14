package org.jasig.ssp.config.logging;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple utility listener to load certain properties before Spring Starts up.
 * 
 * Add this entry to your web.xml:
 * 
 * <pre>
 * <listener>
 *     <listener-class>org.jasig.ssp.config.logging.ExternalConfigLoaderContextListener</listener-class>
 *   </listener>
 * </pre>
 */
public class ExternalConfigLoaderContextListener implements
		ServletContextListener {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExternalConfigLoaderContextListener.class);

	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		String configLocation = sce.getServletContext().getInitParameter(
				"SSP_CONFIGDIR");
		if (configLocation == null) {
			configLocation = System.getenv("SSP_CONFIGDIR")
					+ System.getProperty("path.separator");
		}

		try {
			new LogBackConfigLoader(configLocation + "logback.xml");
		} catch (Exception e) {
			LOGGER.error("Unable to read config file", e);
		}
	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		/**
		 * Nothing to do here
		 */
	}
}
