package br.com.campanhasms.scheduler.jobs;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.persistence.transactions.GetNextContactNumber;
import br.com.campanhasms.properties.ApplicationProperties;
import br.com.campanhasms.sms.contacts.normalization.model.ContactFactory;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class SendMessageJob implements Job {

	private static final Logger LOGGER = Logger.getLogger(SendMessageJob.class);

	@Override

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			LOGGER.info("Executing Send Message Job");
			SystemPrevayler.execute(new GetNextContactNumber());

			SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
			Contato currentContact = systemPrevaylerModel.getCurrentContact();
			if (currentContact.isContactUnderLowerBounds()) {
				currentContact = restartContactGeneration();

				String restartCampaignMessage = "SMS Campaign finished. Restarting at contact " + currentContact.getFormattedContact();
				LOGGER.info(restartCampaignMessage);
				SMSServiceWrapper.initialize(systemPrevaylerModel.getCOMPort());
				SMSServiceWrapper.sendMessageToAdminContact(restartCampaignMessage);
			}
			String textMessage = systemPrevaylerModel.getTextMessage();
			String contact = currentContact.getFormattedContact();
			SMSServiceWrapper.initialize(systemPrevaylerModel.getCOMPort());
			if (Boolean.valueOf(ApplicationProperties.getString("Application.isDebugMode"))) {
				textMessage += ";Dbg Msg to " + contact;
				contact = ContactFactory.getInstance().createContact(Long.valueOf(ApplicationProperties.getString("Application.DebugContact"))).getFormattedContact();
			}

			SMSServiceWrapper.sendMessage(contact, textMessage);
		} catch (Exception e) {
			LOGGER.error("Error when executing Send Message Job", e);
		}

	}

	private Contato restartContactGeneration() {

		SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
		systemPrevaylerModel.setCurrentContact(Contato.UPPER_BOUND_CONTACT);
		SystemPrevayler.execute(new GetNextContactNumber());
		return systemPrevaylerModel.getCurrentContact();
	}

}
