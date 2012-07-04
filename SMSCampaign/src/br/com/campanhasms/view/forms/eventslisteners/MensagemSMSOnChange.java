package br.com.campanhasms.view.forms.eventslisteners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import br.com.campanhasms.properties.SMSServiceProperties;

public class MensagemSMSOnChange implements KeyListener, CaretListener {

	private JLabel lblReference;

	public MensagemSMSOnChange(JLabel lblReference) {
		super();
		this.lblReference = lblReference;
	}

	@Override
	public void caretUpdate(CaretEvent event) {
		JTextArea textArea = (JTextArea) event.getSource();
		lblReference
				.setText(new Integer(SMSServiceProperties.getString("MensagemSMS.SMS_LENGTH")) - textArea.getText().length() + ""); //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Override
	public void keyPressed(KeyEvent event) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent event) {
		JTextArea textArea = (JTextArea) event.getSource();
		if (textArea.getText().length() >= new Integer(SMSServiceProperties.getString("MensagemSMS.SMS_LENGTH"))) { //$NON-NLS-1$
			event.consume();
		}
	}

}
