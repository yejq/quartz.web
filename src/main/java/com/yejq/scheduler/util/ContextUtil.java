/*
 * Created on Aug 29, 2006
 * 
 */

package com.yejq.scheduler.util;

import java.net.InetAddress;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 对整个应用环境的集中管理，包括初始化，刷新，和访问。
 * 
 * @author Legend
 * 
 */
public final class ContextUtil {
	private final static ContextUtil instance = new ContextUtil();

	private final Log logger = LogFactory.getLog(ContextUtil.class);

	private ServletContext servletContext = null;

	private ApplicationContext springContext = null;

	private ContextUtil() {
	}

	public final static ContextUtil getInstance() {
		return instance;
	}

	public static ServletContext getServletContext() {
		return getInstance().servletContext;
	}

	public static Object getSpringBeanById(String beanId) {
		return getInstance().springContext.getBean(beanId);
	}

	public static ApplicationContext getSpringContext() {
		return getInstance().springContext;
	}

	private void autoStartScheduler() throws Exception {
		if (ProbizUtil.getString("quartz.autoStartScheduler").equals("true")) {
			String hostName = InetAddress.getLocalHost().getHostName();
			String serviceName = ProbizUtil.getString("service.name");
			Scheduler scheduler = getScheduler();
			if (StringUtils.isBlank(hostName) || !StringUtils.equals(hostName, serviceName)) {
				scheduler.standby();
				logger.info("service.name=" + serviceName + " is not equals to " + hostName
						+ ", so scheduler is not auto start, only standby.");
			} else {
				startScheduler();
			}
		} else {
			logger.info("quartz.autoStartScheduler is not set or setted to false, so scheduler is not auto start.");
		}
	}

	public void cleanup() {
		this.servletContext = null;
		this.springContext = null;
	}

	private Scheduler getScheduler() {
		return (Scheduler) springContext.getBean("scheduler");
	}

	public void init(ServletContext servletContext) {
		this.servletContext = servletContext;
		this.springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

		try {
			autoStartScheduler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdownScheduler() {
		try {
			getScheduler().shutdown();
		} catch (SchedulerException e) {
			logger.error("Shutdown Scheduler Error!", e);
		}
	}

	public void startScheduler() {
		try {
			SchedulerUtil.startupScheduler(getScheduler());
		} catch (SchedulerException e) {
			throw new RuntimeException("Start Scheduler Error!", e);
		}
	}
}
