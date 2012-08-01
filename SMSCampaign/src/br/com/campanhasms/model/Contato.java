package br.com.campanhasms.model;

import java.io.Serializable;

public class Contato implements Serializable, Comparable<Contato> {
	private static final String FORMAT_NORMALIZED_CONTACT = "%012d";
	private static final long serialVersionUID = 1713352709080995366L;
	private Long contactNumber;

	public Contato(Long contactNumber) {
		super();
		this.contactNumber = contactNumber;
	}

	@Override
	public int compareTo(Contato contato) {
		Long thisContatoAsLong = Long.valueOf(getFormattedContact());
		Long paramContatoAsLong = Long.valueOf(contato.getFormattedContact());
		return thisContatoAsLong.compareTo(paramContatoAsLong);
	}

	public String getFormattedContact() {
		return String.format(FORMAT_NORMALIZED_CONTACT, getContactNumber()); //$NON-NLS-1$
	}

	public Long getContactNumber() {
		return contactNumber;
	}

}
