package br.com.campanhasms.model;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.campanhasms.properties.ApplicationProperties;
import br.com.campanhasms.sms.contacts.normalization.model.ContactFactory;

public class Contato implements Serializable, Comparable<Contato> {

	private static final String FORMAT_NORMALIZED_CONTACT = "%012d";
	public static final Contato LOWER_BOUND_CONTACT = ContactFactory.getInstance().createContact(Long.valueOf(ApplicationProperties.getString("Application.LowerBoundContact")));

	private static Pattern pattern;
	private static final long serialVersionUID = 1713352709080995366L;
	public static final Contato UPPER_BOUND_CONTACT = ContactFactory.getInstance().createContact(Long.valueOf(ApplicationProperties.getString("Application.UpperBoundContact")));
	private Long contactNumber;

	public Contato(Long contactNumber) {
		super();
		this.contactNumber = contactNumber;
	}

	private static Pattern getPhoneNumberPattern() {
		if (pattern == null) {
			pattern = Pattern.compile("\\d{8}\\b");
		}
		return pattern;
	}

	@Override
	public int compareTo(Contato contato) {
		return getContactNumber().compareTo(contato.getContactNumber());
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	private Long getContactPhoneNumber() {
		Matcher matcher = getPhoneNumberPattern().matcher(getFormattedContact());
		if (matcher.find())
			return Long.valueOf(matcher.group());
		else
			return getContactNumber() % 100000000L;
	}

	public String getFormattedContact() {
		return String.format(FORMAT_NORMALIZED_CONTACT, getContactNumber()); 
	}

	private Long getLowerBoundsContact() throws NumberFormatException {
		return Long.valueOf(ApplicationProperties.getString("Application.LowerBoundContact"));
	}

	public boolean isContactUnderLowerBounds() {
		return getLowerBoundsContact() > getContactPhoneNumber();
	}

	@Override

	public boolean equals(Object obj) {
		if(obj instanceof Contato) {

			return ((Contato)obj).getContactNumber().equals(getContactPhoneNumber());
		}
		return super.equals(obj);
	}
	

}
