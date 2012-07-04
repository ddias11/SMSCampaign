package br.com.campanhasms.sms.notification;

import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;

import br.com.campanhasms.persistence.SystemPrevayler;

public class OutboundMessageNotification implements IOutboundMessageNotification {

	@Override
	public void process(AGateway gateway, OutboundMessage msg) {
		SystemPrevayler.getSystemPrevaylerModel().incrementMessagesSendedCounter();
	}

}
