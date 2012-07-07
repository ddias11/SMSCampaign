package br.com.campanhasms.scheduler.jobs;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;

import br.com.campanhasms.mail.MailServiceWrapper;
import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class MailMessagesReceivedJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			SystemPrevaylerModel systemPrevaylerModel = SystemPrevayler.getSystemPrevaylerModel();
			String messageContent;

			messageContent = "<TABLE border=\"1\" CELLPADDING=2 >";
			messageContent += "<THEAD>";
			messageContent += "<TR> <TH>Contact Number</TH> <TH>Contact Text Message</TH> </TR>";
			messageContent += "</THEAD>";
			messageContent += "<TBODY>";

			for (InboundMessage message : SMSServiceWrapper.getMessagesReceived()) {
				if (MessageTypes.INBOUND.equals(message.getType())) {
					messageContent += "<TR>";
					messageContent += "<TD>" + message.getOriginator() + "</TD>";
					messageContent += "<TD>" + message.getText() + "</TD>";
					messageContent += "</TR>";
				}
			}

			messageContent += "</TBODY>";
			messageContent += "</TABLE>";

			MailServiceWrapper mailServiceWrapper = new MailServiceWrapper();

			List<String> to = new ArrayList<String>();
			String subject = "SMS Campaign: Received Messages until " + String.format("%tc", Calendar.getInstance());

			for (String mailAddress : systemPrevaylerModel.getListNotificationReceivers()) {
				to.add(mailAddress);
			}
			mailServiceWrapper.sendGmailMessage(messageContent, to, subject);
			SMSServiceWrapper.removeStoredMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
