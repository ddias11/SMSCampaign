package br.com.campanhasms.properties;


public final class PrevaylerProperties {
	private static final String BUNDLE_NAME = "res/properties/prevaylerproperties.properties"; //$NON-NLS-1$


	private PrevaylerProperties() {
	}


	public static String getString(String key) {
		return PropertiesReader.readProperty(BUNDLE_NAME, key);
	}
}
