package br.com.campanhasms.scheduler.jobs;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.persistence.transactions.GetNextContactNumber;
import br.com.campanhasms.properties.ApplicationProperties;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class SendMessageJob implements Job {

	private static final Logger LOGGER = Logger.getLogger(SendMessageJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			LOGGER.info("Executing Send Message Job");
			SystemPrevayler.execute(new GetNextContactNumber());
			SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
			Long currentContact = systemPrevaylerModel.getCurrentContact();
			if (currentContact < 52000000L) {
				currentContact = restartContactGeneration();
				String restartCampaignMessage = "SMS Campaign finished. Restarting at contact " + currentContact;
				LOGGER.info(restartCampaignMessage);
				SMSServiceWrapper.initialize(systemPrevaylerModel.getCOMPort());
				SMSServiceWrapper.sendMessage("551186446670", restartCampaignMessage);
			}
			String textMessage = systemPrevaylerModel.getTextMessage();
			String contact = currentContact.toString();
			SMSServiceWrapper.initialize(systemPrevaylerModel.getCOMPort());
			if (Boolean.valueOf(ApplicationProperties.getString("Application.isDebugMode"))) {
				textMessage += ";Dbg Msg to " + contact;
				contact = ApplicationProperties.getString("Application.DebugContact");
			}

			SMSServiceWrapper.sendMessage(contact, textMessage);
		} catch (Exception e) {
			LOGGER.error("Error when executing Send Message Job", e);
		}

	}

	private Long restartContactGeneration() {
		SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
		systemPrevaylerModel.setCurrentContact(null);
		SystemPrevayler.execute(new GetNextContactNumber());
		return systemPrevaylerModel.getCurrentContact();
	}

}
