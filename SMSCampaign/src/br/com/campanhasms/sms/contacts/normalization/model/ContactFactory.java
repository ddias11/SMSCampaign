package br.com.campanhasms.sms.contacts.normalization.model;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import br.com.campanhasms.model.Contato;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public final class ContactFactory {

	private static ArrayList<ContactNormalizationRule> contactNormalizationRules = null;
	private static ContactFactory instance = null;
	private static final Logger LOGGER = Logger.getLogger(ContactFactory.class);
	private static final String NORMALIZATION_CONTACTS_XML = "res/properties/contactsNormalization.xml";

	private ContactFactory() {
	}

	private static ArrayList<ContactNormalizationRule> getContactNormalizationRules() {
		return contactNormalizationRules;
	}

	public static ContactFactory getInstance() {
		if (instance == null) {
			instance = new ContactFactory();
			contactNormalizationRules = getPersistedNormalizationRules();

		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<ContactNormalizationRule> getPersistedNormalizationRules() {

		try {
			XStream xstream = new XStream(new StaxDriver());
			xstream.autodetectAnnotations(true);
			return (ArrayList<ContactNormalizationRule>) xstream.fromXML(new File(NORMALIZATION_CONTACTS_XML));
		} catch (Exception e) {
			LOGGER.error("Error when reading Black List Contacts from file: " + NORMALIZATION_CONTACTS_XML, e);
			throw e;
		}
	}

	public Contato createContact(Long contactNumber) {
		Long contactNumberLong = contactNumber;
		for (ContactNormalizationRule contactNormalizationRule : getContactNormalizationRules()) {
			Pattern regex = Pattern.compile(contactNormalizationRule.getMatcherRegex());
			Matcher regexMatcher = regex.matcher(String.valueOf(contactNumber));
			try {
				if (regexMatcher.find()) {
					contactNumberLong = (Long.valueOf(regexMatcher.replaceAll(contactNormalizationRule.getReplacerRegex())));
					break;
				}
			} catch (Exception e) {
				LOGGER.error("Erron when normalizating the contact " + contactNumber + " with the Matcher Regex: " + contactNormalizationRule.getMatcherRegex()
						+ " and Replacer Regex: " + contactNormalizationRule.getReplacerRegex(), e);
			}
		}

		return new Contato(contactNumberLong);
	}

}
