package br.com.campanhasms.properties;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FormProperties {
	private static final String BUNDLE_NAME = "br.com.campanhasms.properties.formproperties"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private FormProperties() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
