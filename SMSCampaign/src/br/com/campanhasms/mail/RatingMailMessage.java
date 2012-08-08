package br.com.campanhasms.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.smslib.Message.MessageTypes;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.properties.MailProperties;
import br.com.campanhasms.sms.model.ReceivedMessage;

public class RatingMailMessage {

	private static final Logger LOGGER = Logger.getLogger(RatingMailMessage.class);

	public void send() throws Exception {
		SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
		StringBuffer messageContent = new StringBuffer();

		messageContent.append("<TABLE border=\"1\" CELLPADDING=2 >");
		messageContent.append("<THEAD>");
		messageContent.append("<TR> <TH>Contact Number</TH> <TH>Contact Text Message</TH> </TR>");
		messageContent.append("</THEAD>");
		messageContent.append("<TBODY>");

		for (ReceivedMessage message : systemPrevaylerModel.getReceivedMessages()) {
			try {
				if (MessageTypes.INBOUND.equals(message.getMessageType())) {
					messageContent.append("<TR>");
					messageContent.append("<TD>" + message.getMessageOriginator().getFormattedContact() + "</TD>");
					messageContent.append("<TD>" + message.getMessateText() + "</TD>");
					messageContent.append("</TR>");
				}
			} catch (Exception e) {
				LOGGER.error("Error when addind content of message into the Mail Report", e);
			}
		}

		messageContent.append("</TBODY> ");
		messageContent.append("</TABLE>");

		messageContent.append("<TABLE border=\"1\" CELLPADDING=2 >");
		messageContent.append("<THEAD>");
		messageContent.append("<TR> <TH>Messages Sent</TH> <TH>Messages Received</TH> <TH>Messages Confirmed</TH> </TR>");
		messageContent.append("</THEAD>");
		messageContent.append("<TBODY>");

		messageContent.append("<TR>");
		messageContent.append("<TD>" + systemPrevaylerModel.getSendedMessagesCouter() + "</TD>");
		messageContent.append("<TD>" + systemPrevaylerModel.getReceivedMessagesCounter() + "</TD>");
		messageContent.append("<TD>" + systemPrevaylerModel.getMessagesConfirmedCounter() + "</TD>");
		messageContent.append("</TR>");

		messageContent.append("</TBODY>");
		messageContent.append("</TABLE>");

		MailServiceWrapper mailServiceWrapper = new MailServiceWrapper();

		List<String> to = new ArrayList<String>();

		to.add(MailProperties.getString("MailServiceWrapper.FROM_MAIL_ADDRESS"));

		String subject = "SMS Campaign: Received Messages until " + String.format("%tc", Calendar.getInstance());

		for (String mailAddress : systemPrevaylerModel.getListNotificationReceivers()) {
			to.add(mailAddress);
		}
		mailServiceWrapper.sendGmailMessage(messageContent.toString(), to, subject);
	}

}
