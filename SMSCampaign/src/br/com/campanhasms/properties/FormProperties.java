package br.com.campanhasms.properties;


public final class FormProperties {
	private static final String BUNDLE_NAME = "res/properties/formproperties.properties"; //$NON-NLS-1$


	private FormProperties() {
	}


	public static String getString(String key) {
		return PropertiesReader.readProperty(BUNDLE_NAME, key);
	}
}
