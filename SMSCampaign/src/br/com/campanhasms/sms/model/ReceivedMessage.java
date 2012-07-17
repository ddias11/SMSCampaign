package br.com.campanhasms.sms.model;

import java.io.Serializable;

import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;
import org.smslib.StatusReportMessage;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.properties.SMSServiceProperties;

public class ReceivedMessage implements Serializable, Comparable<ReceivedMessage> {

	private static final long serialVersionUID = -5078789497534008164L;
	
	private Long messageTimeInMilis;

	public Long getMessageTimeInMilis() {
		return messageTimeInMilis;
	}


	public String getMessateText() {
		return messateText;
	}


	public Contato getMessageOriginator() {
		return messageOriginator;
	}


	public MessageTypes getMessageType() {
		return messageType;
	}

	private String messateText;

	private Contato messageOriginator;

	private MessageTypes messageType;
	
	
	public ReceivedMessage(InboundMessage message) {
		this.messageType = message.getType();
		if(MessageTypes.STATUSREPORT.equals(message.getType())) {
			try {
				StatusReportMessage statusReportMessage = (StatusReportMessage)message;
				this.messageTimeInMilis = statusReportMessage.getSent().getTime();
				this.messageOriginator = new Contato(statusReportMessage.getRecipient());
			} catch (Exception e) {
			}
		} else {
			try {
				this.messageOriginator = new Contato(message.getOriginator());
			} catch (Exception e) {
			}
			this.messageTimeInMilis = message.getDate().getTime() / 100000;
		}
		this.messateText = message.getText();
	}


	public ReceivedMessage(Long messageTimeInMilis, String messateText, Contato messageOriginator, MessageTypes messageType) {
		super();
		this.messageTimeInMilis = messageTimeInMilis;
		this.messateText = messateText;
		this.messageOriginator = messageOriginator;
		this.messageType = messageType;
	}


	@Override
	public int compareTo(ReceivedMessage receivedMessage) {
		return receivedMessage.getMessaFormatToComparison().compareTo(getMessaFormatToComparison());
	}
	
	public String getMessaFormatToComparison() {
		
		String time = String.format("%015d", this.messageTimeInMilis);
		String messageOriginator = this.messageOriginator.getFormattedContact();
		String messateText = String.format("%"+SMSServiceProperties.getString("MensagemSMS.SMS_LENGTH")+"s", this.messateText);
		String messageType = String.format("%7s", this.messageType.name());
		
		return time.concat(messageOriginator).concat(messateText).concat(messageType);
		
	}

}
