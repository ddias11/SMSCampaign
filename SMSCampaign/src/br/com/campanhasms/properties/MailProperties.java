package br.com.campanhasms.properties;


public final class MailProperties {
	private static final String BUNDLE_NAME = "res/properties/mailproperties.properties"; //$NON-NLS-1$


	private MailProperties() {
	}


	public static String getString(String key) {
		return PropertiesReader.readProperty(BUNDLE_NAME, key);
	}
}
