package com.yejq;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;

import com.yejq.scheduler.util.ContextUtil;

/**
 * <p>
 * StartupListener class used to initialize the application and settings. Keep
 * in mind that this listener is executed outside of OpenSessionInViewFilter, so
 * if you're using Hibernate you'll have to explicitly initialize all loaded
 * data at the Dao or service level to avoid LazyInitializationException.
 * Hibernate.initialize() works well for doing this.
 * 
 * @web.listener
 */
public class StartupListener extends ContextLoaderListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ContextUtil.getInstance().cleanup();

		super.contextDestroyed(event);

		Log logger = LogFactory.getLog(StartupListener.class);
		if (logger.isDebugEnabled()) {
			logger.info("Application cleanup [OK].");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		Log logger = LogFactory.getLog(StartupListener.class);
		logger.info("Starting application ...");
		// call Spring's context ContextLoaderListener to initialize
		// all the context files specified in web.xml
		try {
			super.contextInitialized(event);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException("Spring context failed to startup.", e);
		}

		ContextUtil.getInstance().init(servletContext);
		logger.info("Application started [OK].");
	}
}
