package br.com.campanhasms.scheduler.jobs;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.properties.SMSServiceProperties;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class QueryRemainCreditJob implements Job {

	private static final Logger LOGGER = Logger.getLogger(QueryRemainCreditJob.class);

	@Override

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			LOGGER.info("Executing the Query Remain Credit Job");

			SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();

			String textMessage = SMSServiceProperties.getString("QueryRemainCredit.TEXT_MESSAGE"); //$NON-NLS-1$

			String contact = SMSServiceProperties.getString("QueryRemainCredit.CONTACT_MESSAGE"); //$NON-NLS-1$
			SMSServiceWrapper.initialize(systemPrevaylerModel.getCOMPort());
			SMSServiceWrapper.sendMessage(contact, textMessage);
		} catch (Exception e) {
			LOGGER.error("Error when executing the Query Remain Credit Job", e);
		}

	}

}
