package br.com.campanhasms.sms.notification;

import java.io.IOException;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;
import org.smslib.Service;
import org.smslib.StatusReportMessage;
import org.smslib.TimeoutException;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.sms.contacts.AdminContactsListBuilder;
import br.com.campanhasms.sms.contacts.normalization.model.ContactFactory;
import br.com.campanhasms.sms.reports.AbstractReportsRequirements.ReportRequiredType;
import br.com.campanhasms.sms.reports.EmailRatingReport;
import br.com.campanhasms.sms.reports.NotSupportedReport;
import br.com.campanhasms.sms.reports.SmsRatingReport;

public class InboundMessageNotification implements IInboundMessageNotification {

	private static final Logger LOGGER = Logger.getLogger(InboundMessageNotification.class);

	private void deleteMessage(InboundMessage msg) {
		try {
			Service.getInstance().deleteMessage(msg);
		} catch (TimeoutException | GatewayException | IOException | InterruptedException e) {
			LOGGER.error("Error when trying to delete the function message: " + msg.getText().toUpperCase(), e);
		}
	}

	private boolean isFromAdminContact(Contato originator) {

		for (Contato contato : AdminContactsListBuilder.getAdminContacts()) {
			if (originator.getFormattedContact().equals(contato.getFormattedContact())) {
				return true;
			}
		}
		return false;
	}

	private boolean isFunctionMessage(InboundMessage msg) {
		Contato originator = ContactFactory.getInstance().createContact(Long.valueOf(msg.getOriginator().replaceAll("\\D", "0")));
		return isRecentlyMessage(msg) && isFromAdminContact(originator);
	}

	private boolean isRecentlyMessage(InboundMessage msg) {
		Long dateDiff = Calendar.getInstance().getTimeInMillis() - msg.getDate().getTime();
		return dateDiff < (1L * (1000L * 60L * 60L));
	}

	@Override
	public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {

		if (SystemPrevayler.getSystemPrevaylerModel().addReceivedMessage(msg)) {
			switch (msgType) {
			case STATUSREPORT:
				if (msg instanceof StatusReportMessage) {
					StatusReportMessage statusReportMessage = (StatusReportMessage) msg;
					String recipient = ContactFactory.getInstance().createContact(Long.valueOf(statusReportMessage.getRecipient().replaceAll("\\D", "0"))).getFormattedContact();
					LOGGER.info("Processing a " + MessageTypes.STATUSREPORT.name() + " from Recipient: " + recipient);
					Logger.getLogger("validContacts").info("recipient: " + recipient);
				}
				break;
			default:
				String originator = ContactFactory.getInstance().createContact(Long.valueOf(msg.getOriginator().replaceAll("\\D", "0"))).getFormattedContact();
				LOGGER.info("Processing a " + MessageTypes.INBOUND.name() + " Message Notification from Originator: " + originator + "; Text: " + msg.getText());
				if (isFunctionMessage(msg)) {
					Integer requiredFunction = -1;
					try {
						requiredFunction = Integer.valueOf(msg.getText().trim());
					} catch (Exception e) {
						LOGGER.error("Error when parsing ReportRequiredType for value: " + msg.getText().trim());
					}

					switch (ReportRequiredType.parse(requiredFunction)) {
					case SMS_RATING:
						new Thread(new SmsRatingReport()).start();
						break;

					case EMAIL_RATING:
						new Thread(new EmailRatingReport()).start();
						break;

					case CLEAR_RECEIVED_MESSAGES:
						LOGGER.error("Cleanning the received messages...");
						SystemPrevayler.getSystemPrevaylerModel().clearReceivedMessages();
						break;

					case NOT_DEFINED:
						new Thread(new NotSupportedReport()).start();
						break;
					}
					SystemPrevayler.getSystemPrevaylerModel().getReceivedMessages().remove(msg);
				}
				break;
			}
			SystemPrevayler.takeSnapShot();
		}
		deleteMessage(msg);
	}
}
