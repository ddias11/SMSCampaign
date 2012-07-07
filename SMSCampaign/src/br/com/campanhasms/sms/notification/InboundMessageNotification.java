package br.com.campanhasms.sms.notification;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.smslib.AGateway;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;
import org.smslib.StatusReportMessage;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.properties.PrevaylerProperties;

public class InboundMessageNotification implements IInboundMessageNotification {

	private void persistValidContact(String originator) {
		try {
			File file = new File(PrevaylerProperties.getString("SystemPrevayler.PREVAYLER_VALID_CONTACTS_LIST"));
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
			bos.write((originator + ";").getBytes());
			bos.flush();
			bos.close();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
		switch (msgType) {
		case INBOUND:
			SystemPrevayler.getSystemPrevaylerModel().incrementMessagesReceivedCounter();
			break;
		case STATUSREPORT:
			SystemPrevayler.getSystemPrevaylerModel().incrementMessagesConfirmedCounter();
			if(msg instanceof StatusReportMessage) {
				StatusReportMessage statusReportMessage = (StatusReportMessage)msg;
				persistValidContact(statusReportMessage.getRecipient());
			}
			break;
		}
	}

}
