package br.com.campanhasms.sms.reports;

import org.apache.log4j.Logger;

import br.com.campanhasms.mail.RatingMailMessage;

public class EmailRatingReport extends AbstractReportsRequirements {

	private static final Logger LOGGER = Logger.getLogger(EmailRatingReport.class);

	@Override
	public void execute() {
		try {
			LOGGER.info("Trying to send Email Rating Report");
			new RatingMailMessage().send();
			LOGGER.info("Email Rating Report sended ");
		} catch (Exception e1) {
			LOGGER.error("Error when trying to send Email Rating Report", e1);
		}
	}

}
