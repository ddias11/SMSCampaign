package br.com.campanhasms.view.forms.validation.domain;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class PhoneFieldDocumentFilter extends ChainedDocumentFilter {

	private int sizeLimit;

	public PhoneFieldDocumentFilter(int sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public int getSizeLimit() {
		return sizeLimit;
	}

	@Override
	public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
		if (isSizeLimitValid(fb, str, 0) && isOnlyNumbers(str)) {
			fb.insertString(offs, str, a);
		} else {
			throw new BadLocationException("New characters exceeds max size of document", offs);
		}
	}

	private boolean isOnlyNumbers(String str) {
		for (Character character : str.toCharArray()) {
			if (!Character.isDigit(character)) {
				return false;
			}
		}
		return true;
	}

	private boolean isSizeLimitValid(FilterBypass fb, String str, int length) {
		int newFieldSize = fb.getDocument().getLength() + str.length() - length;
		return newFieldSize <= getSizeLimit();
	}

	@Override
	public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
		if (isSizeLimitValid(fb, str, length) && isOnlyNumbers(str)) {
			fb.replace(offs, length, str, a);
		} else {
			throw new BadLocationException("New characters exceeds max size of document", offs);
		}
	}

	public void setSizeLimit(int sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

}
