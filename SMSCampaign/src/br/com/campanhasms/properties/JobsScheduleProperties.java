package br.com.campanhasms.properties;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class JobsScheduleProperties {
	private static final String BUNDLE_NAME = "jobsscheduleproperties"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private JobsScheduleProperties() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
