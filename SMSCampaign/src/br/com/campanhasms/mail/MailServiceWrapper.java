package br.com.campanhasms.mail;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import br.com.campanhasms.properties.MailProperties;
import br.com.campanhasms.zipping.ZipUtility;

public class MailServiceWrapper {

	private final static Logger LOGGER = Logger.getLogger(MailServiceWrapper.class);

	private HtmlEmail mailMessageInstace;

	private EmailAttachment getEmailAttachment() throws IOException {

		File appDirectory = new File(".");
		zipLogFiles();

		File[] listFiles = getFilesByPattern(appDirectory, ".*\\.zip");

		Arrays.sort(listFiles);

		EmailAttachment attachment = new EmailAttachment();
		File lastZippedLogFiles = listFiles[listFiles.length - 1];
		attachment.setPath(lastZippedLogFiles.getAbsolutePath());
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("SMS Campaign Logs; " + (listFiles.length > 0 ? "There are previous zip files in the App Folder" : ""));
		attachment.setName(lastZippedLogFiles.getName());

		return attachment;
	}

	private String getFileIndex() {
		final String NO_INDEX = "";
		File[] existentZipFiles = getFilesByPattern(new File("."), ".*\\.zip");
		if (existentZipFiles.length == 0) {
			return NO_INDEX;
		} else {
			return existentZipFiles.length + "";
		}
	}

	private File[] getFilesByPattern(File appDirectory, final String matchPattern) {
		return appDirectory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().matches(matchPattern);
			}
		});
	}

	private HtmlEmail getMailMessageInstace() {
		if (mailMessageInstace == null) {
			mailMessageInstace = new HtmlEmail();
		}

		return mailMessageInstace;
	}

	public void sendGmailMessage(final String messageContent, final List<String> to, final String subject) throws EmailException {
		String messageContentToSend = "<HTML>" + messageContent + "</HTML>"; //$NON-NLS-1$ //$NON-NLS-2$
		for (String mailAddress : to) {
			getMailMessageInstace().addTo(mailAddress);
		}

		getMailMessageInstace().setHostName(MailProperties.getString("MailServiceWrapper.SMTP")); //$NON-NLS-1$
		getMailMessageInstace().setFrom(MailProperties.getString("MailServiceWrapper.FROM_MAIL_ADDRESS")); //$NON-NLS-1$
		getMailMessageInstace().setSubject(subject);
		getMailMessageInstace().setHtmlMsg(messageContentToSend);
		getMailMessageInstace().setMsg(messageContentToSend);
		getMailMessageInstace().setAuthentication(
				MailProperties.getString("MailServiceWrapper.FROM_MAIL_ADDRESS"), MailProperties.getString("MailServiceWrapper.FROM_MAIL_PASSWORD")); //$NON-NLS-1$ //$NON-NLS-2$
		getMailMessageInstace().setSmtpPort(Integer.valueOf(MailProperties.getString("MailServiceWrapper.SMTP_PORT"))); //$NON-NLS-1$
		getMailMessageInstace().setSSL(Boolean.valueOf(MailProperties.getString("MailServiceWrapper.SSL"))); //$NON-NLS-1$
		getMailMessageInstace().setTLS(Boolean.valueOf(MailProperties.getString("MailServiceWrapper.TLS"))); //$NON-NLS-1$
		try {
			EmailAttachment emailAttachment = getEmailAttachment();
			getMailMessageInstace().attach(emailAttachment);
		} catch (Exception e) {
			LOGGER.error("Error when attaching zip files");
		}
		getMailMessageInstace().send();
		setMailMessageInstace(null);

	}

	private void setMailMessageInstace(HtmlEmail mailMessageInstace) {
		this.mailMessageInstace = mailMessageInstace;
	}

	private void zipLogFiles() throws IOException {
		File directoryToZip = new File("logs");
		ZipUtility.zipDirectory(directoryToZip, new File("logs" + getFileIndex() + ".zip"));
	}

}
