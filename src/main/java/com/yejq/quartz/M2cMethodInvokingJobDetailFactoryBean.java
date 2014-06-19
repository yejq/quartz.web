package com.yejq.quartz;

import java.net.InetAddress;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.util.MethodInvoker;

import com.yejq.scheduler.util.ProbizUtil;

public class M2cMethodInvokingJobDetailFactoryBean extends MethodInvokingJobDetailFactoryBean {
	protected static final Logger logger = LoggerFactory.getLogger(M2cMethodInvokingJobDetailFactoryBean.class);

	private static String serviceName = ProbizUtil.getString("service.name");

	@Override
	public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException {
		super.afterPropertiesSet();

		if (MethodInvokingJob.class.getName().equals(((JobDetail) super.getObject()).getJobClass().getName())) {
			((JobDetail) super.getObject()).setJobClass(M2cMethodInvokingJob.class);
		} else {
			((JobDetail) super.getObject()).setJobClass(M2cStatefulMethodInvokingJob.class);
		}
	}

	public static class M2cMethodInvokingJob extends MethodInvokingJob {
		protected static final Logger logger = LoggerFactory.getLogger(M2cMethodInvokingJob.class);
		private MethodInvoker methodInvoker;
		private String errorMessage;

		/**
		 * Set the MethodInvoker to use.
		 */
		public void setMethodInvoker(MethodInvoker methodInvoker) {
			this.methodInvoker = methodInvoker;
			this.errorMessage = "Could not invoke method '" + this.methodInvoker.getTargetMethod()
					+ "' on target object [" + this.methodInvoker.getTargetObject() + "]";
		}

		/**
		 * Invoke the method via the MethodInvoker.
		 */
		protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
			String taskName = methodInvoker.getTargetClass().getSimpleName();
			try {
				String hostName = InetAddress.getLocalHost().getHostName();
				String serviceName = M2cMethodInvokingJobDetailFactoryBean.serviceName;
				if (StringUtils.isNotBlank(hostName) && StringUtils.isNotBlank(serviceName)
						&& StringUtils.equals(hostName, serviceName)) {
					this.methodInvoker.invoke();
				}
			} catch (Exception ex) {
				logger.error(taskName + " accounted a error : " + this.errorMessage, ex);
				throw new JobExecutionException(this.errorMessage, ex, false);
			}
		}
	}

	public static class M2cStatefulMethodInvokingJob extends M2cMethodInvokingJob {
	}

}