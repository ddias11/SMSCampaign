package br.com.campanhasms.view.forms.exception;

public class FormDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8785313859940734738L;

	public FormDataException() {
	}

	public FormDataException(String message) {
		super(message);
	}

	public FormDataException(String message, Throwable exception) {
		super(message, exception);
	}

	public FormDataException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public FormDataException(Throwable exception) {
		super(exception);
	}

}
