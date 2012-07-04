package br.com.campanhasms.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.campanhasms.persistence.SystemPrevayler;

public class SystemPrevaylerModelSnapshotJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			SystemPrevayler.takeSnapShot();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
