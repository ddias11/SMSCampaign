package br.com.campanhasms.properties;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PrevaylerProperties {
	private static final String BUNDLE_NAME = "br.com.campanhasms.properties.prevaylerproperties"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private PrevaylerProperties() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
