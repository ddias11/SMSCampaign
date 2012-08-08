package br.com.campanhasms.sms.reports;

public abstract class AbstractReportsRequirements implements Runnable {

	public enum ReportRequiredType {
		EMAIL_RATING(2),
		NOT_DEFINED(-1),
		SMS_RATING(1);

		private Integer value;

		private ReportRequiredType(Integer value) {
			this.value = value;
		}

		public static ReportRequiredType parse(Integer value) {
			for (ReportRequiredType reportRequiredType : values()) {
				if (reportRequiredType.value.equals(value)) {
					return reportRequiredType;
				}
			}
			return NOT_DEFINED;
		}

		public Integer getValue() {
			return this.value;
		}

	}

	public abstract void execute();

	@Override
	public void run() {
		execute();
	}
}
