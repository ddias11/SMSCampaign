package br.com.campanhasms.sms.service.test;

import java.util.ArrayList;

import org.smslib.InboundMessage;

public class SMSServiceWrapper {


	private static String convertToASCII2(String text) {
		return text.replaceAll("[�����]", "a").replaceAll("[����]", "e").replaceAll("[����]", "i").replaceAll("[�����]", "o").replaceAll("[����]", "u")
				.replaceAll("[�����]", "A").replaceAll("[����]", "E").replaceAll("[����]", "I").replaceAll("[�����]", "O").replaceAll("[����]", "U").replace('�', 'c')
				.replace('�', 'C').replace('�', 'n').replace('�', 'N');
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
