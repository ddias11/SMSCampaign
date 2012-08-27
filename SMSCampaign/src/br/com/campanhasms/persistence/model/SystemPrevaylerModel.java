package br.com.campanhasms.persistence.model;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;

import br.com.campanhasms.model.Contato;
import br.com.campanhasms.properties.Messages;
import br.com.campanhasms.sms.contacts.BlackListContactsListBuilder;
import br.com.campanhasms.sms.model.ReceivedMessage;
import br.com.campanhasms.sms.model.SendedMessage;
import br.com.campanhasms.view.forms.exception.FormDataException;
import br.com.campanhasms.view.forms.model.IFormDataWrapper;

public class SystemPrevaylerModel implements Serializable, IFormDataWrapper {

	private static final Logger LOGGER = Logger.getLogger(SystemPrevaylerModel.class);

	private static final long serialVersionUID = -651641218710234008L;

	private ConcurrentSkipListSet<Contato> blackListContacts;

	private String commPortIdentifier;

	private Contato currentContact = null;

	private ConcurrentSkipListSet<SendedMessage> lastSendedMessages;

	private List<String> listNotificationReceivers;

	private Integer messagesConfirmedCounter;

	private ConcurrentSkipListSet<ReceivedMessage> receivedMessages = null;

	private Integer sendedMessagesCouter = Integer.valueOf(0);

	private List<String> smsPriorityContactsList;

	private String textMessage;

	public SystemPrevaylerModel() {

	}

	public boolean addContactInBlackList(Contato contato) {
		boolean contactWasAdded = getBlackListContacts().add(contato);
		if (contactWasAdded) {
			persistXML();
		}
		return contactWasAdded;
	}

	public boolean addReceivedMessage(InboundMessage inboundMessage) {
		return getReceivedMessages().add(new ReceivedMessage(inboundMessage));
	}
	
	public void clearReceivedMessages() {
		getReceivedMessages().clear();
	}

	public boolean addSendedMessage(OutboundMessage outboundMessage) {
		boolean isAdded = getLastSendedMessages().add(new SendedMessage(outboundMessage));
		if (isAdded) {
			this.sendedMessagesCouter++;
		}
		return isAdded;
	}

	public ConcurrentSkipListSet<Contato> getBlackListContacts() {
		if (blackListContacts == null) {
			blackListContacts = new ConcurrentSkipListSet<Contato>();
			loadBlackListContactsFromXml();

		}
		return blackListContacts;
	}

	@Override
	public String getCOMPort() {
		return this.commPortIdentifier;
	}

	public Contato getCurrentContact() {
		return currentContact;
	}

	public ConcurrentSkipListSet<SendedMessage> getLastSendedMessages() {
		if (lastSendedMessages == null) {
			lastSendedMessages = new ConcurrentSkipListSet<SendedMessage>();
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

	public ConcurrentSkipListSet<ReceivedMessage> getReceivedMessages() {
		if (receivedMessages == null) {
			receivedMessages = new ConcurrentSkipListSet<ReceivedMessage>();

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
			messagesConfirmedCounter = Integer.valueOf(0);
		}
		this.messagesConfirmedCounter++;
	}

	public void loadBlackListContactsFromXml() {
		try {
			blackListContacts = BlackListContactsListBuilder.getPersistedContactsInBlackList();
		} catch (Exception e) {
			blackListContacts = new ConcurrentSkipListSet<Contato>();
			LOGGER.error("Setting empty BlackList");
		}
	}

	public void persistXML() {
		try {
			BlackListContactsListBuilder.persistContactsInBlackList(getBlackListContacts());
		} catch (Exception e) {
			LOGGER.error("The BlackList was not synchronized");
		}
	}

	public boolean removeContactFromBlackList(Contato contato) {
		boolean contactWasRemoved = getBlackListContacts().remove(contato);
		if (contactWasRemoved) {
			persistXML();
		}
		return contactWasRemoved;
	}

	@Override
	public void setCOMPort(String commPortIdentifier) throws FormDataException {
		LOGGER.info("Setting CommPortIdentifier with value: " + commPortIdentifier);
		if ("".equals(commPortIdentifier) || commPortIdentifier == null) { //$NON-NLS-1$
			throw new FormDataException(Messages.getString("MESSAGE.INVALID_COM_PORT_MSG")); //$NON-NLS-1$
		}
		this.commPortIdentifier = commPortIdentifier;
	}

	public void setCurrentContact(Contato currentContact) {
		LOGGER.info("Setting Current Contact with value: " + currentContact.getFormattedContact());
		this.currentContact = currentContact;
	}

	@Override
	public void setListNotificationReceivers(List<String> listNotificatinReceivers) throws FormDataException {
		if (listNotificatinReceivers == null || listNotificatinReceivers.size() == 0) {
			throw new FormDataException(Messages.getString("MESSAGE.INVALID_LIST_NOTIFICATION_RECEIVERS_MSG")); //$NON-NLS-1$
		}
		LOGGER.info("Setting ListNotificationReceivers with value: " + listNotificatinReceivers.toString());
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
