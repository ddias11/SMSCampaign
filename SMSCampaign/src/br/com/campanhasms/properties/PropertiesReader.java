package br.com.campanhasms.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public final class PropertiesReader {
	public static String readProperty(final String bundleName, final String key) {
		String property;
		try {
			final Properties properties = new Properties();
			final FileInputStream fileInputStream = new FileInputStream(new File(bundleName));
			properties.load(fileInputStream);
			fileInputStream.close();
			property = properties.getProperty(key);
		} catch (Exception e) {
			property = '!' + key + '!';
		}
		return property;
	}
}
