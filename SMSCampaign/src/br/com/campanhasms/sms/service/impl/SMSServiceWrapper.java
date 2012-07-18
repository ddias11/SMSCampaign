package br.com.campanhasms.sms.service.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.Service.ServiceStatus;
import org.smslib.modem.SerialModemGateway;

import br.com.campanhasms.sms.notification.CallNotification;
import br.com.campanhasms.sms.notification.GatewayStatusNotification;
import br.com.campanhasms.sms.notification.InboundMessageNotification;
import br.com.campanhasms.sms.notification.OrphanedMessageNotification;
import br.com.campanhasms.sms.notification.OutboundMessageNotification;

public class SMSServiceWrapper {

	public static final String ADMIN_CONTACTS_GROUP = "ADMIN_CONTACTS";

	private static final Logger LOGGER = Logger.getLogger(SMSServiceWrapper.class);

	private static String convertToASCII2(String text) {
		return text.replaceAll("[„‚‡·‰]", "a").replaceAll("[ÍËÈÎ]", "e").replaceAll("[ÓÏÌÔ]", "i")
				.replaceAll("[ıÙÚÛˆ]", "o").replaceAll("[˚˙˘¸]", "u").replaceAll("[√¬¿¡ƒ]", "A")
				.replaceAll("[ »…À]", "E").replaceAll("[ŒÃÕœ]", "I").replaceAll("[’‘“”÷]", "O")
				.replaceAll("[€Ÿ⁄‹]", "U").replace('Á', 'c').replace('«', 'C').replace('Ò', 'n').replace('—', 'N');
	}

	public static ArrayList<InboundMessage> getMessagesReceived() throws Exception {
		LOGGER.info("Obtaining messages received");
		ArrayList<InboundMessage> msgList = new ArrayList<InboundMessage>();
		Service.getInstance().readMessages(msgList, MessageClasses.ALL);
		return msgList;
	}

	public static void initialize(String COMPortName) throws Exception {
		if (isServiceStopped()) {
			LOGGER.info("Initializing SMS Service...");
			SerialModemGateway gateway = new SerialModemGateway("Modem - " + COMPortName, COMPortName, 115200,
					"Huawei", "");
			gateway.setInbound(true);
			gateway.setOutbound(true);
			gateway.setSimPin("0000");
			// gateway.getATHandler().setStorageLocations("SM");
			Service.getInstance().setInboundMessageNotification(new InboundMessageNotification());
			Service.getInstance().setOutboundMessageNotification(new OutboundMessageNotification());
			Service.getInstance().setGatewayStatusNotification(new GatewayStatusNotification());
			Service.getInstance().setOrphanedMessageNotification(new OrphanedMessageNotification());
			Service.getInstance().setCallNotification(new CallNotification());
			Service.getInstance().addGateway(gateway);
			Service.getInstance().startService();
		}
	}

	private static boolean isServiceStopped() {
		return ServiceStatus.STOPPED.equals(Service.getInstance().getServiceStatus());
	}

	public static void removeStoredMessages() throws Exception {
		LOGGER.info("Removing Stored Messages");
		ArrayList<InboundMessage> msgList = new ArrayList<InboundMessage>();
		Service.getInstance().readMessages(msgList, MessageClasses.ALL);
		for (InboundMessage inboundMessage : msgList) {
			if (Service.getInstance().deleteMessage(inboundMessage)) {
				LOGGER.info("Message [Originator: " + inboundMessage.getOriginator() + "Text: "
						+ inboundMessage.getText() + "] was sucessfuly removed");
			} else {
				LOGGER.info("Message [Originator: " + inboundMessage.getOriginator() + "Text: "
						+ inboundMessage.getText() + "] was not removed");
			}
		}
	}

	public static void sendMessage(String contactNumber, String message) throws Exception {
		OutboundMessage outboundMessage = new OutboundMessage(contactNumber, convertToASCII2(message));
		outboundMessage.setStatusReport(true);
		Service.getInstance().queueMessage(outboundMessage);
	}

}
