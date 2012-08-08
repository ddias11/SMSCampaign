package br.com.campanhasms.sms.notification;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.ICallNotification;

public class CallNotification implements ICallNotification {

	private static final Logger LOGGER = Logger.getLogger(CallNotification.class);

	@Override
	public void process(AGateway gateway, String callerId) {
		LOGGER.info("Processing a Call Notification : " + callerId);
	}

}
