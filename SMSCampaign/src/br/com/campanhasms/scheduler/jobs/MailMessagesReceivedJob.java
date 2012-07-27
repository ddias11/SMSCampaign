package br.com.campanhasms.scheduler.jobs;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.campanhasms.mail.RatingMailMessage;

public class MailMessagesReceivedJob implements Job {

	private static final Logger LOGGER = Logger.getLogger(MailMessagesReceivedJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			LOGGER.info("Executing the Mail Messages Received Job");
			new RatingMailMessage().send();
		} catch (Exception e) {
			LOGGER.error("Error when executing the Mail Messages Received Job", e);
			throw new JobExecutionException(e);
		}
	}

}
