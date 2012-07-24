package br.com.campanhasms.sms.notification;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.IGatewayStatusNotification;

import br.com.campanhasms.persistence.SystemPrevayler;
import br.com.campanhasms.sms.service.impl.SMSServiceWrapper;

public class GatewayStatusNotification implements IGatewayStatusNotification {

	private static final Logger LOGGER = Logger.getLogger(GatewayStatusNotification.class);

	@Override
	public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {
		LOGGER.info("Processing a Gateway Status Notification : OldStatus = " + oldStatus + " => NewStatus = "
				+ newStatus);
		if(GatewayStatuses.STOPPED.equals(newStatus)) {
			LOGGER.info("Force SMS Service to re-initialize...");
			try {
				SMSServiceWrapper.initialize(SystemPrevayler.getSystemPrevaylerModel().getCOMPort());
			} catch (Exception e) {
				LOGGER.error("Error when re-initializing SMS Service...", e);
			}
		}
	}

}
