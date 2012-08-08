package br.com.campanhasms.properties;


public final class ApplicationProperties {
	private static final String BUNDLE_NAME = "res/properties/application.properties"; //$NON-NLS-1$

	private ApplicationProperties() {
	}

	public static String getString(String key) {
		return PropertiesReader.readProperty(BUNDLE_NAME, key);
	}
}
