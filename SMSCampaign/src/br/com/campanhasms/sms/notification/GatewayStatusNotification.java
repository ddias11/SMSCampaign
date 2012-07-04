package br.com.campanhasms.sms.notification;

import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.IGatewayStatusNotification;

public class GatewayStatusNotification implements IGatewayStatusNotification {

	@Override
	public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {

	}

}
