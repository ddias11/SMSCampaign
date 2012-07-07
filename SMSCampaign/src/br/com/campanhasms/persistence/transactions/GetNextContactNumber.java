package br.com.campanhasms.persistence.transactions;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.prevayler.Transaction;

import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.properties.SMSServiceProperties;

public class GetNextContactNumber implements Transaction {

	private static final long serialVersionUID = -3948842046799876001L;
	private SystemPrevaylerModel systemPrevaylerModel;

	@Override
	public void executeOn(Object businessModel, Date arg1) {
		systemPrevaylerModel = (SystemPrevaylerModel) businessModel;
		
		System.out.println("CurrentContact: " + systemPrevaylerModel.getCurrentContact());
		
		for (String contact : systemPrevaylerModel.getSMSPriorityContactsList()) {
			System.out.println("Priority Contact: (" + systemPrevaylerModel.getSMSPriorityContactsList().indexOf(contact) + ") => " + contact);	
		}

		if (hasNoPriorityList()) {
			if (hasNoCurrentContacts()) {
				initializeRandomContact();
			} else {
				do {
					setNextRandomContact();
				} while (isCurrentContactInThePriorityList());
			}
		} else {
			if (hasNoCurrentContacts()) {
				initializePriorityContact();
			} else {
				if (isTheLastPriorityContact()) {
					initializeRandomContact();
				} else {
					if (isCurrentContactInThePriorityList()) {
						setNextPriorityContact();
					} else {
						setNextRandomContact();
					}
				}
			}
		}
	}

	private int getIndexOfTheCurrentContact() {
		return systemPrevaylerModel.getSMSPriorityContactsList().indexOf(systemPrevaylerModel.getCurrentContact().toString());
	}

	private boolean hasNoCurrentContacts() {
		return systemPrevaylerModel.getCurrentContact() == null;
	}

	private boolean hasNoPriorityList() {
		return systemPrevaylerModel.getSMSPriorityContactsList().size() <= 0;
	}

	private void initializePriorityContact() {
		systemPrevaylerModel.setCurrentContact(Long.valueOf(systemPrevaylerModel.getSMSPriorityContactsList().get(0)));
	}

	private void initializeRandomContact() {
		systemPrevaylerModel.setCurrentContact(99999999L);
	}

	private boolean isCurrentContactInThePriorityList() {
		return getIndexOfTheCurrentContact() != -1;
	}

	private boolean isTheLastPriorityContact() {
		return getIndexOfTheCurrentContact() == systemPrevaylerModel.getSMSPriorityContactsList().size() - 1;
	}

	private void setNextPriorityContact() {
		systemPrevaylerModel.setCurrentContact(Long.valueOf(systemPrevaylerModel.getSMSPriorityContactsList().get(
				getIndexOfTheCurrentContact() + 1)));
	}

	private void setNextRandomContact() {
		int randomReduceNumber = new Random(Calendar.getInstance().getTimeInMillis()).nextInt(Integer
				.valueOf(SMSServiceProperties.getString("RANDOM_CONTACT.MAX_RANDOM_INTERVAL"))) + 1; //$NON-NLS-1$
		systemPrevaylerModel.setCurrentContact(systemPrevaylerModel.getCurrentContact() - randomReduceNumber);
		while (isCurrentContactInThePriorityList()) {
			systemPrevaylerModel.setCurrentContact(systemPrevaylerModel.getCurrentContact() - 1L);
		}

	}

}
