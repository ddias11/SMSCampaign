package br.com.campanhasms.sms.reports;



public abstract class AbstractReportsRequirements {

	public enum ReportRequiredType {
		NOT_DEFINED(-1),
		SMS_RATING(1),
		EMAIL_RATING(2);

		private Integer value;

		private ReportRequiredType(Integer value) {
			this.value = value;
		}
		
		public Integer getValue() {
			return this.value;
		}

		public static ReportRequiredType parse(Integer value) {
			for (ReportRequiredType reportRequiredType : values()) {
				if (reportRequiredType.value.equals(value)) {
					return reportRequiredType;
				}
			}
			return NOT_DEFINED;
		}

	}
}
