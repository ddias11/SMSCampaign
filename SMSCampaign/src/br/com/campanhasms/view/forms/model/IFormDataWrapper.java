package br.com.campanhasms.view.forms.model;

import java.util.List;

import br.com.campanhasms.view.forms.exception.FormDataException;

public interface IFormDataWrapper {

	public String getCOMPort();

	public List<String> getListNotificationReceivers();

	public List<String> getSMSPriorityContactsList();

	public String getTextMessage();

	public void setCOMPort(String commPortIdentifier) throws FormDataException;

	public void setListNotificationReceivers(List<String> listNotificatinReceivers) throws FormDataException;

	public void setSMSPriorityContactsList(List<String> smsPriorityContactsList);

	public void setTextMessage(String textMessage) throws FormDataException;

}
