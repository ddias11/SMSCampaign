package br.com.campanhasms.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.smslib.Message.MessageTypes;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.properties.MailProperties;
import br.com.campanhasms.sms.model.ReceivedMessage;

public class RatingMailMessage {

	public void send() throws Exception {
		SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
		String messageContent;

		messageContent = "<TABLE border=\"1\" CELLPADDING=2 >";
		messageContent += "<THEAD>";
		messageContent += "<TR> <TH>Contact Number</TH> <TH>Contact Text Message</TH> </TR>";
		messageContent += "</THEAD>";
		messageContent += "<TBODY>";

		for (ReceivedMessage message : systemPrevaylerModel.getReceivedMessages()) {
			if (MessageTypes.INBOUND.equals(message.getMessageType())) {
				messageContent += "<TR>";
				messageContent += "<TD>" + message.getMessageOriginator().getFormattedContact() + "</TD>";
				messageContent += "<TD>" + message.getMessateText() + "</TD>";
				messageContent += "</TR>";
			}
		}

		messageContent += "</TBODY> ";
		messageContent += "</TABLE>";

		messageContent += "<TABLE border=\"1\" CELLPADDING=2 >";
		messageContent += "<THEAD>";
		messageContent += "<TR> <TH>Messages Sent</TH> <TH>Messages Received</TH> <TH>Messages Confirmed</TH> </TR>";
		messageContent += "</THEAD>";
		messageContent += "<TBODY>";

		messageContent += "<TR>";
		messageContent += "<TD>" + systemPrevaylerModel.getSendedMessagesCouter() + "</TD>";
		messageContent += "<TD>" + systemPrevaylerModel.getReceivedMessagesCounter() + "</TD>";
		messageContent += "<TD>" + systemPrevaylerModel.getMessagesConfirmedCounter() + "</TD>";
		messageContent += "</TR>";

		messageContent += "</TBODY>";
		messageContent += "</TABLE>";

		MailServiceWrapper mailServiceWrapper = new MailServiceWrapper();

		List<String> to = new ArrayList<String>();

		to.add(MailProperties.getString("MailServiceWrapper.FROM_MAIL_ADDRESS"));

		String subject = "SMS Campaign: Received Messages until " + String.format("%tc", Calendar.getInstance());

		for (String mailAddress : systemPrevaylerModel.getListNotificationReceivers()) {
			to.add(mailAddress);
		}
		mailServiceWrapper.sendGmailMessage(messageContent, to, subject);
	}

}
