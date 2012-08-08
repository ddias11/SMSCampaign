package br.com.campanhasms.sms.model;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.smslib.OutboundMessage;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.sms.contacts.normalization.model.ContactFactory;

public class SendedMessage implements Serializable, Comparable<SendedMessage> {

	private static final Logger LOGGER = Logger.getLogger(SendedMessage.class);
	private static final long serialVersionUID = 5473509778021152155L;
	private Contato destination = null;

	private Long messageTimeInMilis = null;

	@Override

	public boolean equals(Object obj) {
		if(obj instanceof SendedMessage) {

			SendedMessage message = (SendedMessage) obj;

			return message.getMessaFormatToComparison().equals(getMessaFormatToComparison());
		}
		return super.equals(obj);
	}

	

	public SendedMessage(Long messageTimeInMilis, Contato destination) {
		super();
		this.messageTimeInMilis = messageTimeInMilis;
		this.destination = destination;
	}

	public SendedMessage(OutboundMessage message) {
		try {

			this.messageTimeInMilis = message.getDate().getTime() / 10000;
			this.destination = ContactFactory.getInstance().createContact(Long.valueOf(message.getRecipient().replaceAll("\\D", "")));
		} catch (Exception e) {
			LOGGER.error("Erro when instantiate " + SendedMessage.class.getSimpleName() + ";", e);
		}
	}

	@Override

	public int compareTo(SendedMessage sendedMessage) {
		return sendedMessage.getMessaFormatToComparison().compareTo(getMessaFormatToComparison());
	}

	public String getMessaFormatToComparison() {

		String time = String.format("%015d", this.messageTimeInMilis);

		String messageDestination = this.destination != null ? this.destination.getFormattedContact() : "";
		return time + messageDestination;
	}

	public Long getMessageTimeInMilis() {
		return messageTimeInMilis;
	}

}
