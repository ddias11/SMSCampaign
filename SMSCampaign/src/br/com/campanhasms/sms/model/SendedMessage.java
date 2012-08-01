package br.com.campanhasms.sms.model;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.smslib.OutboundMessage;

import br.com.campanhasms.model.Contato;

public class SendedMessage implements Serializable, Comparable<SendedMessage> {

	private static final Logger LOGGER = Logger.getLogger(SendedMessage.class);
	private static final long serialVersionUID = 5473509778021152155L;
	private Long messageTimeInMilis = null;
	private Contato destination = null;

	public SendedMessage(Long messageTimeInMilis, Contato destination) {
		super();
		this.messageTimeInMilis = messageTimeInMilis;
		this.destination = destination;
	}

	public SendedMessage(OutboundMessage message) {
		try {
			
		this.messageTimeInMilis = message.getDate().getTime() / 10000;
		this.destination = new Contato(new Long(message.getRecipient().replaceAll("\\D", "")));
		} catch (Exception e) {
			LOGGER.error("Erro when instantiate " + SendedMessage.class.getSimpleName() + ";", e);
		}
	}

	@Override
	public int compareTo(SendedMessage receivedMessage) {
		return receivedMessage.getMessaFormatToComparison().compareTo(getMessaFormatToComparison());
	}

	public String getMessaFormatToComparison() {
		String time = String.format("%015d", this.messageTimeInMilis);
		String messageDestination = this.destination != null ? this.destination.getFormattedContact() : "";
		return time+messageDestination;
	}

	public Long getMessageTimeInMilis() {
		return messageTimeInMilis;
	}

}
