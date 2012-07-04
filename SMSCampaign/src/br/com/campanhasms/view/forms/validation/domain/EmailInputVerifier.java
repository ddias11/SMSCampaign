package br.com.campanhasms.view.forms.validation.domain;

import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

public class EmailInputVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent emailComponent) {
		if (emailComponent instanceof JFormattedTextField) {
			JFormattedTextField parsedEmailComponent = (JFormattedTextField) emailComponent;
			return Pattern.matches(".{3,}@.{3,15}\\..{0,10}", parsedEmailComponent.getText());
		}
		return false;
	}

}
