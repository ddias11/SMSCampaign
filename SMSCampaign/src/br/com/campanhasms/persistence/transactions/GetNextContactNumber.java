package br.com.campanhasms.persistence.transactions;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.prevayler.Transaction;

import br.com.campanhasms.persistence.model.SystemPrevaylerModel;
import br.com.campanhasms.properties.SMSServiceProperties;

public class GetNextContactNumber implements Transaction {

	private static final Logger LOGGER = Logger.getLogger(GetNextContactNumber.class);
	private static final long serialVersionUID = -3948842046799876001L;
	private SystemPrevaylerModel systemPrevaylerModel;

	@Override
	public void executeOn(Object businessModel, Date arg1) {
		systemPrevaylerModel = (SystemPrevaylerModel) businessModel;

		LOGGER.info("Getting The CurrentContact value: " + systemPrevaylerModel.getCurrentContact());

		if (hasNoPriorityList()) {
			if (hasNoCurrentContacts()) {
				initializeRandomContact();
			} else {
				setNextRandomContact();
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
		return systemPrevaylerModel.getSMSPriorityContactsList().indexOf(
				systemPrevaylerModel.getCurrentContact().toString());
	}

	public int getRandomNumber() throws NumberFormatException {
		return new Random(Calendar.getInstance().getTimeInMillis()).nextInt(Integer.valueOf(SMSServiceProperties
				.getString("RANDOM_CONTACT.MAX_RANDOM_INTERVAL"))) + 1; //$NON-NLS-1$
	}

	private boolean hasNoCurrentContacts() {
		boolean hasNoCurrentContact = systemPrevaylerModel.getCurrentContact() == null;
		LOGGER.info("Has No CurrentContacts? " + hasNoCurrentContact);
		return hasNoCurrentContact;
	}

	private boolean hasNoPriorityList() {
		boolean hasNoPriorityList = systemPrevaylerModel.getSMSPriorityContactsList().size() <= 0;
		LOGGER.info("Has No Priority List? " + hasNoPriorityList);
		return hasNoPriorityList;
	}

	private void initializePriorityContact() {
		Long firstPriorityContact = Long.valueOf(systemPrevaylerModel.getSMSPriorityContactsList().get(0));
		LOGGER.info("Initializing the first priority Contact with value: " + firstPriorityContact);
		systemPrevaylerModel.setCurrentContact(firstPriorityContact);
	}

	private void initializeRandomContact() {
		Long initialRandomContact = 99999999L - getRandomNumber();
		LOGGER.info("Initializing Random Contact with value: " + initialRandomContact);
		systemPrevaylerModel.setCurrentContact(initialRandomContact);
	}

	private boolean isCurrentContactInThePriorityList() {
		return getIndexOfTheCurrentContact() != -1;
	}

	private boolean isTheLastPriorityContact() {
		boolean isTheLastPriorityContact = getIndexOfTheCurrentContact() == systemPrevaylerModel
				.getSMSPriorityContactsList().size() - 1;
		LOGGER.info("Is the Last Priority Contact? " + isTheLastPriorityContact);
		return isTheLastPriorityContact;
	}

	private void setNextPriorityContact() {
		Long nextPriorityContact = Long.valueOf(systemPrevaylerModel.getSMSPriorityContactsList().get(
				getIndexOfTheCurrentContact() + 1));
		LOGGER.info("Setting the next priority contact with the value: " + nextPriorityContact);
		systemPrevaylerModel.setCurrentContact(nextPriorityContact);
	}

	private void setNextRandomContact() {
		int randomReduceNumber = getRandomNumber();
		systemPrevaylerModel.setCurrentContact(systemPrevaylerModel.getCurrentContact() - randomReduceNumber);
		while (isCurrentContactInThePriorityList()) {
			Long newCurrentContact = systemPrevaylerModel.getCurrentContact() - 1L;
			LOGGER.info("The contact " + systemPrevaylerModel.getCurrentContact() + " is in the Priority List;"
					+ " Changing to " + newCurrentContact);
			systemPrevaylerModel.setCurrentContact(newCurrentContact);
		}

	}

}
