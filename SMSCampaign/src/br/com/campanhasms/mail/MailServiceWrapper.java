package br.com.campanhasms.mail;

import java.util.List;

import org.apache.commons.mail.HtmlEmail;

import br.com.campanhasms.properties.MailProperties;

public class MailServiceWrapper {

	private HtmlEmail mailMessageInstace;

	private HtmlEmail getMailMessageInstace() {
		if (mailMessageInstace == null) {
			mailMessageInstace = new HtmlEmail();
		}

		return mailMessageInstace;
	}

	public void sendGmailMessage(String messageContent, List<String> to, String subject) {
		try {
			messageContent = "<HTML>" + messageContent + "</HTML>"; //$NON-NLS-1$ //$NON-NLS-2$
			for (String mailAddress : to) {
				getMailMessageInstace().addTo(mailAddress);
			}

			getMailMessageInstace().setHostName(MailProperties.getString("MailServiceWrapper.SMTP")); //$NON-NLS-1$
			getMailMessageInstace().setFrom(MailProperties.getString("MailServiceWrapper.FROM_MAIL_ADDRESS")); //$NON-NLS-1$
			getMailMessageInstace().setSubject(subject);
			getMailMessageInstace().setHtmlMsg(messageContent);
			getMailMessageInstace().setMsg(messageContent);
			getMailMessageInstace()
					.setAuthentication(
							MailProperties.getString("MailServiceWrapper.FROM_MAIL_ADDRESS"), MailProperties.getString("MailServiceWrapper.FROM_MAIL_PASSWORD")); //$NON-NLS-1$ //$NON-NLS-2$
			getMailMessageInstace().setSmtpPort(new Integer(MailProperties.getString("MailServiceWrapper.SMTP_PORT"))); //$NON-NLS-1$
			getMailMessageInstace().setSSL(new Boolean(MailProperties.getString("MailServiceWrapper.SSL"))); //$NON-NLS-1$
			getMailMessageInstace().setTLS(new Boolean(MailProperties.getString("MailServiceWrapper.TLS"))); //$NON-NLS-1$
			getMailMessageInstace().send();
			setMailMessageInstace(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setMailMessageInstace(HtmlEmail mailMessageInstace) {
		this.mailMessageInstace = mailMessageInstace;
	}

}
