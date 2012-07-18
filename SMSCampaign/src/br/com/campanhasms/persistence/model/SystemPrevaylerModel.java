package br.com.campanhasms.persistence.model;

import java.io.Serializable;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;

import br.com.campanhasms.properties.Messages;
import br.com.campanhasms.sms.model.ReceivedMessage;
import br.com.campanhasms.sms.model.SendedMessage;
import br.com.campanhasms.view.forms.exception.FormDataException;
import br.com.campanhasms.view.forms.model.IFormDataWrapper;

public class SystemPrevaylerModel implements Serializable, IFormDataWrapper {

	private static final Logger LOGGER = Logger.getLogger(SystemPrevaylerModel.class);

	private static final long serialVersionUID = -651641218710234008L;

	private String commPortIdentifier;

	private Long currentContact = null;

	private TreeSet<SendedMessage> lastSendedMessages;

	private List<String> listNotificationReceivers;

	private Integer messagesConfirmedCounter;

	private TreeSet<ReceivedMessage> receivedMessages = null;

	private Integer sendedMessagesCouter = new Integer(0);

	private List<String> smsPriorityContactsList;

	private String textMessage;

	public SystemPrevaylerModel() {

	}

	public boolean addReceivedMessage(InboundMessage inboundMessage) {
		return getReceivedMessages().add(new ReceivedMessage(inboundMessage));
	}

	public boolean addSendedMessage(OutboundMessage outboundMessage) {
		boolean isAdded = getLastSendedMessages().add(new SendedMessage(outboundMessage));
		if (isAdded) {
			this.sendedMessagesCouter++;
		}
		return isAdded;
	}

	@Override
	public String getCOMPort() {
		return this.commPortIdentifier;
	}

	public Long getCurrentContact() {
		return currentContact;
	}

	public TreeSet<SendedMessage> getLastSendedMessages() {
		if (lastSendedMessages == null) {
			lastSendedMessages = new TreeSet<SendedMessage>();
		}
		if (lastSendedMessages.size() > 10)
			lastSendedMessages.remove(lastSendedMessages.first());
		return lastSendedMessages;
	}

	@Override
	public List<String> getListNotificationReceivers() {
		return this.listNotificationReceivers;
	}

	private Integer getMessageCounter(MessageTypes messageType) {
		Integer counter = 0;
		for (ReceivedMessage receivedMessage : getReceivedMessages()) {
			if (receivedMessage.getMessageType().equals(messageType)) {
				counter++;
			}
		}
		return counter;
	}

	public Integer getMessagesConfirmedCounter() {
		return getMessageCounter(MessageTypes.STATUSREPORT);
	}

	public TreeSet<ReceivedMessage> getReceivedMessages() {
		if (receivedMessages == null) {
			receivedMessages = new TreeSet<ReceivedMessage>();

		}
		return receivedMessages;
	}

	public Integer getReceivedMessagesCounter() {
		return getMessageCounter(MessageTypes.INBOUND);
	}

	public Integer getSendedMessagesCouter() {
		return sendedMessagesCouter;
	}

	@Override
	public List<String> getSMSPriorityContactsList() {
		return this.smsPriorityContactsList;
	}

	@Override
	public String getTextMessage() {
		return this.textMessage;
	}

	public void incrementMessagesConfirmedCounter() {
		if (messagesConfirmedCounter == null) {
			messagesConfirmedCounter = new Integer(0);
		}
		this.messagesConfirmedCounter++;
	}

	@Override
	public void setCOMPort(String commPortIdentifier) throws FormDataException {
		LOGGER.info("Setting CommPortIdentifier with value: " + commPortIdentifier);
		if ("".equals(commPortIdentifier) || commPortIdentifier == null) { //$NON-NLS-1$
			throw new FormDataException(Messages.getString("MESSAGE.INVALID_COM_PORT_MSG")); //$NON-NLS-1$
		}
		this.commPortIdentifier = commPortIdentifier;
	}

	public void setCurrentContact(Long currentContact) {
		LOGGER.info("Setting Current Contact with value: " + currentContact);
		this.currentContact = currentContact;
	}

	@Override
	public void setListNotificationReceivers(List<String> listNotificatinReceivers) throws FormDataException {
		LOGGER.info("Setting ListNotificationReceivers with value: " + listNotificatinReceivers.toString());
		if (listNotificatinReceivers == null || listNotificatinReceivers.size() == 0) {
			throw new FormDataException(Messages.getString("MESSAGE.INVALID_LIST_NOTIFICATION_RECEIVERS_MSG")); //$NON-NLS-1$
		}
		this.listNotificationReceivers = listNotificatinReceivers;
	}

	@Override
	public void setSMSPriorityContactsList(List<String> smsPriorityContactsList) {
		LOGGER.info("Setting SMSPriorityContactsList with value: " + smsPriorityContactsList.toString());
		this.smsPriorityContactsList = smsPriorityContactsList;
	}

	@Override
	public void setTextMessage(String textMessage) throws FormDataException {
		LOGGER.info("Setting TextMessage with value: " + textMessage);
		if ("".equals(textMessage) || textMessage == null) { //$NON-NLS-1$
			throw new FormDataException(Messages.getString("MESSAGE.INVALID_TEXT_MESSAGE")); //$NON-NLS-1$
		}
		this.textMessage = textMessage;
	}

}
