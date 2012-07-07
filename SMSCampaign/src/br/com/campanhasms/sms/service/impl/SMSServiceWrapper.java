package br.com.campanhasms.sms.service.impl;

import java.util.ArrayList;

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

	private static String convertToASCII2(String text) {
		return text.replaceAll("[„‚‡·‰]", "a").replaceAll("[ÍËÈÎ]", "e").replaceAll("[ÓÏÌÔ]", "i")
				.replaceAll("[ıÙÚÛˆ]", "o").replaceAll("[˚˙˘¸]", "u").replaceAll("[√¬¿¡ƒ]", "A")
				.replaceAll("[ »…À]", "E").replaceAll("[ŒÃÕœ]", "I").replaceAll("[’‘“”÷]", "O")
				.replaceAll("[€Ÿ⁄‹]", "U").replace('Á', 'c').replace('«', 'C').replace('Ò', 'n').replace('—', 'N');
	}

	public static ArrayList<InboundMessage> getMessagesReceived() throws Exception {
		ArrayList<InboundMessage> msgList = new ArrayList<InboundMessage>();
		Service.getInstance().readMessages(msgList, MessageClasses.ALL);
		return msgList;
	}

	public static void initialize(String COMPortName) throws Exception {
		if (isServiceStopped()) {
			SerialModemGateway gateway = new SerialModemGateway("Modem - " + COMPortName, COMPortName, 115200,
					"Huawei", "");
			gateway.setInbound(true);
			gateway.setOutbound(true);
			gateway.setSimPin("0000");
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
		ArrayList<InboundMessage> msgList = new ArrayList<InboundMessage>();
		Service.getInstance().readMessages(msgList, MessageClasses.ALL);
		for (InboundMessage inboundMessage : msgList) {
			Service.getInstance().deleteMessage(inboundMessage);
		}
	}

	public static void sendMessage(String contactNumber, String message) throws Exception {
		System.out.println("Sendind Message Contact	: " + contactNumber);
		OutboundMessage outboundMessage = new OutboundMessage(contactNumber, convertToASCII2(message));
		outboundMessage.setStatusReport(true);
		Service.getInstance().queueMessage(outboundMessage);
	}

}
