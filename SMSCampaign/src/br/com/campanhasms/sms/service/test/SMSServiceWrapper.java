package br.com.campanhasms.sms.service.test;

import java.util.ArrayList;

import org.smslib.InboundMessage;

public class SMSServiceWrapper {


	private static String convertToASCII2(String text) {
		return text.replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e").replaceAll("[îìíï]", "i").replaceAll("[õôòóö]", "o").replaceAll("[ûúùü]", "u")
				.replaceAll("[ÃÂÀÁÄ]", "A").replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I").replaceAll("[ÕÔÒÓÖ]", "O").replaceAll("[ÛÙÚÜ]", "U").replace('ç', 'c')
				.replace('Ç', 'C').replace('ñ', 'n').replace('Ñ', 'N');
	}

	public static ArrayList<InboundMessage> getMessagesReceived() throws Exception {

		ArrayList<InboundMessage> msgList = new ArrayList<InboundMessage>();

		return msgList;
	}


	public static void initialize(String COMPortName) throws Exception {
	}


	public static void removeStoredMessages() throws Exception {
	}


	public static void sendMessage(String contactNumber, String message) throws Exception {
		System.out.println("Sendind Message Contact	: " + contactNumber);
		System.out.println("Sendind Message Text	: " + convertToASCII2(message));
	}

}
