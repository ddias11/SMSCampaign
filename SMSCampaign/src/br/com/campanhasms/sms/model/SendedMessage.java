package br.com.campanhasms.sms.model;

import java.io.Serializable;

import org.smslib.OutboundMessage;

public class SendedMessage implements Serializable, Comparable<SendedMessage> {

	private static final long serialVersionUID = 5473509778021152155L;
	private Long messageTimeInMilis;

	public SendedMessage(Long messageTimeInMilis) {
		super();
		this.messageTimeInMilis = messageTimeInMilis;
	}

	public SendedMessage(OutboundMessage message) {
		this.messageTimeInMilis = message.getDate().getTime() / 100000;
	}

	@Override
	public int compareTo(SendedMessage receivedMessage) {
		return receivedMessage.getMessaFormatToComparison().compareTo(getMessaFormatToComparison());
	}

	public String getMessaFormatToComparison() {
		String time = String.format("%015d", this.messageTimeInMilis);
		return time;
	}

	public Long getMessageTimeInMilis() {
		return messageTimeInMilis;
	}

}
