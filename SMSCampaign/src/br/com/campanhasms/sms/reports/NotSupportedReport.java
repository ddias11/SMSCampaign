package br.com.campanhasms.sms.reports;

import org.apache.log4j.Logger;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.sms.contacts.AdminContactsListBuilder;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class NotSupportedReport extends AbstractReportsRequirements {

	private static final Logger LOGGER = Logger.getLogger(NotSupportedReport.class);

	public void execute() {
		try {
			LOGGER.info("Trying to send Not Supported Report");
			String message = "The Functions Supported are: ";

			message += "\nSend SMS with text \"" + ReportRequiredType.SMS_RATING.getValue() + "\" for "
					+ ReportRequiredType.SMS_RATING.name() + "; ";
			message += "\nSend SMS with text \"" + ReportRequiredType.EMAIL_RATING.getValue() + "\" for "
					+ ReportRequiredType.EMAIL_RATING.name() + "; ";
			for (Contato contato : AdminContactsListBuilder.getAdminContacts()) {
				SMSServiceWrapper.sendMessage(contato.getFormattedContact(), message);
			}
			LOGGER.info("Not Supported Report sended ");
		} catch (Exception e1) {
			LOGGER.error("Error when trying to send sms Report", e1);
		}
		
	}

}
