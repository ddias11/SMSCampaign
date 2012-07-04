package br.com.campanhasms.sms.notification;

import org.smslib.AGateway;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.InboundMessage;

public class OrphanedMessageNotification implements IOrphanedMessageNotification {

	@Override
	public boolean process(AGateway gateway, InboundMessage msg) {
		return false;
	}

}
