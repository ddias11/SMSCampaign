package br.com.campanhasms.sms.reports;

import org.apache.log4j.Logger;
import org.smslib.Service;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.sms.contacts.AdminContactsListBuilder;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class SmsRatingReport extends AbstractReportsRequirements {

	private static final Logger LOGGER = Logger.getLogger(SmsRatingReport.class);

	public void execute() {
		LOGGER.info("Trying to send Sms Rating Report");
		try {
			String message = "";
			message += "\nMsgs Sended: " + Service.getInstance().getOutboundMessageCount();
			message += "\nMsgs Received: " + SystemPrevayler.getSystemPrevaylerModel().getReceivedMessagesCounter();
			message += "\nMsgs Confirmed: " + SystemPrevayler.getSystemPrevaylerModel().getMessagesConfirmedCounter();
			message += "\nLast Contact: " + SystemPrevayler.getSystemPrevaylerModel().getCurrentContact();
			for (Contato contato : AdminContactsListBuilder.getAdminContacts()) {
				SMSServiceWrapper.sendMessage(contato.getFormattedContact(), message);
			}
		} catch (Exception e) {
			LOGGER.error("Error when trying to send Sms Rating Report", e);
		}
		
	}

}
