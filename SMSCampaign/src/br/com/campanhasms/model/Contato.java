package br.com.campanhasms.model;

import java.io.Serializable;

import br.com.campanhasms.properties.Messages;

public class Contato implements Serializable, Comparable<Contato> {
	private static final long serialVersionUID = 1713352709080995366L;
	private static final Integer DEFAULT_DDD = 11;
	private static final Integer DEFAULT_DDI = 55;
	private Integer ddd;
	private Integer ddi;
	private Integer lineNumber;
	private Integer prefix;

	public Contato(Integer ddi, Integer ddd, Integer prefix, Integer lineNumber) {
		super();
		this.ddi = ddi;
		this.ddd = ddd;
		this.prefix = prefix;
		this.lineNumber = lineNumber;
	}

	public Contato(String contact) throws Exception {
		super();
		contact = contact.replaceAll("\\D", ""); //$NON-NLS-1$ //$NON-NLS-2$
		int contactStirngFinalIndex = contact.length() - 1;
		try {
			this.lineNumber = Integer.valueOf(contact.substring(contactStirngFinalIndex - 3));
		} catch (Exception e) {
			throw new Exception(Messages.getString("Contato.INVALID_CONTACT_LINE_NUMBER_MSG")); //$NON-NLS-1$
		}

		try {
			this.prefix = Integer.valueOf(contact.substring(contactStirngFinalIndex - 7, contactStirngFinalIndex - 3));
		} catch (Exception e) {
			throw new Exception(Messages.getString("Contato.INVALID_CONTACT_PREFIX_MSG")); //$NON-NLS-1$
		}

		try {
			this.ddd = Integer.valueOf(contact.substring(contactStirngFinalIndex - 9, contactStirngFinalIndex - 7));
		} catch (Exception e) {
			this.ddd = DEFAULT_DDD;
		}

		try {
			this.ddi = Integer.valueOf(contact.substring(contactStirngFinalIndex - 11, contactStirngFinalIndex - 9));
		} catch (Exception e) {
			this.ddi = DEFAULT_DDI;
		}
	}

	@Override
	public int compareTo(Contato contato) {
		Long thisContatoAsLong = Long.valueOf(getFormattedContact());
		Long paramContatoAsLong = Long.valueOf(contato.getFormattedContact());
		return thisContatoAsLong.compareTo(paramContatoAsLong);
	}

	public Integer getDdd() {
		return ddd;
	}

	public Integer getDdi() {
		return ddi;
	}

	public String getFormattedContact() {
		return String.format("%02d%02d%04d%04d", getDdi(), getDdd(), getPrefix(), getLineNumber()); //$NON-NLS-1$
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public Integer getPrefix() {
		return prefix;
	}

	public void setDdd(Integer ddd) {
		this.ddd = ddd;
	}

	public void setDdi(Integer ddi) {
		this.ddi = ddi;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setPrefix(Integer prefix) {
		this.prefix = prefix;
	}

}
