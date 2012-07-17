package br.com.campanhasms.scheduler.jobs;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.campanhasms.persistence.SystemPrevayler;

public class SystemPrevaylerModelSnapshotJob implements Job {

	
	private static final Logger LOGGER = Logger.getLogger(SystemPrevaylerModelSnapshotJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			LOGGER.info("Executing System Prevayler Model Snapshot Job");		
			SystemPrevayler.takeSnapShot();
		} catch (Exception e) {
			LOGGER.info("Error when executing System Prevayler Model Snapshot Job", e);
		}

	}

}
