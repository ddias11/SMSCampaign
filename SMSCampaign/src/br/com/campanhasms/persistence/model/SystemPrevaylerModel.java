package br.com.campanhasms.persistence.model;

import java.io.Serializable;
import java.util.List;

import br.com.campanhasms.properties.Messages;
import br.com.campanhasms.view.forms.exception.FormDataException;
import br.com.campanhasms.view.forms.model.IFormDataWrapper;

public class SystemPrevaylerModel implements Serializable, IFormDataWrapper {

	// public static final int CONTACT_PREFIXI_LOWER_BOUND = 5555;

	// public static final int CONTACT_UPPER_BOUND = 9999;

	// private static final Integer DDD = 11;

	// private static final Integer DDI = 55;

	private static final long serialVersionUID = -651641218710234008L;

	private String commPortIdentifier;

	private Long currentContact = null;

	private List<String> listNotificationReceivers;

	private Integer messagesConfirmedCounter;

	private Integer messagesReceivedCounter;

	private Integer messagesSendedCounter;

	private List<String> smsContactsBlackList;

	private String textMessage;

	public SystemPrevaylerModel() {

	}

	@Override
	public String getCOMPort() {
		return this.commPortIdentifier;
	}

	public Long getCurrentContact() {
		return currentContact;
	}

	@Override
	public List<String> getListNotificationReceivers() {
		return this.listNotificationReceivers;
	}

	public Integer getMessagesConfirmedCounter() {
		return messagesConfirmedCounter;
	}

	public Integer getMessagesReceivedCounter() {
		return messagesReceivedCounter;
	}

	public Integer getMessagesSendedCounter() {
		return messagesSendedCounter;
	}

	@Override
	public List<String> getSMSPriorityContactsList() {
		return this.smsContactsBlackList;
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

	public void incrementMessagesReceivedCounter() {
		if (messagesReceivedCounter == null) {
			messagesReceivedCounter = new Integer(0);
		}
		this.messagesReceivedCounter++;
	}

	public void incrementMessagesSendedCounter() {
		if (messagesSendedCounter == null) {
			messagesSendedCounter = new Integer(0);
		}
		this.messagesSendedCounter++;
	}

	@Override
	public void setCOMPort(String commPortIdentifier) throws FormDataException {
		if ("".equals(commPortIdentifier) || commPortIdentifier == null) { //$NON-NLS-1$
			throw new FormDataException(Messages.getString("MESSAGE.INVALID_COM_PORT_MSG")); //$NON-NLS-1$
		}
		this.commPortIdentifier = commPortIdentifier;
	}

	public void setCurrentContact(Long currentContact) {
		this.currentContact = currentContact;
	}

	@Override
	public void setListNotificationReceivers(List<String> listNotificatinReceivers) throws FormDataException {
		if (listNotificatinReceivers == null || listNotificatinReceivers.size() == 0) {
			throw new FormDataException(Messages.getString("MESSAGE.INVALID_LIST_NOTIFICATION_RECEIVERS_MSG")); //$NON-NLS-1$
		}
		this.listNotificationReceivers = listNotificatinReceivers;
	}

	@Override
	public void setSMSPriorityContactsList(List<String> smsContactsBlackList) {
		this.smsContactsBlackList = smsContactsBlackList;
	}

	@Override
	public void setTextMessage(String textMessage) throws FormDataException {
		if ("".equals(textMessage) || textMessage == null) { //$NON-NLS-1$
			throw new FormDataException(Messages.getString("MESSAGE.INVALID_TEXT_MESSAGE")); //$NON-NLS-1$
		}
		this.textMessage = textMessage;
	}

}
