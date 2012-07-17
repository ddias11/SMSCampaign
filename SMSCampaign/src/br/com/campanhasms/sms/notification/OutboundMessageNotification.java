package br.com.campanhasms.sms.notification;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;

public class OutboundMessageNotification implements IOutboundMessageNotification {

	
	private static final Logger LOGGER = Logger.getLogger(OutboundMessageNotification.class);
	
	@Override
	public void process(AGateway gateway, OutboundMessage msg) {
		LOGGER.info("Processing a Outbound Message Notification; Recipient: " + msg.getRecipient() + "; Text: " + msg.getText());
	}

}
