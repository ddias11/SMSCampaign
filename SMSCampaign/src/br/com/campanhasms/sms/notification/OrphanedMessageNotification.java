package br.com.campanhasms.sms.notification;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.InboundMessage;

public class OrphanedMessageNotification implements IOrphanedMessageNotification {

	private static final Logger LOGGER = Logger.getLogger(OrphanedMessageNotification.class);

	@Override
	public boolean process(AGateway gateway, InboundMessage msg) {
		LOGGER.info("Processing a Orphaned Message Notification");
		return false;
	}

}
