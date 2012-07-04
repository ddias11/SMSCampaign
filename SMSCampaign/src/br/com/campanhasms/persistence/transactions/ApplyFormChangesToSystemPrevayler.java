package br.com.campanhasms.persistence.transactions;

import java.util.Date;

import org.prevayler.Transaction;

import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.view.forms.exception.FormDataException;
import br.com.campanhasms.view.forms.model.IFormDataWrapper;

public class ApplyFormChangesToSystemPrevayler implements Transaction {

	private static final long serialVersionUID = -5267341497351894349L;
	private IFormDataWrapper formDataWrapper;

	public ApplyFormChangesToSystemPrevayler(IFormDataWrapper formDataWrapper) {
		super();
		this.formDataWrapper = formDataWrapper;
	}

	@Override
	public void executeOn(Object businessModel, Date arg1) {
		SystemPrevaylerModel systemPrevaylerModel = (SystemPrevaylerModel) businessModel;
		try {
			systemPrevaylerModel.setCOMPort(formDataWrapper.getCOMPort());
		} catch (FormDataException e) {
		}

		try {
			systemPrevaylerModel.setListNotificationReceivers(formDataWrapper.getListNotificationReceivers());
		} catch (FormDataException e) {
		}

		systemPrevaylerModel.setSMSPriorityContactsList(formDataWrapper.getSMSPriorityContactsList());
		try {
			systemPrevaylerModel.setTextMessage(formDataWrapper.getTextMessage());
		} catch (FormDataException e) {
		}
	}

}
