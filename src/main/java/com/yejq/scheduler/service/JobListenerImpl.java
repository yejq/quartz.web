package com.yejq.scheduler.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

import com.yejq.scheduler.util.SchedulerUtil;

public class JobListenerImpl implements JobListener {
	private final static Log logger = LogFactory.getLog(JobListenerImpl.class);
	//private final static String EMAIL_TEMPLATE = "system/bugReportEmail.vm";
	private String name = null;

	/**
	 * <p>
	 * Get the name of the <code>JobListener</code>.
	 * </p>
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a
	 * <code>{@link org.quartz.JobDetail}</code> was about to be executed (an
	 * associated <code>{@link Trigger}</code> has occured), but a
	 * <code>{@link TriggerListener}</code> vetoed it's execution.
	 * </p>
	 * 
	 * @see #jobToBeExecuted(JobExecutionContext)
	 */
	public void jobExecutionVetoed(JobExecutionContext context) {
	}

	/**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a
	 * <code>{@link org.quartz.JobDetail}</code> is about to be executed (an
	 * associated <code>{@link Trigger}</code> has occured).
	 * </p>
	 * 
	 * <p>
	 * This method will not be invoked if the execution of the Job was vetoed by
	 * a <code>{@link TriggerListener}</code>.
	 * </p>
	 * 
	 * @see #jobExecutionVetoed(JobExecutionContext)
	 */
	public void jobToBeExecuted(JobExecutionContext context) {
	}

	/**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> after a
	 * <code>{@link org.quartz.JobDetail}</code> has been executed, and be for
	 * the associated <code>Trigger</code>'s <code>triggered(xx)</code> method
	 * has been called.
	 * </p>
	 */
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		if (jobException != null) {
			// 记录错误
			SchedulerUtil.addErrorJob(context.getTrigger().getName(), jobException);
			logger.error(jobException);
		}
		else {
			SchedulerUtil.removeErrorJob(context.getTrigger().getName());
		}
	}

	public void setName(String avalue) {
		name = avalue;
	}
}
