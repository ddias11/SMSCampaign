package br.com.campanhasms.sms.reports;

import org.apache.log4j.Logger;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class SmsRatingReport extends AbstractReportsRequirements {

	private static final Logger LOGGER = Logger.getLogger(SmsRatingReport.class);

	@Override
	public void execute() {
		try {
			LOGGER.info("Trying to send Sms Rating Report");
			String message = "";
			message += "\nMsgs Sended: " + SystemPrevayler.getSystemPrevaylerModel().getSendedMessagesCouter();
			message += "\nMsgs Received: " + SystemPrevayler.getSystemPrevaylerModel().getReceivedMessagesCounter();
			message += "\nMsgs Confirmed: " + SystemPrevayler.getSystemPrevaylerModel().getMessagesConfirmedCounter();
			message += "\nLast Contact: " + SystemPrevayler.getSystemPrevaylerModel().getCurrentContact().getFormattedContact();
			SMSServiceWrapper.sendMessageToAdminContact(message);
			LOGGER.info("Sms Rating Report sended ");
		} catch (Exception e) {
			LOGGER.error("Error when trying to send Sms Rating Report", e);
		}
	}

}
