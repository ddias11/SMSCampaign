package br.com.campanhasms.sms.notification;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.IGatewayStatusNotification;

public class GatewayStatusNotification implements IGatewayStatusNotification {

	private static final Logger LOGGER = Logger.getLogger(GatewayStatusNotification.class);

	@Override
	public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {
		LOGGER.info("Processing a Gateway Status Notification : OldStatus = " + oldStatus + " => NewStatus = "
				+ newStatus);
	}

}
