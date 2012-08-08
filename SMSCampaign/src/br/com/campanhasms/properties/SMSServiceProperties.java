package br.com.campanhasms.properties;


public final class SMSServiceProperties {
	private static final String BUNDLE_NAME = "res/properties/smsserviceproperties.properties"; //$NON-NLS-1$


	private SMSServiceProperties() {
	}


	public static String getString(String key) {
		return PropertiesReader.readProperty(BUNDLE_NAME, key);
	}
}
