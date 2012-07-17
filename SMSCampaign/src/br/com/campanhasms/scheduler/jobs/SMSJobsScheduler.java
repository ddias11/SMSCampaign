package br.com.campanhasms.scheduler.jobs;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import br.com.campanhasms.properties.JobsScheduleProperties;

public class SMSJobsScheduler {
	private static final String MAIL_MESSAGES_RECEIVED_JOB_GROUP_KEY = "MAIL_MESSAGES_RECEIVED_JOB_GROUP_KEY";
	private static final String MAIL_MESSAGES_RECEIVED_JOB_KEY = "MAIL_MESSAGES_RECEIVED_JOB_KEY";
	private static final String MAIL_MESSAGES_RECEIVED_TRIGGER_GROUP_KEY = "MAIL_MESSAGES_RECEIVED_TRIGGER_GROUP_KEY";
	private static final String MAIL_MESSAGES_RECEIVED_TRIGGER_KEY = "MAIL_MESSAGES_RECEIVED_TRIGGER_KEY";

	private static final String QUERY_REMAIN_CREDIT_JOB_GROUP_KEY = "QUERY_REMAIN_CREDIT_JOB_GROUP_KEY";
	private static final String QUERY_REMAIN_CREDIT_JOB_KEY = "QUERY_REMAIN_CREDIT_JOB_KEY";
	private static final String QUERY_REMAIN_CREDIT_TRIGGER_GROUP_KEY = "QUERY_REMAIN_CREDIT_TRIGGER_GROUP_KEY";
	private static final String QUERY_REMAIN_CREDIT_TRIGGER_KEY = "QUERY_REMAIN_CREDIT_TRIGGER_KEY";
	private static Scheduler scheduler = null;

	private static final String SMS_SEND_JOB_GROUP_KEY = "SMS_SEND_JOB_GROUP_KEY";
	private static final String SMS_SEND_JOB_KEY = "SMS_SEND_JOB_KEY";
	private static final String SMS_SEND_TRIGGER_GROUP_KEY = "SMS_SEND_TRIGGER_GROUP_KEY"; //$NON-NLS-1$
	private static final String SMS_SEND_TRIGGER_KEY = "SMS_SEND_TRIGGER_KEY"; //$NON-NLS-1$
	private static final String SYSTEM_PREVAYLER_MODEL_SNAPSHOT_JOB_GROUP_KEY = "SYSTEM_PREVAYLER_MODEL_SNAPSHOT_JOB_GROUP_KEY"; //$NON-NLS-1$
	private static final String SYSTEM_PREVAYLER_MODEL_SNAPSHOT_JOB_KEY = "SYSTEM_PREVAYLER_MODEL_SNAPSHOT_JOB_KEY"; //$NON-NLS-1$
	private static final String SYSTEM_PREVAYLER_MODEL_SNAPSHOT_TRIGGER_GROUP_KEY = "SYSTEM_PREVAYLER_MODEL_SNAPSHOT_TRIGGER_GROUP_KEY"; //$NON-NLS-1$
	private static final String SYSTEM_PREVAYLER_MODEL_SNAPSHOT_TRIGGER_KEY = "SYSTEM_PREVAYLER_MODEL_SNAPSHOT_TRIGGER_KEY"; //$NON-NLS-1$
	
	private static final Logger LOGGER = Logger.getLogger(SMSJobsScheduler.class);

	public static Scheduler getScheduler() throws SchedulerException {
		if (scheduler == null) {
			scheduler = new StdSchedulerFactory().getScheduler();
		}

		return scheduler;
	}

	public static void scheduleMailMessagesReceivedJob() throws SchedulerException {
		
		LOGGER.info("Scheduling MailMessagesReceivedJob at " + JobsScheduleProperties
				.getString("JobsScheduler.MAIL_NOTIFICATION_SCHEDULE"));
		JobDetail job = JobBuilder.newJob(MailMessagesReceivedJob.class)
				.withIdentity(MAIL_MESSAGES_RECEIVED_JOB_KEY, MAIL_MESSAGES_RECEIVED_JOB_GROUP_KEY).build();

		Trigger trigger = null;
		trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(MAIL_MESSAGES_RECEIVED_TRIGGER_KEY, MAIL_MESSAGES_RECEIVED_TRIGGER_GROUP_KEY)
				.withSchedule(
						CronScheduleBuilder.cronSchedule(JobsScheduleProperties
								.getString("JobsScheduler.MAIL_NOTIFICATION_SCHEDULE"))).startNow().build(); //$NON-NLS-1$
		getScheduler().scheduleJob(job, trigger);

	}

	public static void scheduleQueryRemainCreditJob() throws SchedulerException {
		LOGGER.info("Scheduling QueryRemainCreditJob at " + JobsScheduleProperties
				.getString("JobsScheduler.QUERY_REMAIN_CREDIT_SCHEDULE"));
		JobDetail job = JobBuilder.newJob(QueryRemainCreditJob.class)
				.withIdentity(QUERY_REMAIN_CREDIT_JOB_KEY, QUERY_REMAIN_CREDIT_JOB_GROUP_KEY).build();

		Trigger trigger = null;
		trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(QUERY_REMAIN_CREDIT_TRIGGER_KEY, QUERY_REMAIN_CREDIT_TRIGGER_GROUP_KEY)
				.withSchedule(
						CronScheduleBuilder.cronSchedule(JobsScheduleProperties
								.getString("JobsScheduler.QUERY_REMAIN_CREDIT_SCHEDULE"))).startNow().build(); //$NON-NLS-1$
		getScheduler().scheduleJob(job, trigger);

	}

	public static void scheduleSendSMSJob() throws SchedulerException {
		LOGGER.info("Scheduling SendSMSJob at " + JobsScheduleProperties
				.getString("JobsScheduler.SEND_SMS_SCHEDULE"));
		
		JobDetail job = JobBuilder.newJob(SendMessageJob.class).withIdentity(SMS_SEND_JOB_KEY, SMS_SEND_JOB_GROUP_KEY)
				.build();

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(SMS_SEND_TRIGGER_KEY, SMS_SEND_TRIGGER_GROUP_KEY)
				.withSchedule(
						CronScheduleBuilder.cronSchedule(JobsScheduleProperties
								.getString("JobsScheduler.SEND_SMS_SCHEDULE"))).startNow().build(); //$NON-NLS-1$

		getScheduler().scheduleJob(job, trigger);
	}

	public static void scheduleSystemPrevaylerModelSnapshotJob() throws SchedulerException {

		LOGGER.info("Scheduling SystemPrevaylerModelSnapshotJob at " + JobsScheduleProperties
				.getString("JobsScheduler.SNAPSHOT_SCHEDULE"));

		JobDetail job = JobBuilder.newJob(SystemPrevaylerModelSnapshotJob.class)
				.withIdentity(SYSTEM_PREVAYLER_MODEL_SNAPSHOT_JOB_KEY, SYSTEM_PREVAYLER_MODEL_SNAPSHOT_JOB_GROUP_KEY)
				.build();

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(SYSTEM_PREVAYLER_MODEL_SNAPSHOT_TRIGGER_KEY,
						SYSTEM_PREVAYLER_MODEL_SNAPSHOT_TRIGGER_GROUP_KEY)
				.withSchedule(
						CronScheduleBuilder.cronSchedule(JobsScheduleProperties
								.getString("JobsScheduler.SNAPSHOT_SCHEDULE"))).startNow().build(); //$NON-NLS-1$

		getScheduler().scheduleJob(job, trigger);
	}

}
