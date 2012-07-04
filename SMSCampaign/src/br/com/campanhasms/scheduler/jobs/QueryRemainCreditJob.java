package br.com.campanhasms.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.properties.SMSServiceProperties;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class QueryRemainCreditJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
			String textMessage = SMSServiceProperties.getString("QueryRemainCredit.TEXT_MESSAGE"); //$NON-NLS-1$
			String contact = SMSServiceProperties.getString("QueryRemainCredit.CONTACT_MESSAGE"); //$NON-NLS-1$
			SMSServiceWrapper.initialize(systemPrevaylerModel.getCOMPort());
			SMSServiceWrapper.sendMessage(contact, textMessage);
		} catch (Exception e) {
			// ERROR ON Send Message Method
			e.printStackTrace();
		}

	}

}
