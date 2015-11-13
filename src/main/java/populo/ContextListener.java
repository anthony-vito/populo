package populo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

/**
 * A custom context listener for the application. We'll use it to perform
 * initializations and clean ups that require access to the servlet container
 * environment objects.
 *
 * Functions:
 *   Adjusts the Servlet Context to allows ONLY cookies for session tracking
 *   mode. I really dislike path parameters for JSESSION ids.
 *
 * @author amv
 */
public class ContextListener implements ServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(ContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		log.debug("Populo Context Listener: Initialized");

		servletContextEvent.getServletContext().setSessionTrackingModes
				(EnumSet.of(SessionTrackingMode.COOKIE));
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		log.debug("Populo Context Listener: Destroyed");
	}

}
