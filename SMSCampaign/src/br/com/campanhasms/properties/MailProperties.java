package br.com.campanhasms.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class MailProperties {
	private static final String BUNDLE_NAME = "res/properties/mailproperties.properties"; //$NON-NLS-1$

	private MailProperties() {
	}

	public static String getString(String key) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(BUNDLE_NAME)));
			return properties.getProperty(key);
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}
}
