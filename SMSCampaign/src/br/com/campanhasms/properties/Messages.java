package br.com.campanhasms.properties;


public final class Messages {
	private static final String BUNDLE_NAME = "res/properties/messages.properties"; //$NON-NLS-1$


	private Messages() {
	}


	public static String getString(String key) {
		return PropertiesReader.readProperty(BUNDLE_NAME, key);
	}
}
