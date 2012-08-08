package br.com.campanhasms.properties;


public final class JobsScheduleProperties {
	private static final String BUNDLE_NAME = "res/properties/jobsscheduleproperties.properties"; //$NON-NLS-1$


	private JobsScheduleProperties() {
	}


	public static String getString(String key) {
		return PropertiesReader.readProperty(BUNDLE_NAME, key);
	}
}
